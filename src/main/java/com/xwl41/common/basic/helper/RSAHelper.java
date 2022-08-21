package com.xwl41.common.basic.helper;

import com.xwl41.common.basic.constant.Algorithms;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.xwl41.common.basic.helper.RSAHelper.RSAKeyType.PRIVATE;
import static com.xwl41.common.basic.helper.RSAHelper.RSAKeyType.PUBLIC;
import static com.xwl41.common.basic.util.CoreUtil.FILE.saveToFile;

/**
 * RSAHelper RSA密钥使用相关
 */
public class RSAHelper {

    //////////////////////////////////////////////////////////////////////////////
    //    generate methods
    //////////////////////////////////////////////////////////////////////////////

    /**
     * 生成RSA密钥对
     *
     * @param keySize 密钥位数
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 无指定算法异常
     */
    public static Map<String, Key> genRSAKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(Algorithms.RSA.toString());
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        Map<String, Key> keyMap = new HashMap<>(2);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        keyMap.put(PUBLIC.toString(), publicKey);
        keyMap.put(PRIVATE.toString(), privateKey);
        return keyMap;
    }

    public static Map<String, String> genRSAKeyStringPair(int keySize) throws NoSuchAlgorithmException {
        Map<String, Key> keyMap = genRSAKeyPair(keySize);
        Map<String, String> keyStringMap = new HashMap<>();
        keyStringMap.put(PUBLIC.toString(), toEncodedString(keyMap.get(PUBLIC.toString())));
        keyStringMap.put(PRIVATE.toString(), toEncodedString(keyMap.get(PRIVATE.toString())));
        return keyStringMap;
    }

    /**
     * 保存密钥对到指定目录
     *
     * @param keyMap 密钥对
     * @param path   要保存密钥对文件的路径
     * @return 保存是否成功
     */
    public static boolean saveKeyPair(Map<String, Key> keyMap, String path) {
        boolean saved = false;
        Key publicKey = keyMap.get(PUBLIC.toString());
        Key privateKey = keyMap.get(PRIVATE.toString());

        String keyPairStr = publicKey.toString() + "\r\n"
                + "[" + Base64.getEncoder().encodeToString(publicKey.getEncoded()) + "]\r\n"
                + privateKey.toString() + "\r\n"
                + "[" + Base64.getEncoder().encodeToString(privateKey.getEncoded()) + "]\r\n";
        String publicKeyStr = genKeyContent(toEncodedString(publicKey), PUBLIC);
        String privateKeyStr = genKeyContent(toEncodedString(privateKey), PRIVATE);
        File p = new File(path);
        if (p.isDirectory()) {
            if (p.exists() || p.mkdirs()) {
                saveToFile(new ByteArrayInputStream(keyPairStr.getBytes()), path + File.separator + "keypair.txt");
                saveToFile(new ByteArrayInputStream(publicKeyStr.getBytes()), path + File.separator + "publicKey.pem");
                saveToFile(new ByteArrayInputStream(privateKeyStr.getBytes()), path + File.separator + "privateKey.pem");
                saved = true;
            }
        }
        return saved;
    }

    //////////////////////////////////////////////////////////////////////////////
    //    convert methods
    //////////////////////////////////////////////////////////////////////////////

    /**
     * 将字符串格式公钥转换为RSA公钥对象
     *
     * @param encodedBase64KeyStr 字符串格式公钥
     * @return 返回RSA公钥对象
     */
    public static RSAPublicKey convertToPublicKey(String encodedBase64KeyStr) {
        RSAPublicKey key = null;
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(encodedBase64KeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(Algorithms.RSA.toString());
            key = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return key;
    }

    /**
     * 将字符串格式私钥转换为RSA私钥对象
     *
     * @param encodedBase64KeyStr 字符串格式私钥
     * @return RSA私钥对象
     */
    public static RSAPrivateKey convertToPrivateKey(String encodedBase64KeyStr) {
        RSAPrivateKey key = null;
        PKCS8EncodedKeySpec priPKCS8;
        try {
            priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(encodedBase64KeyStr));
            KeyFactory keyf = KeyFactory.getInstance(Algorithms.RSA.toString());
            key = (RSAPrivateKey) keyf.generatePrivate(priPKCS8);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return key;
    }

    /**
     * 从指定文件读取字符串格式公钥并转换为公钥对象
     *
     * @param keyFileContent 完整密钥文件文本内容
     * @return 公钥对象
     */
    public static RSAPublicKey getRSAPublicKey(String keyFileContent) {
        return convertToPublicKey(keyContentProcess(keyFileContent, PUBLIC.beginStr(), PUBLIC.endStr()));
    }

    /**
     * 从指定文件读取字符串格式私钥并转换为私钥对象
     *
     * @param keyFileContent 完整密钥文件文本内容
     * @return 私钥对象
     */
    public static RSAPrivateKey getRSAPrivateKey(String keyFileContent) {
        return convertToPrivateKey(keyContentProcess(keyFileContent, PRIVATE.beginStr(), PRIVATE.endStr()));
    }

    //////////////////////////////////////////////////////////////////////////////
    //    decrypt methods
    //////////////////////////////////////////////////////////////////////////////
    public static byte[] decrypt(PrivateKey key, byte[] cipherData) {
        byte[] plainData = null;
        try {
            Cipher c = Cipher.getInstance(Algorithms.RSA.toString());
            c.init(Cipher.DECRYPT_MODE, key);
            plainData = c.doFinal(cipherData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            e.printStackTrace();
        }
        return plainData;
    }

    public static String decryptFromBase64String(PrivateKey key, String base64Ciphertext) {
        String plaintext = "";
        try {
            Cipher c = Cipher.getInstance(Algorithms.RSA.toString());
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] cipherBytes = Base64.getDecoder().decode(base64Ciphertext);
            byte[] content = c.doFinal(cipherBytes);
            plaintext = new String(content);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            e.printStackTrace();
        }

        return plaintext;
    }

    //////////////////////////////////////////////////////////////////////////////
    //    encrypt methods
    //////////////////////////////////////////////////////////////////////////////
    public static byte[] encrypt(PublicKey key, byte[] plainData) {
        byte[] cipherData = null;
        try {
            Cipher c = Cipher.getInstance(Algorithms.RSA.toString());
            c.init(Cipher.ENCRYPT_MODE, key);
            cipherData = c.doFinal(plainData);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException |
                 NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return cipherData;
    }

    public static String encryptToBase64String(PublicKey key, String plaintext) {
        return encryptToBase64String(key, plaintext, StandardCharsets.UTF_8);
    }

    public static String encryptToBase64String(PublicKey key, String plaintext, Charset charset) {
        byte[] cipherData = encrypt(key, plaintext.getBytes(charset));
        return Base64.getEncoder().encodeToString(cipherData);
    }

    public static String encryptToBase64String(String publicKeyStr, String plaintext) {
        return encryptToBase64String(convertToPublicKey(publicKeyStr), plaintext);
    }

    public static String encryptToBase64String(String publicKeyStr, String plaintext, Charset charset) {
        return encryptToBase64String(convertToPublicKey(publicKeyStr), plaintext, charset);
    }

    //////////////////////////////////////////////////////////////////////////////
    //    sign methods
    //////////////////////////////////////////////////////////////////////////////

    /**
     * @param plainText     明文
     * @param keyEncoded    私钥的编码字符串
     * @param signAlgorithm 签名算法
     * @return 返回明文的签名
     */
    private static String sign(String plainText, byte[] keyEncoded, String signAlgorithm) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(keyEncoded);
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance(signAlgorithm);
            signature.initSign(priKey);
            signature.update(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] signed = signature.sign();
            return Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(String plainText, String privateKey, String signAlgorithm) {
        byte[] keyEncoded = Base64.getDecoder().decode(privateKey);
        return sign(plainText, keyEncoded, signAlgorithm);
    }

    public static String sign(String plainText, PrivateKey privateKey, String signAlgorithm) {
        return sign(plainText, privateKey.getEncoded(), signAlgorithm);
    }

    public static String sign(String plainText, String privateKey) {
        return sign(plainText, privateKey, "MD5withRSA");
    }

    public static String sign(String plainText, PrivateKey privateKey) {
        return sign(plainText, privateKey.getEncoded(), "MD5withRSA");
    }

    /**
     * 公钥验签
     *
     * @param plainText        明文
     * @param encodedSign      使用Base64编码的签名
     * @param encodedPublicKey 使用Base64编码的公钥
     * @param signAlgorithm    签名算法(MD5withRSA/SHA1withRSA)
     * @return 验签结果
     */
    public static boolean check(String plainText, String encodedSign, String encodedPublicKey, String signAlgorithm) {
        try {

            PublicKey key = convertToPublicKey(encodedPublicKey);
            Signature signatureChecker = Signature.getInstance(signAlgorithm);
            signatureChecker.initVerify(key);
            signatureChecker.update(plainText.getBytes(StandardCharsets.UTF_8));
            return signatureChecker.verify(Base64.getDecoder().decode(encodedSign));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean check(String plainText, String encodedSign, String encodedPublicKey) {
        //摘要算法默认使用MD5
        return check(plainText, encodedSign, encodedPublicKey, "MD5withRSA");
    }


    /**
     * 公钥验签
     *
     * @param plainText     明文
     * @param encodedSign   使用Base64编码的签名
     * @param publicKey     公钥
     * @param signAlgorithm 签名算法(MD5withRSA/SHA1withRSA)
     * @return 验签结果
     */
    public static boolean check(String plainText, String encodedSign, PublicKey publicKey, String signAlgorithm) {
        try {
            Signature signatureChecker = Signature.getInstance(signAlgorithm);
            signatureChecker.initVerify(publicKey);
            signatureChecker.update(plainText.getBytes(StandardCharsets.UTF_8));
            return signatureChecker.verify(Base64.getDecoder().decode(encodedSign));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean check(String plainText, String encodedSign, PublicKey publicKey) {
        //摘要算法默认使用MD5
        return check(plainText, encodedSign, publicKey, "MD5withRSA");
    }


    //////////////////////////////////////////////////////////////////////////////
    //    private methods
    //////////////////////////////////////////////////////////////////////////////

    public static String toEncodedString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * remove start and end line of the key content
     * 移除密钥文件文本内容的开头和结尾行
     *
     * @param keyContent key content with start line and end line
     * @param beginStr   start line content
     * @param endStr     end line content
     * @return actual key String
     */
    private static String keyContentProcess(String keyContent, String beginStr, String endStr) {
        return keyContent.replace(beginStr, "").replace(endStr, "")
                .replace("\n", "").replace("\r", "");
    }

    /**
     * 生成包含头尾说明的密钥文件内容
     *
     * @param encodedBase64KeyStr 密钥字符串
     * @param keyType             密钥类型（公钥或者私钥）
     * @return 要保存到文件的密钥内容
     */
    private static String genKeyContent(String encodedBase64KeyStr, RSAKeyType keyType) {
        String beginStr = PUBLIC.equals(keyType) ? PUBLIC.beginStr() : PRIVATE.beginStr();
        StringBuilder keyStr = new StringBuilder(beginStr).append("\r\n");
        int i = 0;
        while ((i + 64) < encodedBase64KeyStr.length()) {
            keyStr.append(encodedBase64KeyStr, i, i + 64).append("\n");
            i += 64;
        }
        keyStr.append(encodedBase64KeyStr.substring(i)).append("\n")
                .append(PUBLIC.equals(keyType) ? PUBLIC.endStr() : PRIVATE.endStr());
        return keyStr.toString();
    }


    enum RSAKeyType {
        PUBLIC("-----BEGIN RSA PUBLIC KEY-----","-----END RSA PUBLIC KEY-----"),
        PRIVATE("-----BEGIN RSA PRIVATE KEY-----","-----END RSA PRIVATE KEY-----");

        RSAKeyType(String beginStr, String endStr){
            this.beginStr = beginStr;
            this.endStr = endStr;
        }

        private final String beginStr;
        private final String endStr;

        public String beginStr(){
            return beginStr;
        }

        public String endStr(){
            return endStr;
        }
    }
}
