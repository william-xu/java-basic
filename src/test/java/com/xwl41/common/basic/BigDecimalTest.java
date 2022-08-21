package com.xwl41.common.basic;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.xwl41.common.basic.constant.CoreConstants.NUMERIC.ZERO;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class BigDecimalTest {

    @Test
    public void testRoundingMode(){
        BigDecimal b = new BigDecimal("99.99");
        b = b.setScale(3, RoundingMode.FLOOR);
        assertEquals(1,b.divide(new BigDecimal("100")).setScale(3, RoundingMode.HALF_UP).doubleValue());
    }


    @Test
    public void testBigDecimal() {
        //参数为整数，默认小数点位数为0
        BigDecimal bd0 = BigDecimal.valueOf(0);
        assertEquals(ZERO, bd0.scale());
        BigDecimal bd1 = BigDecimal.valueOf(1234);
        assertEquals(ZERO, bd1.scale());
        //10以内的整数都是相同的
        BigDecimal bd2 = BigDecimal.valueOf(9);
        BigDecimal bd3 = BigDecimal.valueOf(9);
        assertSame(bd2, bd3);
        //超过10不同
        BigDecimal bd4 = BigDecimal.valueOf(99);
        BigDecimal bd5 = BigDecimal.valueOf(99);
        assertNotSame(bd4, bd5);
        //10相同
        BigDecimal bd6 = BigDecimal.valueOf(10);
        BigDecimal bd7 = BigDecimal.valueOf(10);
        assertSame(bd6, bd7);
        //超过10不同
        BigDecimal bd8 = BigDecimal.valueOf(11);
        BigDecimal bd9 = BigDecimal.valueOf(11);
        assertNotSame(bd8, bd9);
        //除不尽时需要设置精度，否则会抛出java.lang.ArithmeticException异常
        assertThrowsExactly(ArithmeticException.class, () -> BigDecimal.valueOf(11).divide(BigDecimal.valueOf(3)));
        //字符串值必须可以转换为数值
        assertThrowsExactly(NumberFormatException.class, () -> new BigDecimal("SLKDF9324-342ldf"));
    }
}
