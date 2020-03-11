package com.service;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Created by 74117 on 8/3/2020.
 */

/**
 * 文件AES加密服务
 */
public class AES {
    private static Logger logger = Logger.getLogger(AES.class);
    /**
     * 获得密钥
     * @param strKey
     * @return
     */
    public static Key getKey(String strKey) {
        logger.debug("开始获取AES密钥");

        try {
            KeyGenerator _generator = KeyGenerator.getInstance("AES");
            //_generator.init(new SecureRandom(strKey.getBytes()));这种方式，在windows可以，可是在linux每次生成的key不一样
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            _generator.init(secureRandom);
            Key key = (Key) _generator.generateKey();
            _generator = null;
            //System.out.println(key);
            return key;
        } catch (Exception e) {
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);

        }
    }

    /**
     * 加密文件，file是待加密文件，destFile是加密后的结果，strKey是密钥
     * @param file
     * @param destFile
     * @param strKey
     * @throws Exception
     */
    public static void fileEncrypt(File file, File destFile, String strKey) throws Exception {
        logger.debug("开始文件加密");
        //ZipUtils.compress();
        Cipher cipher = Cipher.getInstance("AES");
        // cipher.init(Cipher.ENCRYPT_MODE, getKey());
        cipher.init(Cipher.ENCRYPT_MODE, (java.security.Key) getKey(strKey));
        InputStream is = new FileInputStream(file);
        OutputStream out = new FileOutputStream(destFile);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = cis.read(buffer)) > 0) {
            out.write(buffer, 0, r);
        }
        cis.close();
        is.close();
        out.close();
    }

    /**
     * 解密文件，file是待解密文件，destFile是解密后的结果，strKey是密钥
     * @param file
     * @param dest
     * @param strKey
     * @throws Exception
     */
    public static void fileDecrypt(File file, File dest, String strKey) throws Exception {
        logger.debug("开始文件解密");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, getKey(strKey));
        InputStream is = new FileInputStream(file);
        OutputStream out = new FileOutputStream(dest);
        CipherOutputStream cos = new CipherOutputStream(out, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = is.read(buffer)) >= 0) {
            cos.write(buffer, 0, r);
        }
        cos.close();
        out.close();
        is.close();
    }
}
