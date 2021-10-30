package cn.techarts.jhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Split the given char sequence to parts according to the specific separator
 */
public final class Spliter {
	public static List<String> split(String src, char separator){
		var result = new ArrayList<String>(256);
		if(Empty.is(src)) return result; 
		char[] chars = src.toCharArray();
		int length = chars.length, prev = 0;
		for( int i = 0; i < length; i++){
			if( chars[i] != separator)continue;
			result.add(src.substring( prev, i));
			prev = i + 1;
		}
		if( chars[length - 1] != separator){//result.add("");
			result.add(src.substring( prev, src.length()));
		}
		return result;
	}
	
	public static List<Integer> split(String src, char separator, boolean abs){
		if(Empty.is(src)) return new ArrayList<>();
		var result = new ArrayList<Integer>(16);
		char[] chars = src.toCharArray();
		int length = chars.length, prev = 0;
		for( int i = 0; i < length; i++){
			if( chars[i] != separator)continue;
			result.add(Converter.toInt(src.substring(prev, i), abs));
			prev = i + 1;
		}
		if( chars[length - 1] != separator){ 
			String last = src.substring(prev, src.length());
			result.add(Converter.toInt(last, abs));
		}
		return result;
	}
	
	/**
	 * Split the given string to 2 parts.
	 * For example: "age:28" -->["age", "28"]
	 */
	public static String[] split2Parts( String src, char separator){
		if(Empty.is(src)) return null; 
		if(src.indexOf(separator) < 0) return null;
		char[] chars = src.toCharArray();
		int length = chars.length, idx = 0;
		String[] result = new String[2];
		for( int i = 0; i < length; i++){
			if( chars[i] != separator)continue;
			idx = i;
			result[0] = src.substring(0, i);
		}
		result[1] = src.substring(idx + 1);
		return result;
	}
}