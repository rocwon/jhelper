package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.Spliter;
import junit.framework.TestCase;

public class SpliterTest {

	@Test
	public void testSplit2String() {
		var arg = "jhelper,is,a,very,good,library";
		var result = Spliter.split(arg, ',');
		TestCase.assertEquals("very", result.get(3));
	}
	
	@Test
	public void testSplit2Integer() {
		var arg = "333,2,56,4,0,7";
		var result = Spliter.split(arg, ',', false);
		TestCase.assertEquals(Integer.valueOf(4), result.get(3));
	}
}
