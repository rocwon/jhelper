package cn.techarts.jhelper.test;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import cn.techarts.jhelper.Finder;
import junit.framework.TestCase;

public class FinderTest {
	
	@Test
	public void testFindInList() {
		var target = new Book(2);
		var src = List.of(new Book(1), new Book(6), target, new Book(3), new Book(4));
		TestCase.assertEquals(2, Finder.find(src, target));
	}
	
	@Test
	public void testFindInSet() {
		var target = new Book(2);
		var src = Set.of(new Book(1), new Book(6), target, new Book(3), new Book(4));
		TestCase.assertEquals(true, Finder.find(src, target));
	}
	
	@Test
	public void testFindInPrimitiveTypeArray() {
		var src = new int[] {1, 2, 3, 4, 5, 0, 6, 7, 8, 9, 90};
		TestCase.assertEquals(5, Finder.find(src, 0));
	}
	
	@Test
	public void testFindInString() {
		var src = "Are you going to scarborough fair?";
		TestCase.assertEquals(14, Finder.find(src, "to"));
	}
	
	@Test
	public void testFindInObjectArray() {
		var target = new Book(2);
		var src = new Book[] {new Book(1), new Book(6), target, new Book(3), new Book(4)};
		TestCase.assertEquals(2, Finder.find(src, target));
	}
}
