/*
 * @(#)MD5Util.java 2013-12-4上午11:10:40
 * Copyright 2012 juncsoft, Inc. All rights reserved.
 */
package com.miguo.live.views.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 采用MD5加密解密
 * 
 * @author vincent
 */
public class MD5Util {

	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }
	
	private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }
	
	public static String string2MD5(String str) {
        String result = null;
        try {
        	result = new String(str);
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byteToString(md.digest(str.getBytes("UTF-8")));
            result = result.toLowerCase();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return result;
    }
	
	public static String string2MD5(String str,boolean lowerCase) {
        String result = null;
        try {
        	result = new String(str);
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byteToString(md.digest(str.getBytes("UTF-8")));
            if(lowerCase){
            	result = result.toUpperCase();	
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return result;
    }
	
}
