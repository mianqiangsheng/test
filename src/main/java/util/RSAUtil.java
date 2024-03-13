package util;

import org.apache.commons.lang.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：li zhen
 * @description:
 * @date ：2023/5/24 15:50
 */
public class RSAUtil {

    public static final Provider provider = new BouncyCastleProvider();

    private static final String PUBLIC_KEY = "RSAPublicKey";

    private static final String PRIVATE_KEY = "RSAPrivateKey";

    private static final String charSet = "UTF-8";

    public static final String KEY_ALGORITHM = "RSA";

    // 种子,改变后,生成的密钥对会发生变化
    //private static final String seedKey = "seedKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 生成密钥对(公钥和私钥)
     * @return
     * @throws Exception
     */
    public static synchronized Map<String, Object> generateKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
        kpg.initialize(1024, new SecureRandom());//seedKey.getBytes()
        KeyPair keyPair = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static PublicKey getPublicRSAKey(String modulus, String exponent)
            throws Exception {
        RSAPublicKeySpec spec = new RSAPublicKeySpec(
                new BigInteger(modulus, 16), new BigInteger(exponent, 16));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, provider);
        return kf.generatePublic(spec);
    }

    /**
     * 获取公钥
     * @param key base64加密后的公钥
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicRSAKey(String key) throws Exception {
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decryptBase64(key));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, provider);
        return kf.generatePublic(x509);
    }

    /**
     * 获取私钥
     * @param key base64加密后的私钥
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateRSAKey(String key) throws Exception {
        PKCS8EncodedKeySpec pkgs8 = new PKCS8EncodedKeySpec(decryptBase64(key));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, provider);
        return kf.generatePrivate(pkgs8);
    }

    /**
     * 加密
     * @param input 明文
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String input, PublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", provider);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] re = cipher.doFinal(input.getBytes(charSet));
        return re;
    }

    /**
     * 解密
     * @param encrypted
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] encrypted, PrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", provider);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] re = cipher.doFinal(encrypted);
        return re;
    }
    /**
     * base64加密
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBase64(String key) throws Exception {
//        return (new BASE64Decoder()).decodeBuffer(key);
        return Base64.getMimeDecoder().decode(key);
    }

    /**
     * base64解密
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBase64(byte[] key) throws Exception {
//        return (new BASE64Encoder()).encodeBuffer(key);
        return Base64.getMimeEncoder().encodeToString(key);
    }

    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBase64(key.getEncoded());
    }

    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBase64(key.getEncoded());
    }

    /**
     * 分段解密
     * @param jsonEncryptStr 密文 格式 base64(rsa(明文)),base64(rsa(明文)),base64(rsa(明文))
     * @param privateKey base64加密后的秘钥
     * @return
     * @throws Exception
     */
    public static String segmentdecrypt(String jsonEncryptStr, String privateKey) throws Exception {
        String jsonStr = "";
        String[] str = jsonEncryptStr.split(",");
        if(str !=null && str.length > 0) {
            int inputLen = str.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - 1 >= 0) {
                byte[] bt = RSAUtil.decryptBase64(str[i]);
                cache = RSAUtil.decrypt(bt, RSAUtil.getPrivateRSAKey(privateKey));
                out.write(cache, 0, cache.length);
                i++;
                inputLen--;
            }


            byte[] decryptedData = out.toByteArray();
            out.close();
            jsonStr = new String(decryptedData);
        }
        return jsonStr;
    }

    // 获取公钥
    public static PublicKey getPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyByte = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 公钥加密
     *
     * @param text         待加密的明文字符串
     * @param publicKeyStr 公钥
     * @return 加密后的密文
     */
    public static String encrypt(String text, String publicKeyStr) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyStr));

            // URLEncoder编码解决中文乱码问题
            byte[] data = text.getBytes(StandardCharsets.UTF_8);
            // 加密时超过117字节就报错。为此采用分段加密的办法来加密
            byte[] enBytes = null;
            for (int i = 0; i < data.length; i += MAX_ENCRYPT_BLOCK) {
                // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_ENCRYPT_BLOCK));
                enBytes = ArrayUtils.addAll(enBytes, doFinal);
            }
            return Base64.getEncoder().encodeToString(enBytes);
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + text + "]时遇到异常", e);
        }
    }
}
