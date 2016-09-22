package com.fanwe.listener;

import java.math.BigDecimal;

public class TextMoney {
	/**
	 * 对金钱的字符串进行处理,保留两位和四舍五入
	 * 
	 * @param number
	 * @return 例如:123.00 -45.00 123 -45
	 */
	public static String textFarmat(float number) {
		String numberStr=String.valueOf(number);
		if (!numberStr.contains(".")) {
			return numberStr;
		}
		
		if (numberStr.endsWith(".0") ) {
			return numberStr.substring(0, numberStr.indexOf("."));
		}
		
		if (numberStr.subSequence(numberStr.indexOf("."), numberStr.length()).length()>2) {
			BigDecimal b = new BigDecimal(number); 
			float result = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
			return String.valueOf(result);
		}else {
			return numberStr+"0";
		}
	}

	/**
	 * 对金钱的字符串进行处理,保留两位和四舍五入
	 * 
	 * @param number
	 * @return 返回两位小数,整数也是两位,例如10会返回10.00
	 */
	public static String textFarmat2(float number) {
		String numberStr=String.valueOf(number);
		if (!numberStr.contains(".")) {
			return numberStr;
		}
		
		if (numberStr.endsWith(".0") ) {
			return numberStr+"0";
		}
		
		if (numberStr.subSequence(numberStr.indexOf("."), numberStr.length()).length()>2) {
			BigDecimal b = new BigDecimal(number); 
			float result = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
			return String.valueOf(result);
		}else {
			return numberStr+"0";
		}
	}

	/**
	 * 对金钱的字符串进行处理,保留两位和四舍五入
	 * <p>返回的规则1: 值为整数返回整数,例如:23.000返回23</p>
	 * <p>返回的规则2: 值为小数返回四舍五入的两位数小数,例如:23.346返回23.35</p>
	 * <p>返回的规则3: 值为0返回0.00,例如:0.0000或者0返回0.00</p>
	 * 
	 * 2016-5-18项目统一用此方案
	 * @param number
	 * @return 返回统一规范的数据
	 */
	public static String textFarmat3(float number) {
		String numberStr=String.valueOf(number);
		if (!numberStr.contains(".")) {
			return numberStr;
		}
		if (number==0) {
			return "0.00";
		}
		
		String substring = numberStr.substring(0, numberStr.indexOf('.'));
//		if (numberStr.endsWith(".0")) {
//			return numberStr+"0";
//		}
		if (number==Integer.valueOf(substring)) {
			return substring;
		}
		
		if (numberStr.subSequence(numberStr.indexOf("."), numberStr.length()).length()>2) {
			BigDecimal b = new BigDecimal(number); 
			float result = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
			return String.valueOf(result);
		}else {
			return numberStr+"0";
		}
	}
	/**
	 * 对金钱的字符串进行处理,保留两位和四舍五入
	 * 
	 * @param number
	 * @return
	 */
	public static String textFarmat(String number) {
		if (!number.contains(".")) {
			return number;
		}
		if (Float.valueOf(number).floatValue()==Integer.valueOf(number.substring(0, number.indexOf("."))).intValue()) {
			return number.substring(0, number.indexOf("."));
		}else {
			if (number.length()-number.indexOf(".")==2) {
				return number+"0";
			}
			BigDecimal b = new BigDecimal(Float.valueOf(number).floatValue()); 
			float result = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
			return String.valueOf(result);
		}
	}

	/**
	 * 输入的数字不能超过两位小数
	 * @param number的字符串
	 * @return false表示:不让用户继续输入字符,当前的字符有误
	 */
	public static boolean NumberLimited(String number) {

		if (number.startsWith(".")) {
			return false;
		}
		if (number.startsWith("0")) {
			if (number.startsWith("00")) {
				return false;
			}
			if (number.length() - number.indexOf(".") >= 4) {
				return false;
			}
			if (number.endsWith("0") || number.endsWith(".")) {
				return true;
			}
			
			return true;
		}

		if (number.contains(".")) {

			if (number.endsWith(".")) {
				return true;
			}
			if (number.length() - number.indexOf(".") >= 4) {
				return false;
			}
			return true;
		}

		return true;
	}

}
