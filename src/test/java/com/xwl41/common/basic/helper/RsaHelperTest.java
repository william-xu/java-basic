package com.xwl41.common.basic.helper;

import com.xwl41.common.basic.util.CoreUtil;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import static com.xwl41.common.basic.helper.RSAHelper.RSAKeyType.PRIVATE;
import static com.xwl41.common.basic.helper.RSAHelper.RSAKeyType.PUBLIC;

public class RsaHelperTest {

    @Test
    public void testGenKeyPair() throws NoSuchAlgorithmException {
        Map<String, String> keyPair = RSAHelper.genRSAKeyStringPair(1024);
        System.out.println(keyPair.get(PUBLIC.toString()));
        System.out.println(keyPair.get(PRIVATE.toString()));
    }

    @Test
    public void testReadFile(){
        String path = "D:\\data\\keys\\privateKey.pem";
        String keyContent = CoreUtil.FILE.getTextFromFile(path);
        if(CoreUtil.isNotEmpty(keyContent)){
            System.out.println(RSAHelper.getRSAPrivateKey(keyContent));
        }
    }

}
