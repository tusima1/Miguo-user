package com.fanwe.utils;

import java.text.NumberFormat;

/**
 * @author didikee
 * @date 2016年6月17日 上午10:25:49
 * 
 */

public class MoneyFormat {
	
	/**
	 * 
	 * @param str 例如:012345.789--> ￥12345.79
	 * @return ￥12345.79
	 */
	public static String format(String numStr) {

        NumberFormat n = NumberFormat.getCurrencyInstance();  
        double d;  
        String outStr = "0.00";  
        try {  
             d = Double.parseDouble(numStr);  
             outStr = n.format(d);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return outStr;  
	}
	
	public static String format(Float number) {
		String num = String.valueOf(number);
        return format(num);  
	}
	
}
