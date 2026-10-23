package com.tagalong.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HexFormat;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PassworHashdHelper {

    private static final PasswordEncoder BCRYPT = new BCryptPasswordEncoder();
    private static final String PBKDF2_ALGO = "PBKDF2WithHmacSHA1";
    private static final String SEP = ":";

    public static boolean isPbkdf2Format(String hashPassword){
        int first = hashPassword.indexOf(SEP);
        int second = first > 0 ? hashPassword.indexOf(SEP, first + 1) : -1;
        if (first <= 0 || second <= first + 1 || second >= hashPassword.length() - 1){
            return false;
        }
        
        try {
            Integer.parseInt(hashPassword.substring(0, first));
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
    }

    private static byte[] pbkdf2(String password, byte[] salt, int iteration, int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iteration, bytes * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGO);
            return skf.generateSecret(spec).getEncoded();
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++){
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }


    public static boolean validatePbkdf2(String rawPassword, String correctHash)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
            String [] parts = correctHash.split(SEP);
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = HexFormat.of().parseHex(parts[1]);
            byte[] expected = HexFormat.of().parseHex(parts[2]);
            byte[] computed = pbkdf2(rawPassword, salt, iterations, iterations);
            return slowEquals(expected, computed);
        }


    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null){
            return false;
        }
        
        try {
            return validatePbkdf2(rawPassword, encodedPassword);
        } catch (Exception e) {
            return false;
        }
    }

}
