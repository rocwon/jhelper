package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.Finder;
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
	
	@Test
	public void testSpliterPerformance() {
		var arg = "abc,2321,56,67,sdf,retery,0kdsf,34,addsfdsf,35435qq,msdfa";
		var bgn = System.nanoTime();
		for(var i = 0; i < 100000000; i++) {
			var segs = arg.split(",");
			Finder.find(segs, "56");
			//Spliter.split(arg, ',');
		}
		var end = System.nanoTime();
		System.out.println((end - bgn) / 1000000);
	}
}
