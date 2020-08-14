package com.atguigu.gmall.ums;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * @author Lee_engineer
 * @create 2020-08-06 16:57
 */
public class HashMapTest {


    public static void main(String[] args) {
        WeakHashMap<Object, String> map = new WeakHashMap<>();
        Object key = new Object();
        System.out.println(key);
        String value = "WeakHashMap";
        map.put(key, value);
        System.out.println(map);

//        key = null;
        System.gc();
        System.out.println(map);
    }

}
