package cn.surine.schedulex.super_import;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SuperUtil {
    public static String encrypt(String str) {
        try {
            return aes(URLEncoder.encode(str, "utf-8"), md5("friday_syllabus"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String aes(String str, String str2) {
        String str3 = "AES";
        String str4 = "utf-8";
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(str2.getBytes(str4)), str3);
            Cipher instance = Cipher.getInstance(str3);
            instance.init(1, secretKeySpec);
            return byteToStr(instance.doFinal(str.getBytes(str4)));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String byteToStr(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append('0');
                sb2.append(hexString);
                hexString = sb2.toString();
            }
            sb.append(hexString.toUpperCase());
        }
        return sb.toString();
    }

    private static String md5(String str) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            char[] cArr2 = new char[32];
            int i = 0;
            for (int i2 = 0; i2 < 16; i2++) {
                byte b = digest[i2];
                int i3 = i + 1;
                cArr2[i] = cArr[(b >>> 4) & 15];
                i = i3 + 1;
                cArr2[i3] = cArr[b & 15];
            }
            return new String(cArr2).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
