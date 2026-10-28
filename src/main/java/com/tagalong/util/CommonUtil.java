package com.tagalong.util;

import java.util.UUID;

public class CommonUtil {
    public static String getAlphaNumericUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
