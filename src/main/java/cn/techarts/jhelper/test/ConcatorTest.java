package cn.techarts.jhelper.test;

import java.util.List;

import org.junit.Test;

import cn.techarts.jhelper.Concator;
import junit.framework.TestCase;

public class ConcatorTest {

	@Test
	public void testContactArrays() {
			var result = Concator.concat(new int[] {1,2,3,4,5,6}, new int[] {7,8,9,10,11,12,13,14,15,16});
			TestCase.assertEquals(16, result.length);
	}
	
	@Test
	public void testConcatWithSeparator() {
		var result = Concator.concat(',', "abc", "cd", "ef");
		TestCase.assertEquals("abc,cd,ef", result);
		result = Concator.concat(List.of("abc", "cd", "ef"), ',');
		TestCase.assertEquals("abc,cd,ef", result);
		var result1 = Concator.concat(',', 1, 0, 2 ,3 ,4, 5);
		TestCase.assertEquals("1,0,2,3,4,5", result1);
		result1 = Concator.concat(List.of(1, 0, 2 ,3 ,4, 5), ',');
		TestCase.assertEquals("1,0,2,3,4,5", result1);
	}
}
