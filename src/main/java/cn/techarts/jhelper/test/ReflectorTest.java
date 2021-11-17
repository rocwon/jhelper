package cn.techarts.jhelper.test;

import java.util.List;
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
	
	@Test
	public void testGetObjects() {
		var src = List.of(new Book(1), new Book(2), new Book(3), new Book(4), new Book(5));
		TestCase.assertEquals("3", Reflector.getInts("id", src).get(2));
		TestCase.assertEquals(Integer.valueOf(3), Reflector.getInts(src, "id").get(2));
		TestCase.assertEquals(Integer.valueOf(3), Reflector.getValues(src, "id", Integer.class).get(2));
	}
}
