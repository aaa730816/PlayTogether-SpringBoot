package com.shu.tony.PlayTogether.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAEncodeUtil {
    public static String shaEncode(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(string.getBytes("UTF-8"));
        return byte2Hex(digest.digest());
    }

    private static String byte2Hex(byte[] digest) {
        StringBuffer buffer = new StringBuffer();
        String temp;
        for (int i = 0; i<digest.length;i++) {
            temp = Integer.toHexString(digest[i] & 0xFF);
            if (temp.length() == 1) {
                buffer.append("0");
            }
            buffer.append(temp);
        }
        return buffer.toString();
    }
}
