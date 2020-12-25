package com.zh.test.test;

import android.content.IntentFilter;
import android.util.TimeUtils;

import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Administrator
 * @Time 2020/11/30 003019:41
 * @Email: zhaohang@zhizhangyi.com
 * @Describe:
 */
public class Test {
    public static void main(String[] args) {
        long days=2;
        days*=24*60*60*1000;
        System.out.println(TimeUnit.MILLISECONDS.toHours(days));
        long mill=0;
        if (mill>0){
            System.out.println("xxx");
        }
        long millSecond=  TimeUnit.DAYS.toMillis(0);
        System.out.println(millSecond);

        long mills= TimeUnit.MINUTES.toMillis(1);
        System.out.println(mills);
        test();

        System.out.println(0x00000020);

        System.out.println("xxx"+1024*1024*1024*128L);
        testBigDecimal(1024*1024*1024);
        testBigDecimal(1024*1024*2);
        testBigDecimal(1024*1024*200);
        testBigDecimal(1024*1024*1024*128L);
    }

    public static void test(){
        String s="";
        String[] strings=s.split(" ");
        System.out.println(strings.length);
    }

    public static void testBigDecimal(long curAvailableStorage){
        double curAvailableStorageDouble = (double) curAvailableStorage / 1024 / 1024 / 1024;
        System.out.println("curAvailableStorage-old:"+curAvailableStorageDouble);
        BigDecimal bigDecimal = new BigDecimal(curAvailableStorageDouble).setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入、保留两位小数，并且不使用科学技术法
        curAvailableStorageDouble = bigDecimal.doubleValue();//当前可用存储空间(单位：GB)
        System.out.println("curAvailableStorage:"+curAvailableStorageDouble);
    }


}
