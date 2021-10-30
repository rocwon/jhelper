package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.Requester;
import junit.framework.TestCase;

public class RequesterTest {
	
	@Test
	public void testPingWebsite() {
		var status = Requester.ping("https://www.baidu.com");
		TestCase.assertEquals(200, status);
	}
}
