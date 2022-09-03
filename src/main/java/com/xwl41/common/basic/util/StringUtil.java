package com.xwl41.common.basic.util;

class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String toCamelCase(String name) {
        if (null == name || name.length() == 0) {
            return null;
        }

        int length = name.length();
        StringBuilder sb = new StringBuilder(length);
        boolean afterUnderline = false;

        for (int i = 0; i < length; ++i) {
            char c = name.charAt(i);
            if (c == '_') {
                afterUnderline = true;
            } else if (afterUnderline) {
                sb.append(Character.toUpperCase(c));
                afterUnderline = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
