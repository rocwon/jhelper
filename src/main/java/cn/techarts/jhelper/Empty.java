package cn.techarts.jhelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 1. Detect the specific parameter is null or the length/size is 0<p>
 * 2. Construct an empty and mutable/immutable collection or map.
 */
public final class Empty {
	
	public static boolean is(Object arg) {
		return arg == null;
	}
	
	public static boolean is(String arg) {
		return arg == null || "".equals(arg.trim());
	}
	
	public static boolean is(Object[] objs) {
		if(objs == null) return true;
		return objs.length == 0;
	}
	
	public static boolean is(int[] args) {
		if(args == null) return true;
		return args.length == 0;
	}
	
	public static boolean is(long[] args) {
		if(args == null) return true;
		return args.length == 0;
	}
	
	public static boolean is(float[] args) {
		if(args == null) return true;
		return args.length == 0;
	}
	
	public static boolean is(double[] args) {
		if(args == null) return true;
		return args.length == 0;
	}
	
	public static boolean is(boolean[] args) {
		if(args == null) return true;
		return args.length == 0;
	}
	
	public static boolean is(byte[] args) {
		if(args == null) return true;
		return args.length == 0;
	}
	
	public static boolean is(char[] args) {
		if(args == null) return true;
		return args.length == 0;
	}
	
	public static boolean is(short[] args) {
		if(args == null) return true;
		return args.length == 0;
	}
	
	public static boolean is(Collection<?> arg) {
		return (arg == null || arg.isEmpty());
	}
	
	public static boolean is(Map<?, ?> arg) {
		return (arg == null || arg.isEmpty());
	}
	
	public static<T> List<T> list(){
		return new ArrayList<T>();
	}
	
	public static<T> Set<T> set(){
		return new HashSet<T>();
	}
	
	public static<K, V> Map<K, V> map(){
		return new HashMap<K, V>();
	}
	
	public static<T> List<T> immutableList(){
		return List.of();
	}
	
	public static<T> Set<T> immutableSet(){
		return Set.of();
	}
	
	public static<K, V> Map<K, V> immutableMap(){
		return Map.of();
	}
}