package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.Concator;
import junit.framework.TestCase;

public class ConcatorTest {

	@Test
	public void testContactArrays() {
			var result = Concator.concat(new int[] {1,2,3,4,5,6}, new int[] {7,8,9,10,11,12,13,14,15,16});
			TestCase.assertEquals(16, result.length);
	}
}
