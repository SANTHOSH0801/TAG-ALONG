package com.tagalong.util;

import java.util.Locale;

public class BasicValidator {
    private static final EmailValidator emailValidator = EmailValidator.getInstance();


    public static boolean isEmailValid(String email) {
        return emailValidator.isValid(email); 
    }

    public static String getValidEmail(String email){
        if (email == null){
            return null;
        }
        if (isEmailValid(email)) {
            return email.toLowerCase(Locale.ENGLISH);
        }
        return null;
    }
}