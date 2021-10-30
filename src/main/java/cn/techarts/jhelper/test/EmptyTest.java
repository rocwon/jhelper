package cn.techarts.jhelper.test;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import cn.techarts.jhelper.Empty;
import junit.framework.TestCase;

public class EmptyTest {
	
	@Test
	public void testArrayIsEmpty() {
		int[] arg = null;
		TestCase.assertEquals(true, Empty.is(arg));
		float[] arg1 = new float[] {1f, 2f};
		TestCase.assertEquals(false, Empty.is(arg1));
	}
	
	@Test
	public void testStringIsEmpty() {
		var arg = "";
		TestCase.assertEquals(true, Empty.is(arg));
		arg = null;
		TestCase.assertEquals(true, Empty.is(arg));
		arg = "If you miss the train I am on";
		TestCase.assertEquals(false, Empty.is(arg));
	}
	
	@Test
	public void testCollectionIsEmpty() {
		TestCase.assertEquals(true, Empty.is(List.of()));
		TestCase.assertEquals(true, Empty.is(Set.of()));
		TestCase.assertEquals(false, Empty.is(List.of(1,2)));
		TestCase.assertEquals(false, Empty.is(Set.of(1,2)));
		List<Object> arg = null;
		TestCase.assertEquals(true, Empty.is(arg));
	}
	
	@Test
	public void testGenerateEmptyCollection() {
		var arg = Empty.list();
		TestCase.assertEquals(true, arg != null);
		
		arg = Empty.immutableList(); //Read-only
		try {
			arg.add("1");
		}catch(Exception e) {
			TestCase.assertEquals(true, true);
		}
	}
	
	@Test
	public void testZero() {
		byte arg1 = 0x00;
		TestCase.assertEquals(true, Empty.zero(arg1));
		short arg2 = 0;
		TestCase.assertEquals(true, Empty.zero(arg2));
		int arg3 = 0;
		TestCase.assertEquals(true, Empty.zero(arg3));
		float arg4 = 0f;
		TestCase.assertEquals(true, Empty.zero(arg4));
		long arg5 = 0l;
		TestCase.assertEquals(true, Empty.zero(arg5));
		double arg6 = 0d;
		TestCase.assertEquals(true, Empty.zero(arg6));
	}
	
	@Test
	public void testOneOf() {
		Book book1 = new Book(1);
		Book book2 = new Book(2);
		TestCase.assertEquals(true, Empty.oneOf(book1, book2, null));
		TestCase.assertEquals(false, Empty.allOf(book1, book2, null));
		TestCase.assertEquals(true, Empty.is(null, book2) != null);
	}
}
