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
}
