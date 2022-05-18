package com.ABH.Auction.services;

import com.ABH.Auction.authorization.CustomOAuth2User;
import com.ABH.Auction.domain.*;
import com.ABH.Auction.domain.enums.UserRole;
import com.ABH.Auction.repositories.ProductRepository;
import com.ABH.Auction.repositories.ReviewRepository;
import com.ABH.Auction.repositories.UserRepository;
import com.ABH.Auction.repositories.WishlistRepository;
import com.ABH.Auction.requests.UserInfoUpdateRequest;
import com.ABH.Auction.responses.*;
import com.ABH.Auction.services.image_service.ImageService;
import com.ABH.Auction.services.image_service.ImageServiceFactory;
import com.ABH.Auction.utility.AgeStructureHolder;
import com.ABH.Auction.utility.EmailValidator;
import com.ABH.Auction.utility.JWTUtility;
import com.ABH.Auction.utility.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@DependsOn({"factory"})
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND =
            "User with email %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;
    private final RoleService roleService;
    private final JWTUtility jwtUtility;
    private final EmailValidator emailValidator;
    private final AddressService addressService;
    private final CreditCardService creditCardService;
    private final PhoneNumberService phoneNumberService;
    private final String profileImgPlaceholder = "https://res.cloudinary.com/abh-auction/image/upload/v1648831369/xadoztzn3wbbsfxfyxnh.png";
    private final ReviewRepository reviewRepository;
    private final WishlistRepository wishlistRepository;
    private final EmailService emailService;
    private final ProductRepository productRepository;
    private ImageService imageService;

    @PostConstruct
    private void setImgService() {
        imageService = ImageServiceFactory.getImageService("cloudinary");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public MessageResponse signUpUser(User user) {
        MessageResponse mr = checkUser(user);
        if(!mr.getIsSuccess() || mr.getMessage() != null) {
            return mr;
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setImage(profileImgPlaceholder);
        saveUserWithRole(user, UserRole.ROLE_USER);
        return createAndSaveToken(user);
    }

    public Token createToken(String uuidToken, User user) {
        return new Token(
                uuidToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
    }

    public MessageResponse createAndSaveToken(User user) {
        String uuidToken = UUID.randomUUID().toString();
        Token token = createToken(uuidToken, user);
        tokenService.saveToken(token);
        return new MessageResponse(uuidToken, true);
    }

    public MessageResponse checkUser(User user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if(userFromDB.isEmpty()) {
            return new MessageResponse();
        }
        else if(userFromDB.get().isEnabled()) {
            return new MessageResponse("User already registered!", false);
        }
        return checkToken(user);
    }

    private MessageResponse checkToken(User user) {
        Token existingToken = tokenService.getTokenByUser(user.getEmail()).get();
        LocalDateTime confirmedAt = existingToken.getConfirmationTime();
        LocalDateTime expiresAt = existingToken.getExpirationTime();
        if(confirmedAt == null) {
            if (expiresAt.isAfter(LocalDateTime.now())) {
                return new MessageResponse("Request not confirmed!", false);
            } else {
                String uuidToken = UUID.randomUUID().toString();
                tokenService.setToken(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),
                        uuidToken, existingToken.getToken());
                return new MessageResponse(uuidToken, true);
            }
        }
        return new MessageResponse();
    }

    public void deleteUser(String email) {
        userRepository.delete(getUser(email).get());
    }

    public void saveOAuth2User(User user, String image) {
        user.setImage(image == null ? profileImgPlaceholder : image);
        user.setIsEnabled(true);
        saveUserWithRole(user, UserRole.ROLE_USER);
    }

    public void saveUserWithRole(User user, UserRole role) {
        userRepository.save(user);
        addUserRole(user.getEmail(), role);
    }

    public Map<String, String> loginOAuth2(CustomOAuth2User attributes) {
        Optional<User> userOptional = getUser(attributes.getMail());
        String name = "";

        Map<String, String> map = new HashMap<>();
        if(userOptional.isPresent() && userOptional.get().getPassword() != null) {
            map.put("user", "");
            return map;
        }
        else if (userOptional.isEmpty()) {
            String[] nameSurname = attributes.getFullName();
            saveOAuth2User(
                    new User(
                            nameSurname[0],
                            nameSurname[1],
                            attributes.getMail()
                    ),
                    attributes.getImage()
            );
            name +=  nameSurname[0] + " " + nameSurname[1];
        }
        else name += userOptional.get().getFirstName() + " " + userOptional.get().getLastName();
        final UserDetails userDetails = loadUserByUsername(attributes.getMail());

        map.put("token", jwtUtility.generateToken(userDetails));
        map.put("name", name);
        return map;
    }

    public void enableUser(String email) {
        userRepository.enableUser(email);
    }

    @Transactional
    public void addUserRole(String email, UserRole roleName) {
        User user = userRepository.findByEmail(email).get();
        Role role = roleService.getRole(roleName).get();
        user.getRoles().add(role);
    }

    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponse getUserResponseFromUser(User user) {
        return new UserResponse(user.getId(), user.getFirstName(),
                user.getLastName(), user.getImage(), getUserReview(user));
    }

    public ReviewResponse getUserReview(User user) {
        ReviewResponse response = new ReviewResponse();
        response.setNumberOfVotes(reviewRepository.getStars(user.getId(), null));
        long divisionValue = response.getNumberOfVotes() == 0 ? 1 : response.getNumberOfVotes();
        response.setFiveStars((reviewRepository
                .getStars(user.getId(), 5)*1f / divisionValue) * 100);
        response.setFourStars((reviewRepository
                .getStars(user.getId(), 4)*1f / divisionValue) * 100);
        response.setThreeStars((reviewRepository
                .getStars(user.getId(), 3)*1f /divisionValue) * 100);
        response.setTwoStars((reviewRepository
                .getStars(user.getId(), 2)*1f / divisionValue) * 100);
        response.setOneStar((reviewRepository
                .getStars(user.getId(), 1)*1f / divisionValue) * 100);
        Float value = reviewRepository.getRating(user.getId());
        response.setRating(value == null ? 0 : value);
        return response;
    }

    public UserResponse getUserResponseFromToken(String token) {
        if(token.isEmpty()){
            return null;
        }
        Optional<User> user = getUser(jwtUtility.getUsernameFromToken(token
                .replace("Bearer ", "")));
        return getUserResponseFromUser(user.get());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserFromToken(String token) {
        return getUser(jwtUtility.getUsernameFromToken(token
                .replace("Bearer ", ""))).get();
    }

    @Transactional
    public MessageResponse updateProfilePhoto(MultipartFile image, String token) {
        MultipartFile[] files = {image};
        List<String> photo = imageService.postImages(files);
        if(photo == null) {
            return new MessageResponse("Could not upload image!", false);
        }
        User user = getUserFromToken(token);
        user.setImage(photo.get(0));
        return new MessageResponse(user.getImage(), true);
    }

    public UserDetailsResponse getUserInfo(String token) {
        User user = getUserFromToken(token);
        return new UserDetailsResponse(getUserResponseFromUser(user),
                user.getDateOfBirth(),
                user.getPhoneNumber(),
                user.getCreditCard(),
                user.getAddress());
    }

    @Transactional
    public MessageResponse updateUser(String token, UserInfoUpdateRequest info) {
        MessageResponse mr = checkBasicUserInfo(info);
        if(!mr.getIsSuccess()) {
            return new MessageResponse(mr.getMessage(), false);
        }
        mr = CreditCardService.checkCreditCard(info);
        if(!mr.getIsSuccess()) {
            return new MessageResponse(mr.getMessage(), false);
        }
        Address adr;
        try {
            Integer zip = info.getZipCode() != null ?
                    Integer.parseInt(info.getZipCode()) : null;
            adr = new Address(info.getStreet(), info.getCity(),
                    zip, info.getState(), info.getCountry());
        }
        catch (Exception e) {
            return new MessageResponse("Invalid data!", false);
        }
        mr = AddressService.checkAddress(adr);
        if(!mr.getIsSuccess()) {
            return new MessageResponse(mr.getMessage(), false);
        }
        User user = getUserFromToken(token);
        if(info.getName() != null){
            user.setFirstName(info.getName());
        }
        if(info.getSurname() != null){
            user.setLastName(info.getSurname());
        }
        if(info.getEmail() != null){
            user.setEmail(info.getEmail());
        }
        if(info.getPhone() != null) {
            if(user.getPhoneNumber() == null) {
                PhoneNumber pn = new PhoneNumber(info.getPhone(), false);
                user.setPhoneNumber(phoneNumberService.addPhoneNumber(pn));
            }
            else {
                user.getPhoneNumber().setNumber(info.getPhone());
            }
        }
        if(info.getDateOfBirth() != null) {
            user.setDateOfBirth(info.getDateOfBirth());
        }
        try {
            mr = creditCardService.updateCard(user, info);
            if(!mr.getIsSuccess()) {
                return new MessageResponse(mr.getMessage(), false);
            }
            mr = addressService.updateAddress(user, adr);
            if(!mr.getIsSuccess()) {
                return new MessageResponse(mr.getMessage(), false);
            }
        }
        catch (Exception e) {
            return new MessageResponse(e.getMessage(), false);
        }
        return new MessageResponse("Profile successfully updated!", true);
    }

    public MessageResponse checkBasicUserInfo(UserInfoUpdateRequest info) {
        if(info.getName() != null && Validator.isNameInvalid(info.getName())){
            return new MessageResponse("Wrong first name format!", false);
        }
        if(info.getSurname() != null && Validator.isNameInvalid(info.getSurname())){
            return new MessageResponse("Wrong last name format!", false);
        }
        if(info.getEmail() != null && !emailValidator.test(info.getEmail())){
            return new MessageResponse("Wrong email format!", false);
        }
        if(info.getPhone() != null && Validator.isPhoneNumberInvalid(info.getPhone())) {
            return new MessageResponse("Wrong phone number format!", false);
        }
        if(info.getDateOfBirth() != null &&
                info.getDateOfBirth().getYear() > LocalDateTime.now().getYear() - 18) {
            return new MessageResponse("Your age group is critical!", false);
        }
        return new MessageResponse("Success", true);
    }

    @Transactional
    public void deactivateUser(String token) {
        //cannot reregister
        User user = getUserFromToken(token);
        //I already know what you think, mentor ╰(°ㅂ°)╯
        //should be boolean without setting email to null
        user.setReactivationEmail(user.getEmail());
        user.setEmail(null);
    }

    public Shipment getShipmentInfo(String token) {
        User user = getUserFromToken(token);
        return new Shipment(user.getAddress(), user.getPhoneNumber(), null);
    }

    public void updateInitialLogin(String email) {
        userRepository.updateInitialLogin(email, LocalDateTime.now());
    }

    public void updateLoginTime(String email) {
        userRepository.updateLoginTime(email, LocalDateTime.now());
    }

    public UsersResponse getUsers(final Integer page, final Integer users,
                                  final String sort, String search) {
        Pageable pageable = PageRequest.of(page, users).withSort(
                Sort.by(sort));
        if(search.isEmpty()) {
            search = null;
        }
        if(search != null) {
            search = "%" + String.join("%", search.split(" "))
                    .toLowerCase() + "%";
        }
        Page<User> usersPage = userRepository.getUsers(search, pageable);
        List<UserResponse> userResponses = usersPage.getContent().stream().map(e ->
                new UserResponse(e.getId(), e.getFirstName(), e.getLastName(), e.getImage()))
                .collect(Collectors.toList());
        return new UsersResponse(userResponses, usersPage.getTotalElements());
    }

    @Transactional
    public MessageResponse reactivateUser(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isPresent()) {
            User user = optUser.get();
            if(user.getReactivationEmail() != null) {
                user.setEmail(user.getReactivationEmail());
                user.setReactivationEmail(null);
                return new MessageResponse("User reactivated!", true);
            }
            return new MessageResponse("User already active!", false);
        }
        return new MessageResponse("No such user present!", false);
    }

    @Transactional
    public void deleteUserPermanently(Long id) {
        //delete cascade?
        //what if user deletes himself?
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Product> products = userRepository.getProductsForUser(id);
            for (Product p : products) {
                deleteProductForUser(p.getId());
            }
            userRepository.deleteRoleAsgForUser(id);
            userRepository.deleteTokensForUser(id);
            userRepository.deletePaymentsForUser(id);
            userRepository.deleteReviewsForUser(id);
            userRepository.deleteUserBids(id);
            Optional<Wishlist> userWishlist = wishlistRepository.getUserWishlist(id);
            if(userWishlist.isPresent()) {
                Wishlist wishlist = userWishlist.get();
                userRepository.deleteWishlistAsgForUser(wishlist.getId());
                wishlistRepository.delete(wishlist);
            }
            userRepository.delete(user);
        }
    }

    @Transactional
    public void deleteProductForUser(Long id) {
        productRepository.deleteColorAsgForProduct(id);
        productRepository.deleteSizeAsgForProduct(id);
        productRepository.deleteImagesForProduct(id);
        productRepository.deleteProductBids(id);
        productRepository.deletePaymentsForProduct(id);
        productRepository.deleteWishlistAsgForProduct(id);
        userRepository.deleteProductForUser(id);
    }

    public MessageResponse exportUser(Long id) {
        String fileName = "User information";
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isEmpty()) {
            return new MessageResponse("No such user!", false);
        }
        User user = optUser.get();
        try {
            ICsvBeanWriter beanWriter = new CsvBeanWriter(new FileWriter(fileName + ".csv"),
                    CsvPreference.STANDARD_PREFERENCE);
            String[] header = {"NAME", "SURNAME", "DATE OF BIRTH", "PROFILE PHOTO"};
            String[] nameMapping = {"firstName", "lastName", "dateOfBirth", "image"};
            beanWriter.writeHeader(header);
            beanWriter.write(user, nameMapping);
            Address adr = user.getAddress();
            if(adr != null) {
                String[] headerAdr = {"ADDRESS STREET", "CITY", "STATE"};
                String[] adrMapping = {"street", "city", "state"};
                beanWriter.writeHeader(headerAdr);
                beanWriter.write(adr, adrMapping);
            }
            beanWriter.close();
        } catch (IOException e) {
            return new MessageResponse("Error writing the CSV file!", false);
        }
        //deleting file? (what would happen if it was Async?)
        //what else to export and what about null values?
        String email = user.getEmail();
        //because deactivation works as it works right now
        if(email == null) {
            email = user.getReactivationEmail();
        }
        return emailService.sendFile(fileName, email,
                "CSV file of user information", "Here are Your information.", ".csv");
    }

    @Transactional
    public MessageResponse blockUser(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isEmpty()) {
            return new MessageResponse("Unknown user!", false);
        }
        User user = optUser.get();
        if(user.getIsBlocked()) {
            return new MessageResponse("User already blocked!", false);
        }
        user.setIsBlocked(true);
        return new MessageResponse("User blocked!", true);
    }

    public LoginActivityResponse getUsersLoginActivity() {
        LocalDateTime now = LocalDateTime.now();
        List<Object[]> data = userRepository.getUsersLoginActivity(now.minusDays(7), now.minusMonths(1));
        return data.stream().map(e -> new LoginActivityResponse(
                ((BigInteger) e[0]).longValue(), ((BigInteger) e[1]).longValue(),
                ((BigInteger) e[2]).longValue(), ((BigInteger) e[3]).longValue()
        )).collect(Collectors.toList()).get(0);
    }

    public AgeStructureResponse getUsersAgeStructure() {
        List<Object[]> data = userRepository.getUsersAgeStructure(LocalDateTime.now());
        AgeStructureHolder holder = data.stream().map(e -> new AgeStructureHolder(
                ((BigInteger) e[0]).longValue(), ((BigInteger) e[1]).longValue(),
                ((BigInteger) e[2]).longValue(), ((BigInteger) e[3]).longValue(),
                ((BigInteger) e[4]).longValue(), ((BigInteger) e[5]).longValue()
        )).collect(Collectors.toList()).get(0);
        if(holder.getNumberOfUsers().equals(0L)) {
            holder.setNumberOfUsers(1L);
        }
        //could make separate func for percentage calculation
        return new AgeStructureResponse(
                ((holder.getTeen()*1f) / holder.getNumberOfUsers()) * 100,
                ((holder.getAdult()*1f) / holder.getNumberOfUsers()) * 100,
                ((holder.getMiddleAge()*1f) / holder.getNumberOfUsers()) * 100,
                ((holder.getSenior()*1f) / holder.getNumberOfUsers()) * 100,
                ((holder.getUnfilled()*1f) / holder.getNumberOfUsers()) * 100
        );
    }
}
