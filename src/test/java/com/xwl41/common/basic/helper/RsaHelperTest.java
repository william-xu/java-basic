package com.xwl41.common.basic.helper;

import com.xwl41.common.basic.util.CoreUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static com.xwl41.common.basic.helper.RSAHelper.RSAKeyType.PRIVATE;
import static com.xwl41.common.basic.helper.RSAHelper.RSAKeyType.PUBLIC;

public class RsaHelperTest {

    @Test
    public void testSaveKeyPair() throws NoSuchAlgorithmException {
        String pathToSave = "D:\\pathToSaveKeys";
        File f = new File(pathToSave);
        if(f.exists() && f.isDirectory()){
            Map<String, Key> keyPair = RSAHelper.genRSAKeyPair(3072);
            RSAHelper.saveKeyPair(keyPair,pathToSave);
        }else{
            System.out.println("dir '" + pathToSave + "' not exists.");
        }
    }

    @Test
    public void testGenKeyPair() throws NoSuchAlgorithmException {
        Map<String, String> keyPair = RSAHelper.genRSAKeyStringPair(3072);
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
