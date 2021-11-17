package cn.techarts.jhelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Convert the specific type to another. For an incorrect input parameter,<p>
 * 1. returns 0 or 0.0 if the target type is a numeric, or<br>
 * 2. returns null if the target type is a date string. 
 */
public final class Converter {
	public static int toInt( String arg){
		if(Empty.is(arg)) return 0;
		try{ 
			return Integer.parseInt(arg);
		}catch( NumberFormatException e){ 
			return 0;
		}
	}
	
	public static int toInt( String arg, boolean abs){
		if(Empty.is(arg)) return 0;
		try{ 
			int val = Integer.parseInt( arg);
			return abs && val < 0 ? Math.abs(val) : val;
		}catch( NumberFormatException e){ 
			return 0;
		}
	}
	
	public static int toInt( String arg, int defaultValue){
		if(Empty.is(arg)) return defaultValue;
		try{ 
			return Integer.parseInt(arg);
		}catch( NumberFormatException e){ 
			return defaultValue;
		}
	}
	
	public static double toDouble( String arg){
		if(Empty.is(arg)) return 0d;
		try{ 
			return Double.parseDouble(arg);
		}catch( NumberFormatException e){ 
			return 0d;
		}
	}
	
	public static long toLong( String arg){
		if(Empty.is(arg)) return 0L;
		try{ 
			return Long.parseLong(arg);
		}catch(NumberFormatException e){ 
			return 0L;
		}
	}
	
	public static float toFloat( String arg){
		if(Empty.is(arg)) return 0f;
		try{ 
			return Float.parseFloat(arg);
		}catch( NumberFormatException e){ 
			return 0f;
		}
	}
	
	public static boolean toBoolean(int arg) {
		return arg != 0; //C-like style
	}
	
	public static boolean toBoolean(String arg) {
		if(Empty.is(arg)) return false;
		var val = arg.trim().toLowerCase();
		return val.equals("1") || val.equals("true");
	}
	
	public static String toString(int arg) {
		return String.valueOf(arg);
	}
	
	public static String toString(long arg) {
		return Long.toString(arg);
	}
	
	public static String toString(float arg) {
		return Float.toString(arg);
	}
	
	public static String toString(double arg) {
		return Double.toString(arg);
	}
	
	/**
	 * It's same to the method {@link DateHelper.format(arg, withTime)}<p>
	 * @param withTime Returns the long pattern "yyyy-MM-dd HH:mm:ss" if it's true. 
	 * Otherwise, the short pattern "yyyy-MM-dd" is returned. 
	 */
	public static String toString(Date arg, boolean withTime) {
		return Time.format(arg, withTime);
	}
	
	/**
	 *@param arg The pattern of date string is yyyy-MM-dd or yyyy-MM-dd HH:mm:ss<p>
	 *It's same to the method {@link DateHelper.parse(String arg)} 
	 */
	public static Date toDate(String arg) {
		return Time.parse(arg);
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T[] toArray(Collection<T> arg) {
		if(arg == null) return null;
		return (T[])arg.toArray(); //Force to convert the generic type
	}
	
	public static<T> List<T> toList(T[] arg){
		if(arg == null) return null;
		var result = new ArrayList<T>();
		if(arg.length == 0) return result;
		for(var item : arg) {
			if(item != null) result.add(item);
		}
		return result;
	}
	
	public static<T> Set<T> toSet(T[] arg){
		if(arg == null) return null;
		if(arg.length == 0) {
			return new HashSet<>();
		}
		return new HashSet<>(toList(arg));
	}
	
	public static<T> T to(Object arg, Class<T> clazz) {
		if(arg == null || clazz == null) return null;
		try {
			return clazz.cast(arg);
		}catch(ClassCastException e) {
			return null; //Cannot cast to the given type
		}
	}
	
	/**
	 * An alias of {@link Reflector.dump}
	 */
	public static Map<String, Object> toMap(Object arg){
		return Reflector.dump(arg);
	}
	
	/**
	 * An alias of {@link Reflector.fill}
	 */
	public static void toBean(Object bean, Map<String, Object> data) {
		Reflector.fill(bean, data);
	}
	
	//---------------------Bit Manipulation-----------------------------------
	
	public static int toInt(byte[] bytes) {
		if(bytes == null) return 0;
		if(bytes.length != 4) return 0;
		return  (bytes[3] & 0xFF) |
	            (bytes[2] & 0xFF) << 8 |
	            (bytes[1] & 0xFF) << 16 |
	            (bytes[0] & 0xFF) << 24;
	}
	
	public static int toIntLE(byte[] bytes) {
		if(bytes == null) return 0;
		if(bytes.length != 4) return 0;
		return  (bytes[0] & 0xFF)       |
	            (bytes[1] & 0xFF) << 8  |
	            (bytes[2] & 0xFF) << 16 |
	            (bytes[3] & 0xFF) << 24;
	}
	
	public static short toShort(byte[] bytes) {
		if(bytes == null) return 0;
		if(bytes.length != 2) return 0;
		var result = (bytes[1] & 0xFF) |
		         (bytes[0] & 0xFF) << 8;		        
		return (short)result;
	}
	
	public static short toShortLE(byte[] bytes) {
		if(bytes == null) return 0;
		if(bytes.length != 2) return 0;
		var result = (bytes[0] & 0xFF) |
		         (bytes[1] & 0xFF) << 8;		        
		return (short)result;
	}
	
	/**
	 * Little Endain Order (From right to left). If you want a big endain order, please reverse the result array.<p>
	 * For example:<p>
	 * 0x4b -> 01001011 ->{true, true, false, true, false, false, true, false} 
	 */
	public static boolean[] toBooleans(byte arg) {
		var result = new boolean[8];
		for(int i = 0; i < 8; i++) {
			var b = (arg >> i) & 0x01;
			result[i] = b == 1;
		}
		return result;	
	}
	
	public static byte[] toBytes(long val) {
		var result = new byte[8];
		result[0] = (byte)(val >> 56);
		result[1] = (byte)(val >> 48);
		result[2] = (byte)(val >> 40);
		result[3] = (byte)(val >> 32);
		result[4] = (byte)(val >> 24);
		result[5] = (byte)(val >> 16);
		result[6] = (byte)(val >> 8);
		result[7] = (byte)val;
		return result;
	}
	
	public static byte[] toBytesLE(long val) {
		var result = new byte[8];
		result[0] = (byte)val;
		result[1] = (byte)(val >> 8);
		result[2] = (byte)(val >> 16);
		result[3] = (byte)(val >> 24);
		result[4] = (byte)(val >> 32);
		result[5] = (byte)(val >> 40);
		result[6] = (byte)(val >> 48);
		result[7] = (byte)(val >> 56);
		return result;
	}
	
	public static byte[] toBytes(int val) {
		var result = new byte[4];
		result[0] = (byte)(val >> 24);
		result[1] = (byte)(val >> 16);
		result[2] = (byte)(val >> 8);
		result[3] = (byte)val;
		return result;
	}
	
	public static byte[] toBytesLE(int val) {
		var result = new byte[4];
		result[0] = (byte)val;
		result[1] = (byte)(val >> 8);
		result[2] = (byte)(val >> 16);
		result[3] = (byte)(val >> 24);
		return result;
	}
	
	public static byte[] toBytes(short val) {
		var result = new byte[2];
		result[0] = (byte)(val >> 8);
		result[1] = (byte)val;
		return result;
	}
	
	public static byte[] toBytesLE(short val) {
		var result = new byte[4];
		result[0] = (byte)val;
		result[1] = (byte)(val >> 8);
		return result;
	}
}