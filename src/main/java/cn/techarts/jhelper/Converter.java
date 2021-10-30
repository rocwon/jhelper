package cn.techarts.jhelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Convert the specific type to another. For an incorrect input parameter,<p>
 * 1. returns 0 or 0.0 if the target type is a numeric, or<br>
 * 2. returns null if the target type is a date. 
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
		return arg.trim().equalsIgnoreCase("true");
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
}