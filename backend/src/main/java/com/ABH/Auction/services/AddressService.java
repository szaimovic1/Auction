package com.ABH.Auction.services;

import com.ABH.Auction.domain.Address;
import com.ABH.Auction.domain.User;
import com.ABH.Auction.repositories.AddressRepository;
import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.utility.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public Address addAddress(Address adr) {
        return addressRepository.save(adr);
    }

    @Transactional
    public MessageResponse updateAddress(User user, Address address) {
        if(user.getAddress() != null) {
            if(address.getStreet() != null) {
                user.getAddress().setStreet(address.getStreet());
            }
            if(address.getCity() != null) {
                user.getAddress().setCity(address.getCity());
            }
            if(address.getZipCode() != null) {
                user.getAddress().setZipCode(address.getZipCode());
            }
            if(address.getState() != null) {
                user.getAddress().setState(address.getState());
            }
            if(address.getCountry() != null) {
                user.getAddress().setCountry(address.getCountry());
            }
            return new MessageResponse("Success", true);
        }
        if(address.getStreet() == null && address.getCity() == null &&
                address.getZipCode() == null && address.getState() == null &&
                address.getCountry() == null) {
            return new MessageResponse("Success", true);
        }
        return saveAddressUpdate(user, address);
    }

    @Transactional
    public MessageResponse saveAddressUpdate(User user, Address address) {
        if(address.getStreet() == null || address.getCity() == null ||
                address.getZipCode() == null || address.getState() == null ||
                address.getCountry() == null) {
            return new MessageResponse("Address info incomplete!", false);
        }
        else {
            Address adr = new Address(address.getStreet(), address.getCity(),
                    address.getZipCode(), address.getState(), address.getCountry());
            user.setAddress(addAddress(adr));
        }
        return new MessageResponse("Success", true);
    }

    public static MessageResponse checkAddress(Address address) {
        if(address.getCity() != null && Validator.isNameInvalid(address.getCity())) {
            return new MessageResponse("Wrong city format!", false);
        }
        if(address.getZipCode() != null && !Validator.isZipCodeValid(Integer.toString(address.getZipCode()))) {
            return new MessageResponse("Wrong zip code value!", false);
        }
        if(address.getState() != null && Validator.isNameInvalid(address.getState())) {
            return new MessageResponse("Wrong state format!", false);
        }
        if(address.getCountry() != null && Validator.isNameInvalid(address.getCountry())) {
            return new MessageResponse("Wrong country format!", false);
        }
        if(address.getStreet() != null && Validator.isStreetNameInvalid(address.getStreet())) {
            return new MessageResponse("Wrong street format!", false);
        }
        return new MessageResponse("Success", true);
    }

    public static MessageResponse checkAddressForProduct(Address adr) {
        if(adr.getCity() == null || adr.getStreet() == null ||
                adr.getCountry() == null || adr.getZipCode() == null) {
            return new MessageResponse("Missing address fields!", false);
        }
        return checkAddress(adr);
    }
}
