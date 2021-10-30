package cn.techarts.jhelper.test;

import org.junit.Before;
import org.junit.Test;

import cn.techarts.jhelper.INIReader;
import junit.framework.TestCase;

public class IniReaderTest {
	INIReader config = null;
	
	//You should change the path to the file on your local machine
	@Before
	public void init() {
		config = new INIReader("D:\\Studio\\Project\\Java\\jhelper\\src\\main\\java\\cn\\techarts\\jhelper\\test\\config.ini");
	}
	
	@Test
	public void testReadItem() {
		var ip = config.get("network", "ip");
		TestCase.assertEquals("192.168.0.12", ip);
	}
}
