package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.Converter;
import cn.techarts.jhelper.Finder;
import junit.framework.TestCase;

public class ConverterTest {
	
	@Test
	public void testConvertString() {
		var arg = "1";
		TestCase.assertEquals(1, Converter.toInt(arg));
		TestCase.assertEquals(1f, Converter.toFloat(arg));
		TestCase.assertEquals(1d, Converter.toDouble(arg));
		TestCase.assertEquals(1l, Converter.toLong(arg));
	}
	
	@Test
	public void testConvert2String() {
		TestCase.assertEquals("1", Converter.toString(1));
		TestCase.assertEquals("1.2", Converter.toString(1.2));
		TestCase.assertEquals("1.23", Converter.toString(1.23));
		TestCase.assertEquals("1", Converter.toString((byte)0x01));
	}
	
	@Test
	public void testArray2Collection() {
		var arg = new Book[] {new Book(1), new Book(2), new Book(3)};
		TestCase.assertEquals(2, Converter.toList(arg).get(1).getId());
	}
	
	@Test
	public void testConvertObject() {
		Object obj = new Book(2);
		var book = Converter.to(obj, Book.class);
		TestCase.assertEquals(obj, book);
	}
	
	@Test
	public void testBitManipulations() {
		int arg = 1234567890;
		var val = Converter.toBytes(arg);
		TestCase.assertEquals(arg, Converter.toInt(val));
		
		short arg1 = 12;
		var val1 = Converter.toBytes(arg1);
		TestCase.assertEquals(arg1, Converter.toShort(val1));
		
		byte arg3 = 0x00;
		var val3 = Converter.toBooleans(arg3);
		TestCase.assertEquals(false, val3[5]);
		
	}
}
