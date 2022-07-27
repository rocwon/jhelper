package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.VLArray;
import junit.framework.TestCase;

public class VLArrayTest {
	
	@Test
	public void testVarLengthIntArray() {
		var array = new VLArray(32);
		array.put(1, 999);
		array.put(62, 22);
		TestCase.assertEquals(999, array.get(1));
		TestCase.assertEquals(22, array.get(62));
		TestCase.assertEquals(93, array.length);
		TestCase.assertEquals(63, array.get().length);
	}
}
