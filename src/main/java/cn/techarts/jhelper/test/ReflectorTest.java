package cn.techarts.jhelper.test;

import java.util.Map;

import org.junit.Test;

import cn.techarts.jhelper.Reflector;
import junit.framework.TestCase;

public class ReflectorTest {
	
	@Test
	public void testGetValue() {
		var book = new Book(1, "ISBN-111-00", "Little Fairy");
		TestCase.assertEquals(1, Reflector.getInt(book, "id"));
		TestCase.assertEquals("ISBN-111-00", Reflector.getString(book, "isbn"));
	}
	
	@Test
	public void testSetValue() {
		var book = new Book();
		Reflector.setValue(book, "id", 520);
		TestCase.assertEquals(520, book.getId());
	}
	
	@Test
	public void testBean2Map() {
		var book = new Book(1, "ISBN-111-00", "Little Fairy");
		TestCase.assertEquals(true, !Reflector.dump(book).isEmpty());
	}
	
	@Test
	public void testMap2Bean() {
		Map<String, Object> map = Map.of("id", 1, "isbn", "ISBN-191-077", "name", "Little fairy", "chinese", true);
		var result = new Book();
		Reflector.fill(result, map);
		TestCase.assertEquals("ISBN-191-077", result.getIsbn());
	}
}
