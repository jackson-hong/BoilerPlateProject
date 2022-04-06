package com.jp.boiler.base.support.crypto;

import com.jp.boiler.base.properties.CryptoProperties;
import lombok.Builder;
import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AESCryptoFactory {

    public AESCryptoFactory(CryptoProperties cryptoProperties) {
        cryptoProperties.getAesSpecs()
                .forEach(this::addAESSpec);
    }

    private Map<String, AESCipher> cipherMap = new HashMap<>();

    public String encrypt(String targetName, String plainText) throws Exception{
        return getCipher(targetName).encrypt(plainText);
    }

    public String decrypt(String targetName, String cipherText) throws Exception{
        return getCipher(targetName).decrypt(cipherText);
    }

    private void addAESSpec(AESSpec aesSpec){
        cipherMap.putIfAbsent(aesSpec.getName(), new AESCipher(aesSpec));
    }

    public AESCipher createCipher(String aesKey, String ivKey, int bitSize){
        return AESCipher.builder()
                .key(aesKey)
                .iv(ivKey)
                .bitSize(bitSize)
                .enabled(true)
                .build();
    }

    public static class AESCipher {
        private String key;
        private String iv;
        private int bitSize;
        @Getter
        private boolean enabled;
        private final static String TRANSFORMATION = "AES/CBC/PKCS5Padding";

        @Builder
        public AESCipher(String key, String iv, int bitSize, boolean enabled) {
            this.key = key;
            this.iv = iv;
            this.bitSize = bitSize;
            this.enabled = enabled;
        }

        public AESCipher(AESSpec aesSpec){
            this(aesSpec.getKey(), aesSpec.getIv(), aesSpec.getBitSize(), aesSpec.isEnabled());
        }

        private Key getKey() throws Exception {
            Key keySpec;
            byte[] keyBytes = new byte[bitSize/8];
            byte[] bytes = null;

            bytes = key.getBytes("UTF-8");

            int len = bytes.length;
            if(len > keyBytes.length){
                len = keyBytes.length;
            }

            System.arraycopy(bytes, 0, keyBytes, 0, len);
            keySpec = new SecretKeySpec(keyBytes, "AES");

            return keySpec;
        }

        public String encrypt(String strBeforeEnc) throws Exception {
            if(!isEnabled()) return strBeforeEnc;

            Key keySpec = getKey();

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
            byte[] encrypted = cipher.doFinal(strBeforeEnc.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        }

        public String decrypt(String strBeforeDec) throws Exception {
            if(!isEnabled()) return strBeforeDec;

            Key keySpec = getKey();

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
            byte[] decodedBytes = Base64.getDecoder().decode(strBeforeDec);
            return new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8);
        }
    }


    public AESCipher getCipher(String cryptoTarget){
        if(cipherMap.containsKey(cryptoTarget)) return cipherMap.get(cryptoTarget);
        throw new RuntimeException("Unknown Crypto Target : " + cryptoTarget);
    }
}
