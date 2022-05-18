package com.ABH.Auction.services;

import com.ABH.Auction.domain.CreditCard;
import com.ABH.Auction.domain.User;
import com.ABH.Auction.repositories.CreditCardRepository;
import com.ABH.Auction.requests.UserInfoUpdateRequest;
import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.utility.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CreditCardService {
    private final CreditCardRepository creditCardRepository;

    public CreditCard addCreditCard(CreditCard card) {
        return creditCardRepository.save(card);
    }

    @Transactional
    public MessageResponse updateCard(User user, UserInfoUpdateRequest info) {
        if(user.getCreditCard() != null) {
            if(info.getNameCard() != null) {
                user.getCreditCard().setName(info.getNameCard());
            }
            if(info.getNumberCard() != null) {
                user.getCreditCard().setNumber(info.getNumberCard());
            }
            if(info.getMonthExpCard() != null) {
                user.getCreditCard().setExpirationMonth(info.getMonthExpCard());
            }
            if(info.getYearExpCard() != null) {
                user.getCreditCard().setExpirationYear(info.getYearExpCard());
            }
            if(info.getCvcCvv() != null) {
                user.getCreditCard().setCvcCvv(Integer.parseInt(info.getCvcCvv()));
            }
            return new MessageResponse("Success", true);
        }
        if(info.getNameCard() == null && info.getNumberCard() == null &&
                info.getCvcCvv() == null && info.getMonthExpCard() == null &&
                info.getYearExpCard() == null) {
            return new MessageResponse("Success", true);
        }
        return saveCreditCardUpdate(user, info);
    }

    @Transactional
    public MessageResponse saveCreditCardUpdate(User user, UserInfoUpdateRequest info) {
        if(info.getNameCard() == null || info.getNumberCard() == null ||
                info.getCvcCvv() == null || info.getMonthExpCard() == null ||
                info.getYearExpCard() == null) {
            return new MessageResponse("Credit card info incomplete!", false);
        }
        else {
            CreditCard cc = new CreditCard(info.getNameCard(), info.getNumberCard(),
                    info.getMonthExpCard(), info.getYearExpCard(), Integer.parseInt(info.getCvcCvv()));
            user.setCreditCard(addCreditCard(cc));
        }
        return new MessageResponse("Success", true);
    }

    public static MessageResponse checkCreditCard(UserInfoUpdateRequest info) {
        if(info.getNameCard() != null && Validator.isNameInvalid(info.getNameCard())) {
            return new MessageResponse("Wrong name on card format!", false);
        }
        if(info.getNumberCard() != null && !Validator.isCreditCardValid(info.getNumberCard())) {
            return new MessageResponse("Wrong card number format!", false);
        }
        if(info.getYearExpCard() != null) {
            int nowYear = LocalDateTime.now().getYear();
            int cardYear = Integer.parseInt(Integer.toString(nowYear)
                    .substring(0,2) + info.getYearExpCard());
            if(cardYear < nowYear) {
                return new MessageResponse("Bad card expiration date!", false);
            }
        }
        if(info.getMonthExpCard() != null &&
                !(info.getMonthExpCard() > 0 && info.getMonthExpCard() < 13)) {
            return new MessageResponse("Bad card expiration date!", false);
        }
        if(info.getCvcCvv() != null && !Validator.isCvcCvvValid(info.getCvcCvv())) {
            return new MessageResponse("Wrong CVC/CVV value!", false);
        }
        return new MessageResponse("Success", true);
    }
}
