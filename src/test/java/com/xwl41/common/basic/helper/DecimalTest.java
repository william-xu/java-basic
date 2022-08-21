package com.xwl41.common.basic.helper;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DecimalTest {

    @Test
    public void testNew(){
        Decimal decimal1 = new Decimal("0");
        Decimal decimal2 = new Decimal(0);
        Decimal decimal3 = new Decimal(0L);
        assertEquals(decimal1, decimal2);
        assertEquals(decimal1.get(),decimal2.get());
        assertEquals(decimal2, decimal3);
        assertEquals(decimal2.get(),decimal3.get());
        //浮点数会有小数位
        Decimal decimal4 = new Decimal(0f);
        Decimal decimal5 = new Decimal(0d);
        assertEquals(decimal4, decimal5);
        assertEquals(decimal4.get(),decimal5.get());
        //与整数不同
        assertNotEquals(decimal1, decimal4);
        assertNotEquals(decimal1.get(),decimal4.get());
        //如果浮点数设置小数位=0，则与整数相同
        decimal4.setScale(0);
        assertEquals(decimal1, decimal4);
        assertEquals(decimal1.get(),decimal4.get());
        //如果整数设置相同的小数位，则与对应的小数相同
        decimal1.setScale(1);
        assertEquals(decimal1, decimal5);
        assertEquals(decimal1.get(),decimal5.get());

        assertNotEquals(decimal1, decimal4);
        assertNotEquals(decimal1.get(),decimal4.get());
    }


    @Test
    public void testDecimal(){
        Decimal d1 = new Decimal(9);
        Decimal d2 = new Decimal(9);
        //Decimal构造器将传入值转换为string处理，没有使用预设值，因此不是同个对象
        assertNotSame(d1.get(), d2.get());
        //(9*9+9)/9.0000 = 10
        Decimal d3 = d1.multiply(d2).add(d2).divide(new Decimal(9),4);
        //与有相同小数位同值的对象比较-->相等
        assertEquals(new Decimal(10,4), d3);
        //无小数位同值的对象比较-->不等
        assertNotEquals(new Decimal(10), d3);
        //仅进行数值比较 --> 相等
        assertEquals(10, d3.doubleValue());

        Decimal sd = new Decimal(9,3);
        //直接操作，与BigDecimal不同，不创建新的Decimal对象：  (9+99)*100/33 = 327.273
        sd.add(new BigDecimal("99")).multiply(new BigDecimal("100")).divide(new BigDecimal(33));
        System.out.println(sd);
        Decimal sd2 = new Decimal(9,3);
        //直接操作数值： (9+99)*100/33
        sd2.add(99).multiply(100).divide(33);
        //直接操作数值，与操作BigDecimal结果一致
        assertEquals(sd.get(), sd2.get());
    }


}
