/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.security.MessageDigest;
import java.util.Date;


public class CreateAuthCode {

    public CreateAuthCode() {
    }

    public String GetAuthCode(String encodeStr, String agentnum_hex) {
        String result = null;
        Date current = new Date();
        int hours = current.getHours();
        int minutes = current.getMinutes();
        int ones = minutes % 16;
        String abddd = md5(encodeStr);
        char[] codechar = new char[14];
        for (int i = 0; i < 3; i++) {
            codechar[i] = abddd.charAt(hours % 16 + i);
        }

        String hourStr = Integer.toHexString(hours).toUpperCase();
        if (hourStr.length() == 1) {
            codechar[3] = Integer.toHexString(hours).toUpperCase().charAt(0);
        } else {
            codechar[3] = Integer.toHexString(hours).toUpperCase().charAt(1);
        }

        codechar[4] = '-';
        for (int i = 0; i < 2; i++) {
            codechar[5 + i] = abddd.charAt(ones + i);
        }
        String minStr = Integer.toHexString(minutes).toUpperCase();
        if (minStr.length() == 1) {
            codechar[7] = '0';
            codechar[8] = minStr.charAt(0);
        } else {
            codechar[7] = minStr.charAt(0);
            codechar[8] = minStr.charAt(1);
        }
        codechar[9] = '-';
        if (agentnum_hex.length() == 4) {
            codechar[10] = agentnum_hex.charAt(0);
            codechar[11] = agentnum_hex.charAt(1);
            codechar[12] = agentnum_hex.charAt(2);
            codechar[13] = agentnum_hex.charAt(3);
        } else if (agentnum_hex.length() == 3) {
            codechar[10] = '0';
            codechar[11] = agentnum_hex.charAt(0);
            codechar[12] = agentnum_hex.charAt(1);
            codechar[13] = agentnum_hex.charAt(2);
        } else if (agentnum_hex.length() == 2) {
            codechar[10] = '0';
            codechar[11] = '0';
            codechar[12] = agentnum_hex.charAt(0);
            codechar[13] = agentnum_hex.charAt(1);
        } else if (agentnum_hex.length() == 1) {
            codechar[10] = '0';
            codechar[11] = '0';
            codechar[12] = '0';
            codechar[13] = agentnum_hex.charAt(0);
        }
        result = new String(codechar);
        return result.toUpperCase();
    }

    private String md5(String str) {
        String md5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] barr = md.digest(str.getBytes());  //將 byte 陣列加密
            StringBuffer sb = new StringBuffer();  //將 byte 陣列轉成 16 進制
            for (int i = 0; i < barr.length; i++) {
                sb.append(byte2Hex(barr[i]));
            }
            String hex = sb.toString();
            md5 = hex.toUpperCase(); //一律轉成大寫
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return md5;
    }

    private String byte2Hex(byte b) {
        String[] h = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        int i = b;
        if (i < 0) {
            i += 256;
        }
        return h[i / 16] + h[i % 16];
    }
}
