package com.plumdo.common.utils;
import java.math.BigDecimal;  
import java.text.DecimalFormat;  
  
/** 
 * 金额工具类,主要是金额的格式化,金额的加、减  
 * @author Tiny 
 * 
 */  
public class MoneyUtils {  
  
    public static DecimalFormat fnum = new DecimalFormat("##0.00000000000000000000");  
      
    /** 
     * 格式化金额 
     * @param value 
     * @return 
     */  
    public static String formatMoney(String value){  
        if(value == null || value == "" ){  
            value = "0.00";  
        }  
        return fnum.format(new BigDecimal(value));  
    }  
      
    public static String add(String valueStr,String addStr){  
        BigDecimal value = new BigDecimal(valueStr);  
        BigDecimal augend = new BigDecimal(addStr);  
        return fnum.format(value.add(augend));  
    } 
    
    public static Double add(Double valueStr,Double addStr){  
        BigDecimal value = new BigDecimal(valueStr);  
        BigDecimal augend = new BigDecimal(addStr);  
        return value.add(augend).doubleValue();  
    }  
      
    public static BigDecimal add(BigDecimal value,BigDecimal augend){  
        return value.add(augend);  
    }  
      
    public static String subtract(String valueStr,String subtrStr){  
        BigDecimal value= new BigDecimal(valueStr);  
        BigDecimal subtrahend = new BigDecimal(subtrStr);  
        return fnum.format(value.subtract(subtrahend));  
    }  
      
    public static Double subtract(Double valueStr,Double subtrStr){  
        BigDecimal value = new BigDecimal(valueStr);  
        BigDecimal subtrahend = new BigDecimal(subtrStr);  
        return value.subtract(subtrahend).doubleValue();  
    }
    
    public static BigDecimal subtract(BigDecimal value,BigDecimal subtrahend){  
        return value.subtract(subtrahend);  
    }  
      
    public static String multiply(String valueStr,String mulStr){  
        BigDecimal value = new BigDecimal(valueStr);  
        BigDecimal mulValue = new BigDecimal(mulStr);  
        return fnum.format(value.multiply(mulValue));  
    }  
      
    public static Double multiply(Double valueStr,Double mulStr){  
        BigDecimal value = new BigDecimal(valueStr);  
        BigDecimal mulValue = new BigDecimal(mulStr);  
        return value.multiply(mulValue).doubleValue();  
    }
    
    public static BigDecimal multiply(BigDecimal value,BigDecimal mulValue){  
        return value.multiply(mulValue);  
    }  
      
   
    public static String divide(String valueStr,String divideStr){  
        BigDecimal value = new BigDecimal(valueStr);  
        BigDecimal divideValue = new BigDecimal(divideStr);  
        return fnum.format(value.divide(divideValue, 2, BigDecimal.ROUND_HALF_UP));  
    }  
      
    public static Double divide(Double valueStr,Double divideStr){  
        BigDecimal value = new BigDecimal(valueStr);  
        BigDecimal divideValue = new BigDecimal(divideStr);  
        return value.divide(divideValue, 2, BigDecimal.ROUND_HALF_UP).doubleValue();  
    }
    
    public static BigDecimal divide(BigDecimal value,BigDecimal divideValue){  
        return value.divide(divideValue, 2, BigDecimal.ROUND_HALF_UP);  
    }  
      
    public static boolean compare(String valueStr,String compValueStr){  
        BigDecimal value = new BigDecimal(valueStr);  
        BigDecimal compValue = new BigDecimal(compValueStr);  
        //0:等于    >0:大于    <0:小于  
        int result = value.compareTo(compValue);  
        if(result >= 0){  
            return true;  
        }else{  
            return false;  
        }  
    }  
      
    public static boolean compare(Double valueStr,Double compValueStr){  
        BigDecimal value = new BigDecimal(valueStr);  
        BigDecimal compValue = new BigDecimal(compValueStr);  
        //0:等于    >0:大于    <0:小于  
        int result = value.compareTo(compValue);  
        if(result >= 0){  
            return true;  
        }else{  
            return false;  
        }  
    }  
    
    public static boolean compare(BigDecimal valueStr,BigDecimal compValueStr){  
        //0:等于    >0:大于    <0:小于  
        int result = valueStr.compareTo(compValueStr);  
        if(result >= 0){  
            return true;  
        }else{  
            return false;  
        }  
    }  
      
    /** 
     * 金额乘以，省去小数点 
     * @param valueStr 基础值 
     * @return 
     */  
    public static String moneyMulOfNotPoint (String valueStr, String divideStr){  
        BigDecimal value = new BigDecimal(valueStr);  
        BigDecimal mulValue = new BigDecimal(divideStr);  
        valueStr = fnum.format(value.multiply(mulValue));  
        return fnum.format(value.multiply(mulValue)).substring(0, valueStr.length()-3);  
    }  
   
    
    public static Integer getWeixinMoney(BigDecimal decimal) {
		if (decimal != null) {
			int value = decimal.multiply(BigDecimal.valueOf(100)).intValue();
			return value;
		}
		return null;
	}
}  