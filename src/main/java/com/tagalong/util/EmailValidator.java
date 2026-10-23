package com.tagalong.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    private static final EmailValidator EMAIL_VALIDATOR = new EmailValidator();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\s*?(.+)@(.+?)\\s*$");
    private static final Pattern IP_DOMAIN_PATTERN = Pattern.compile("\\[([^\\]]+)\\]");
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    private static final Pattern DOMAIN_PATTERN = Pattern.compile(
            "^(?!-)(?:[a-zA-Z0-9-]{1,63}(?<!-)\\.)+[a-zA-Z]{2,63}$");
    // Regex for a single label, used for local domains like "localhost".
    private static final Pattern LOCAL_DOMAIN_PATTERN = Pattern.compile(
            "^(?!-)[a-zA-Z0-9-]{1,63}(?<!-)$");
    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|"
            + "^(?:[0-9a-fA-F]{1,4}:){1,7}:$|"
            + "^(?:[0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}$|"
            + "^(?:[0-9a-fA-F]{1,4}:){1,5}(?::[0-9a-fA-F]{1,4}){1,2}$|"
            + "^(?:[0-9a-fA-F]{1,4}:){1,4}(?::[0-9a-fA-F]{1,4}){1,3}$|"
            + "^(?:[0-9a-fA-F]{1,4}:){1,3}(?::[0-9a-fA-F]{1,4}){1,4}$|"
            + "^(?:[0-9a-fA-F]{1,4}:){1,2}(?::[0-9a-fA-F]{1,4}){1,5}$|"
            + "^[0-9a-fA-F]{1,4}:(?:(?::[0-9a-fA-F]{1,4}){1,6})$|"
            + "^:(?:(?::[0-9a-fA-F]{1,4}){1,7}|:)$");
    private static final Pattern USER_PATTERN = Pattern.compile(
            "^\\s*(((\\\\.)|[^\\s\\p{Cntrl}\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"(\\\\\"|[^\"])*\"))(\\.(((\\\\.)|[^\\s\\p{Cntrl}\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"(\\\\\"|[^\"])*\")))*$");

    private static final int MAX_USERNAME_LEN = 64;

    protected EmailValidator() {
    }

    ;

    public static EmailValidator getInstance() {
        return EMAIL_VALIDATOR;
    }

    ;

    public boolean isValid(String email) {
        if (email == null) {
            return false;
        } else if (email.endsWith(".")) {
            return false;
        } else {
            Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
            if (!emailMatcher.matches()) {
                return false;
            } else if (!this.isValidUser(emailMatcher.group(1))) {
                return false;
            } else {
                return this.isValidDomain(emailMatcher.group(2));
            }
        }
    }

    ;

    protected boolean isValidUser(String user) {
        return user != null && user.length() <= MAX_USERNAME_LEN && USER_PATTERN.matcher(user).matches();
    }

    private boolean isValidIpAddress(String ipAddress) {
        return IPV4_PATTERN.matcher(ipAddress).matches() || IPV6_PATTERN.matcher(ipAddress).matches();
    }

    private boolean isValidDomainStructure(String domain) {
        if (DOMAIN_PATTERN.matcher(domain).matches()) {
            return true;
        }
        return LOCAL_DOMAIN_PATTERN.matcher(domain).matches();
    }

    public boolean isValidDomain(String domain) {
        if (domain == null || domain.trim().isEmpty()) {
            return false;
        }
        // 1. Check if it's an IP address in brackets (e.g., [1.2.3.4])
        Matcher ipMatcher = IP_DOMAIN_PATTERN.matcher(domain);
        if (ipMatcher.matches()) {
            return isValidIpAddress(ipMatcher.group(1));
        }

        // 2. If not an IP, treat as a domain name
        boolean isValidStructure = isValidDomainStructure(domain);

        // Lenient validation: can be a valid domain OR just a TLD
        boolean isTld = LOCAL_DOMAIN_PATTERN.matcher(domain).matches() && domain.matches("[a-zA-Z]+");
        return isValidStructure || (!domain.startsWith(".") && isTld);
    }

}
