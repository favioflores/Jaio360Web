/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;

public class EncryptDecrypt {
    
    private final String UNICODE_FORMAT = "UTF8";
    private final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    static SecretKeyFactory skf;
    static Cipher cipher;
    static byte[] arrayBytes;
    static String myEncryptionKey;
    static String myEncryptionScheme;
    static SecretKey key;
    
    private static final Logger log = Logger.getLogger(EncryptDecrypt.class);

    public EncryptDecrypt() throws Exception {
        
        myEncryptionKey = DigestUtils.getSha256Digest().toString();
        
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        
        ks = new DESedeKeySpec(arrayBytes);
        
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        
        cipher = Cipher.getInstance(myEncryptionScheme);
        
        key = skf.generateSecret(ks);
        
    }


    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        } catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            log.error(e);
        }
        return encryptedString;
    }


    public String decrypt(String encryptedString) {

        String decryptedText=null;
        
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString.getBytes());
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error(e);
        }
        
        return decryptedText;
    }

}