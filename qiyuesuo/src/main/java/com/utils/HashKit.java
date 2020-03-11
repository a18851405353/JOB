package com.utils;

import com.sun.xml.internal.ws.util.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import sun.security.pkcs11.wrapper.Constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import static org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers.bagtypes;
import static org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers.data;

/**
 * Created by 74117 on 9/3/2020.
 */

/**
 * 数字签名验证，content是原文，sign是签名，publicKey是公钥，charset是字符格式
 */
public class HashKit {
    private static Logger logger = Logger.getLogger(HashKit.class);
    public static boolean rsaCheckContent(String content, String sign, String publicKey, String charset) throws Exception {

        try {
            byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(publicKey);
            PublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //SHA1WithRSA是散列算法
            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");//"SHA1WithRSA"
            signature.initVerify(pubKey);
            if (charset.equals("")) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            byte[] d = Base64.decode(sign.getBytes());
            boolean yn = signature.verify(d);
            if(yn)
            {
                logger.debug("数字签名验证成功");
            }
            return yn;
        } catch (Exception e) {
            return false;
        }

    }


}

