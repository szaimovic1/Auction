package com.ABH.Auction.utility;

import com.ABH.Auction.responses.MessageResponse;

public class Validator {

    public static boolean isNameInvalid(String name) {
        if(name == null || name.length() < 1 || name.length() > 100) {
            return true;
        }
        return !name.matches("[A-Za-z ]*");
    }

    public static boolean isPhoneNumberInvalid(String number) {
        if(number == null || number.length() < 10 || number.length() > 100) {
            return true;
        }
        if(number.charAt(0) == '+') {
             return isNumberInvalid(number.substring(1));
        }
        return true;
    }

    public static boolean isNumberInvalid(String number) {
        if(number == null) {
            return true;
        }
        return !number.matches("[0-9]*");
    }

    public static boolean isCreditCardValid(String card) {
        if(card == null || card.length() < 15 || card.length() > 100) {
            return false;
        }
        String[] numbers = card.split("-");
        for (String i : numbers){
            if(isNumberInvalid(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCvcCvvValid(String value) {
        if(value == null || isNumberInvalid(value)) {
            return false;
        }
        return value.length() == 3;
    }

    public static boolean isZipCodeValid(String value) {
        return (value != null && value.length() == 5);
    }

    public static MessageResponse checkPassword(String pass) {
        MessageResponse mr = new MessageResponse();
        if(pass == null) {
            mr.setMessage("Missing password!");
            mr.setIsSuccess(false);
        }
        else if(pass.length() < 8) {
            mr.setMessage("Password must be at least 8 characters long!");
            mr.setIsSuccess(false);
        }
        else if(pass.length() > 200) {
            mr.setMessage("Password too long!");
            mr.setIsSuccess(false);
        }
        else if(pass.chars().filter(Character::isUpperCase).findAny().isEmpty()) {
            mr.setMessage("Password must contain at least one capital letter!");
            mr.setIsSuccess(false);
        }
        else if(pass.chars().filter(Character::isLowerCase).findAny().isEmpty()) {
            mr.setMessage("Password must contain at least one lowercase letter!");
            mr.setIsSuccess(false);
        }
        else if(pass.chars().filter(Character::isDigit).findAny().isEmpty()) {
            mr.setMessage("Password must contain at least one number!");
            mr.setIsSuccess(false);
        }
        return mr;
    }

    public static boolean isStreetNameInvalid(String street) {
        return street == null || street.isEmpty() || street.length() < 3 || street.length() > 100;
    }
}
