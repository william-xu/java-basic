package com.xwl41.common.basic.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class FileUtilTest {
    @Test
    public void testGetFile(){
        String path = "D:\\data\\keys\\privateKey.pem";
        assertNull(CoreUtil.FILE.getTextFromResource(path));
        System.out.println(CoreUtil.FILE.getTextFromFile(path));
    }
}
