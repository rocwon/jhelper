package cn.techarts.jhelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 1. Combine 2 arrays, list or map as one,<br>
 * 2. or combine series strings as one.
 */
public final class Concator {
	
	public static int[] concat(int[] array1, int[] array2) {
		if(array1 == null) return array2;
		if(array2 == null) return array1;
		int length1 = array1.length;
		int length2 = array2.length;
		int[] result = new int[length1 + length2];
		concatArrays(array1, length1, array2, length2, result);
		return result;
	}
	
	public static float[] concat(float[] array1, float[] array2) {
		if(array1 == null) return array2;
		if(array2 == null) return array1;
		int length1 = array1.length;
		int length2 = array2.length;
		float[] result = new float[length1 + length2];
		concatArrays(array1, length1, array2, length2, result);
		return result;
	}
	
	public static long[] concat(long[] array1, long[] array2) {
		if(array1 == null) return array2;
		if(array2 == null) return array1;
		int length1 = array1.length;
		int length2 = array2.length;
		var result = new long[length1 + length2];
		concatArrays(array1, length1, array2, length2, result);
		return result;
	}
	
	public static double[] concat(double[] array1, double[] array2) {
		if(array1 == null) return array2;
		if(array2 == null) return array1;
		int length1 = array1.length;
		int length2 = array2.length;
		var result = new double[length1 + length2];
		concatArrays(array1, length1, array2, length2, result);
		return result;
	}
	
	public static String[] concat(String[] array1, String[] array2) {
		if(array1 == null) return array2;
		if(array2 == null) return array1;
		int length1 = array1.length;
		int length2 = array2.length;
		var result = new String[length1 + length2];
		concatArrays(array1, length1, array2, length2, result);
		return result;
	}
	
	public static Object[] concat(Object[] array1, Object[] array2) {
		if(array1 == null) return array2;
		if(array2 == null) return array1;
		int length1 = array1.length;
		int length2 = array2.length;
		var result = new Object[length1 + length2];
		concatArrays(array1, length1, array2, length2, result);
		return result;
	}
	
	public static boolean[] concat(boolean[] array1, boolean[] array2) {
		if(array1 == null) return array2;
		if(array2 == null) return array1;
		int length1 = array1.length;
		int length2 = array2.length;
		var result = new boolean[length1 + length2];
		concatArrays(array1, length1, array2, length2, result);
		return result;
	}
	
	public static byte[] concat(byte[] array1, byte[] array2) {
		if(array1 == null) return array2;
		if(array2 == null) return array1;
		int length1 = array1.length;
		int length2 = array2.length;
		var result = new byte[length1 + length2];
		concatArrays(array1, length1, array2, length2, result);
		return result;
	}
	
	public static char[] concat(char[] array1, char[] array2) {
		if(array1 == null) return array2;
		if(array2 == null) return array1;
		int length1 = array1.length;
		int length2 = array2.length;
		var result = new char[length1 + length2];
		concatArrays(array1, length1, array2, length2, result);
		return result;
	}
	
	public static String concat(String... args) {
		if(args == null || args.length == 0) return "";
		var result = new StringBuilder(64);
		for(var arg : args) {
			if(arg == null) continue;
			result.append(arg);
		}
		return result.toString();
	}
	
	public static String concat(Collection<String> args) {
		if(args == null || args.isEmpty()) return "";
		var result = new StringBuilder(64);
		for(var arg : args) {
			if(arg == null) continue;
			result.append(arg);
		}
		return result.toString();
	}
	
	public static<T> List<T> concat(List<T> arg0, List<T> arg1){
		if(Empty.is(arg0)) return arg1;
		if(Empty.is(arg1)) return arg0;
		var result = new ArrayList<T>(arg0); 
		result.addAll(arg1);
		return result;
	}
	
	public static<T> Set<T> concat(Set<T> arg0, Set<T> arg1){
		if(Empty.is(arg0)) return arg1;
		if(Empty.is(arg1)) return arg0;
		var result = new HashSet<T>(arg0); 
		result.addAll(arg1);
		return result;
	}
	
	public static<K, V> Map<K, V> concat(Map<K, V> arg0, Map<K, V> arg1){
		if(Empty.is(arg0)) return arg1;
		if(Empty.is(arg1)) return arg0;
		var result = new HashMap<K, V>(arg0); 
		result.putAll(arg1);
		return result;
	}
	
	private static void concatArrays(Object array1, int length1, Object array2, int length2, Object result) {
		System.arraycopy(array1, 0, result, 0, length1);
		System.arraycopy(array2, 0, result, length1, length2);
	}
}