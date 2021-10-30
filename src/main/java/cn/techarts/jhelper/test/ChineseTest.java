package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.Pinyin;
import junit.framework.TestCase;

public class ChineseTest {
	
	@Test
	public void testGetChineseInitials() {
		var ch = "\u4E16\u754C\u4EBA\u6C11\u5927\u56E2\u7ED3\u4E07\u5C81";
		TestCase.assertEquals("SJRMDTJWS", Pinyin.getFirstLetters(ch));
	}
}
