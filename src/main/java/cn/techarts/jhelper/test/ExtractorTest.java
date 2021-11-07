package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.Extractor;
import junit.framework.TestCase;

public class ExtractorTest {
	
	@Test
	public void testExtractBirthday() {
		var cin = "512926197810302278";
		var b = Extractor.extractBirthday(cin);
		TestCase.assertEquals("1978-10-30", b);
	}
}
