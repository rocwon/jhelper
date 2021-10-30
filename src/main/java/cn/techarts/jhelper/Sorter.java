package cn.techarts.jhelper;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class Sorter {
	
	private static int PARALLEL_LENGTH = 100000;
	
	/**
	 * If the length is less than 10000, the parallel sorting is disabled. 
	 */
	public static void setMinParallelLength(int length) {
		if(length > 10000) PARALLEL_LENGTH = length;
	}
	
	public static<T extends Comparable<T>> void sort(List<T> source){
		sort(source, false);
	}
	
	public static<T extends Comparable<T>> void sort(List<T> source, final boolean desc){
		if(Empty.is(source)) return;
		Collections.sort(source, new Comparator<T>(){
			@Override
			public int compare(T o1, T o2) {
				if(o1 == null || o2 == null) return 0;
				return o1.compareTo(o2) * (desc ? -1 : 1);
			}
		});
	}
	
	/**
	 * The parallel version for very large elements
	 */
	public static<T extends Comparable<T>> List<T> parallelSort(List<T> source) {
		return parallelSort(source, false);
	}
	
	/**
	 * The parallel version for very large elements and sorted by DESC
	 */
	public static<T extends Comparable<T>> List<T> parallelSort(List<T> source, final boolean desc) {
		if(Empty.is(source)) return null;
		return source.parallelStream().sorted(new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				if(o1 == null || o2 == null) return 0;
				return o1.compareTo(o2) * (desc ? -1 : 1);
			}
		}).collect(Collectors.toList());
	}	
	
	public static void sort(int[] source) {
		sort(source, false);		
	}
		
	public static void sort(int[] source, boolean desc) {
		if(Empty.is(source)) return;
		if(source.length < PARALLEL_LENGTH) {
			Arrays.sort(source); //Sorted by ASC
		}else {
			Arrays.parallelSort(source);
		}		
		if(desc) reverseArray(source);
	}
	
	public static void sort(long[] source) {
		sort(source, false);
	}
		
	public static void sort(long[] source, boolean desc) {
		if(Empty.is(source)) return;
		if(source.length < PARALLEL_LENGTH) {
			Arrays.sort(source); //Sorted by ASC
		}else {
			Arrays.parallelSort(source);
		}		
		if(desc) reverseArray(source);
	}
	
	public static void sort(float[] source) {
		sort(source, false);
	}
		
	public static void sort(float[] source, boolean desc) {
		if(Empty.is(source)) return;
		if(source.length < PARALLEL_LENGTH) {
			Arrays.sort(source); //Sorted by ASC
		}else {
			Arrays.parallelSort(source);
		}		
		if(desc) reverseArray(source);
	}
	
	public static void sort(double[] source) {
		sort(source, false);
	}
		
	public static void sort(double[] source, boolean desc) {
		if(Empty.is(source)) return;
		if(source.length < PARALLEL_LENGTH) {
			Arrays.sort(source); //Sorted by ASC
		}else {
			Arrays.parallelSort(source);
		}		
		if(desc) reverseArray(source);
	}
	
	public static void sort(byte[] source) {
		sort(source, false);
	}
		
	public static void sort(byte[] source, boolean desc) {
		if(Empty.is(source)) return;
		if(source.length < PARALLEL_LENGTH) {
			Arrays.sort(source); //Sorted by ASC
		}else {
			Arrays.parallelSort(source);
		}		
		if(desc) reverseArray(source);
	}
	
	public static void sort(char[] source) {
		sort(source, false);
	}
		
	public static void sort(char[] source, boolean desc) {
		if(Empty.is(source)) return;
		if(source.length < PARALLEL_LENGTH) {
			Arrays.sort(source); //Sorted by ASC
		}else {
			Arrays.parallelSort(source);
		}		
		if(desc) reverseArray(source);
	}
	
	public static void sort(short[] source) {
		sort(source, false);
	}
		
	public static void sort(short[] source, boolean desc) {
		if(Empty.is(source)) return;
		if(source.length < PARALLEL_LENGTH) {
			Arrays.sort(source); //Sorted by ASC
		}else {
			Arrays.parallelSort(source);
		}		
		if(desc) reverseArray(source);
	}
	
	public static void sort(String[] source) {
		sort(source, false);
	}
		
	public static void sort(String[] source, boolean desc) {
		if(Empty.is(source)) return;
		if(source.length < PARALLEL_LENGTH) {
			Arrays.sort(source); //Sorted by ASC
		}else {
			Arrays.parallelSort(source);
		}		
		if(desc) reverseArray(source);
	}
	
	public static<T extends Comparable<T>> void sort(T[] source) {
		sort(source, false);
	}
		
	public static<T extends Comparable<T>> void sort(T[] source, boolean desc) {
		if(Empty.is(source)) return;
		if(source.length < PARALLEL_LENGTH) {
			Arrays.sort(source); //Sorted by ASC
		}else {
			Arrays.parallelSort(source);
		}		
		if(desc) reverseArray(source);
	}
		
	private static void reverseArray(Object array) {
		if(array == null) return;
		if(!array.getClass().isArray()) return;
		Object tmp = null; //For swap
		var len = Array.getLength(array);
		if(len == 0 || len == 1) return;
        int loopTimes = len / 2;
	    
        for (int i = 0; i < loopTimes; i++) {
            tmp = Array.get(array, i);
            var idx = len - i - 1;
            var val = Array.get(array, idx);
            Array.set(array, i, val);
            Array.set(array, idx, tmp);
        }
    }
}