package cn.techarts.jhelper.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import cn.techarts.jhelper.Finder;
import cn.techarts.jhelper.Sorter;
import junit.framework.TestCase;

public class SorterTest {
	
	@Test
	public void testSortPrimitiveTypeArray() {
		var src = new int[] {3,7,1, 0, 5, 9, 8};
		Sorter.sort(src);
		TestCase.assertEquals(1, src[1]);
		Sorter.sort(src, true);
		TestCase.assertEquals(8, src[1]);
	}
	
	@Test
	public void testSortStringArray() {
		var src = new String[] {"A", "C", "B", "D", "X", "F", "H"};
		Sorter.sort(src);
		TestCase.assertEquals("B", src[1]);
		Sorter.sort(src, true);
		TestCase.assertEquals("H", src[1]);
	}
	
	@Test
	public void testSortList() {
		//88, 5, 71, 7, 0, 23, 9
		var src =new ArrayList<Integer>();
		src.add(88);
		src.add(5);
		src.add(71);
		src.add(7);
		src.add(0);
		src.add(23);
		src.add(9);
		Sorter.sort(src);
		TestCase.assertEquals(Integer.valueOf(0), src.get(0));
		Sorter.sort(src, true);
		TestCase.assertEquals(Integer.valueOf(71), src.get(1));
	}
	
	@Test
	public void testParallelSortList() {
		Random random = new Random();
		List<Integer> src =new ArrayList<>();
		
		for(int i = 0; i < 10000; i++) {
			var tmp = random.nextInt();
			if(Finder.find(src, tmp) < 0)src.add(tmp);
		}
		src = Sorter.parallelSort(src);
		TestCase.assertEquals(true, src.get(13) < src.get(27));
		
		src = Sorter.parallelSort(src, true);
		TestCase.assertEquals(false, src.get(13) < src.get(27));
	}
}