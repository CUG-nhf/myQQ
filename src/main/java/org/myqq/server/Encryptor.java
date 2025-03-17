package org.myqq.server;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Encryptor {
    private static final String ALGORITHM = "AES";

    // 生成一个随机密钥
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // AES密钥长度（128位）
        return keyGen.generateKey();
    }

    // 加密函数
    public static String encrypt(String input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // 解密函数
    public static String decrypt(String encrypted, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted);
    }

    // TODO:学习redis后,每个用户一个唯一密钥
    public static SecretKey getTmpKey() throws Exception {
        String secretKeyStr = "1234567890abcdef";
        byte[] keyBytes = secretKeyStr.getBytes();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
}
