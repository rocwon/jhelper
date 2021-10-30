package cn.techarts.jhelper;

import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.Comparator;

public final class Finder {
	
	public static int find(int[] arg, int target) {
		if(Empty.is(arg)) return -1;
		return Arrays.binarySearch(arg, target);
	}
	
	public static int find(float[] arg, float target) {
		if(Empty.is(arg)) return -1;
		return Arrays.binarySearch(arg, target);
	}
	
	public static int find(long[] arg, long target) {
		if(Empty.is(arg)) return -1;
		return Arrays.binarySearch(arg, target);
	}
	
	public static int find(double[] arg, double target) {
		if(Empty.is(arg)) return -1;
		return Arrays.binarySearch(arg, target);
	}
	
	public static int find(byte[] arg, byte target) {
		if(Empty.is(arg)) return -1;
		return Arrays.binarySearch(arg, target);
	}
	
	public static int find(char[] arg, char target) {
		if(Empty.is(arg)) return -1;
		return Arrays.binarySearch(arg, target);
	}
	
	public static int find(short[] arg, short target) {
		if(Empty.is(arg)) return -1;
		return Arrays.binarySearch(arg, target);
	}
	
	public static int find(String[] arg, String target) {
		if(Empty.is(arg)) return -1;
		return Arrays.binarySearch(arg, target);
	}
	
	public static int find(String arg, String target) {
		if(Empty.is(arg) || Empty.is(target)) return -1;
		return arg.indexOf(target); //The start index
	}
	
	public static<T> int find(T[] arg, T target) {
		if(Empty.is(arg) || target == null) return -1;
		return Arrays.binarySearch(arg, target, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				if(o1 == null || o2 == null) return -1;
				int hs1 = o1.hashCode(), hs2 = o2.hashCode();
				return hs1 == hs2 ? 0 : (hs1 < hs2 ? -1 : 1);
			}
		});
	}
	
	public static<T> int find(List<T> arg, T target) {
		if(Empty.is(arg) || target == null) return -1;
		return arg.indexOf(target);
	}
	
	/**
	 * It's same to the method {@link Set.contains(Object o)}
	 */
	public static<T> boolean find(Set<T> arg, T target) {
		if(Empty.is(arg) || target == null) return false;
		return arg.contains(target); //
	}
}