package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.Codec;
import junit.framework.TestCase;

public class CodecTest {
	@Test
	public void testBase64Codec() {
		var source = "pLease encode me to a base64 bytes array";
		var encoded = Codec.toBase64(source.getBytes());
		var decoded = Codec.decodeBase64(encoded);
		TestCase.assertEquals("pLease", new String((decoded)).substring(0, 6));
	}
	
	@Test
	public void testJsonCodec() {
		var book = new Book(1, null, "Little Fairy");
		var json = Codec.toJson(book);
		var compactJson = Codec.toJsonCompact(book);
		TestCase.assertEquals(true, json.length() > compactJson.length());
		book = Codec.decodeJson(json, Book.class);
		TestCase.assertEquals("Little Fairy", book.getName());
	}
	
	@Test
	public void testMsgPackCodec() {
		var book = new Book(1, null, "Little Fairy");
		var bytes = Codec.toMsgPack(book);
		book = Codec.decodeMsgPack(bytes, Book.class);
		TestCase.assertEquals("Little Fairy", book.getName());
	}
}
