package com.tagalong.util;

import java.util.regex.Pattern;

public class PasswordHelper {
    public static final String PASSWORD_REGEX = "(?=^.{8,}$)(?=.*\\d)(?=.*[!@#$%^&*]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

    public static boolean validatePasswordFormat(String password){
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
