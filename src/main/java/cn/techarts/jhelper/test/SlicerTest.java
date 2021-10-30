package cn.techarts.jhelper.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.techarts.jhelper.Slicer;
import junit.framework.TestCase;

public class SlicerTest {
	
	@Test
	public void testSliceArray() {
		var arg = new int[] {1, 2, 3, 4, 5, 6};
		var result = Slicer.slice(arg, 1, 3);
		TestCase.assertEquals(3, result.length);
		TestCase.assertEquals(3, result[1]);
	}
	
	@Test
	public void testSliceCollection() {
		var arg = new ArrayList<>(List.of(1, 2, 3, 4, 5));
		var result = Slicer.slice(arg, 1, 3);
		TestCase.assertEquals(3, result.size());
		TestCase.assertEquals(Integer.valueOf(3), result.get(1));
	}
}
