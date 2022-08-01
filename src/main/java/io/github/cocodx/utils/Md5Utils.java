package io.github.cocodx.utils;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author amazfit
 * @date 2022-08-01 下午9:23
 **/
public class Md5Utils {

    public static String md5(String password){
        MessageDigest digest = null;
        byte[] result = new byte[0];
        try {
            digest = MessageDigest.getInstance("md5");
            result = digest.digest(password.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(result);
    }
}
