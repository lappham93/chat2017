package com.mit.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Random;

/**
 * Created by truyetnguyen on 8/17/16.
 */
public class HmacDigestUtils {

    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private static final String HMAC_256 = "HmacSHA256";
    private static final int RANDOM_LENGTH = 16;


    public static String sign(String secretKey, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_256);
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(UTF8_CHARSET), HMAC_256);
            sha256_HMAC.init(secret_key);
            byte sig[] = sha256_HMAC.doFinal(data.getBytes(UTF8_CHARSET));
            return Base64.getEncoder().encodeToString(sig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String signToHex(String secretKey, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_256);
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(UTF8_CHARSET), HMAC_256);
            sha256_HMAC.init(secret_key);
            byte sig[] = sha256_HMAC.doFinal(data.getBytes(UTF8_CHARSET));
            StringBuilder result = new StringBuilder();
            for (int i=0; i < sig.length; i++) {
                result.append(Integer.toString( ( sig[i] & 0xff ) + 0x100, 16).substring( 1 ));
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateRadomString() {
        Random random = new Random();
        byte [] randomByte = new byte[RANDOM_LENGTH];
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            randomByte[i] = (byte)(random.nextInt(94) + 32);
        }

        return new String(randomByte);
    }

    public static void main(String[] args) {
        String sign = sign("123@321", "abcxyz");
        System.out.println(sign);
    }

}
