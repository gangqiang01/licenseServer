package com.util;

import java.util.HashMap;

public class ThirtySixToTenUtil {
    // 定义36进制数字
    private static final String X36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // 拿到36进制转换10进制的值键对
    private static HashMap<Character, Integer> thirysixToTen = createMapThirtysixToTen();
    // 拿到10进制转换36进制的值键对
    private static HashMap<Integer, Character> tenToThirtysix = createMapTenToThirtysix();
    // 定义静态进制数
    private static int BASE = 36;

    private static HashMap<Character, Integer> createMapThirtysixToTen() {
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < X36.length(); i++) {
            // 0--0,... ..., Z -- 35的对应存放进去
            map.put(X36.charAt(i), i);
        }
        return map;
    }

    private static HashMap<Integer, Character> createMapTenToThirtysix() {
        HashMap<Integer, Character> map = new HashMap<Integer, Character>();
        for (int i = 0; i < X36.length(); i++) {
            // 0--0,... ..., 35 -- Z的对应存放进去
            map.put(i, X36.charAt(i));
        }
        return map;
    }

    public static int ThirtysixToDeciaml(String pStr) {
        if (pStr == "")
            return 0;
        // 目标十进制数初始化为0
        int deciaml = 0;
        // 记录次方,初始为36进制长度 -1
        int power = pStr.length() - 1;
        // 将36进制字符串转换成char[]
        char[] keys = pStr.toCharArray();
        for (int i = 0; i < pStr.length(); i++) {
            // 拿到36进制对应的10进制数
            int value = thirysixToTen.get(keys[i]);
            deciaml = (int) (deciaml + value * Math.pow(BASE, power));
            // 执行完毕 次方自减
            power--;
        }
        return deciaml;
    }

    public static int DeciamlToThirtySix(int iSrc, String pRet) {
        String str = DeciamlToThirtySix(iSrc);
        if (str.equals(pRet)) {
            return 0;
        } else {
            return 1;
        }
    }

    public static String DeciamlToThirtySix(int iSrc) {
        String result = "";
        int key;
        int value;

        key = iSrc / BASE;
        value = iSrc - key * BASE;
        if (key != 0) {
            result = result + DeciamlToThirtySix(key);
        }

        result = result + tenToThirtysix.get(value).toString();
        return result;
    }

}
