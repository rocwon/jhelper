package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.Cryptor;
import cn.techarts.jhelper.Guarder;
import junit.framework.TestCase;

public class GuarderTest {

	@Test
	public void testGenerateVerifyPicture() {
		var result = Guarder.getVerificationCode();
		TestCase.assertEquals(4, result[0].length());
		TestCase.assertEquals(true, result[1].length() > 12);
	}
	
	@Test
	public void testEncryptPassword() {
		var password = "abc&123_0";
		var salt = "LIKEABRIDGEOVERTROUBLEDWATER";
		var result = Guarder.encryptPassword(password, salt);
		TestCase.assertEquals(32, result.length());
	}
	
	@Test
	public void testGenerateRandPassword() {
		var pwd = Guarder.randomPassword();
		TestCase.assertEquals(6, pwd.length());
		var uuid = Guarder.uuid();
		TestCase.assertEquals(32, uuid.length());
	}
	
	@Test
	public void testGeneateAndVerifySession() {
		int ttl = 1000; //Minutes
		int userAgent = 2;
		var ip = "192.168.10.232";
		//var key = Cryptor.toHex(Cryptor.getKey(), false);
		var key = Cryptor.getHexKey();
		var salt = "wouldyouknowmyname";
		var session = Guarder.generateSession("12", ip, userAgent, key, salt);
		TestCase.assertEquals(true, Guarder.verifySession(session, "12", ip, userAgent, key, salt, ttl));
	}
		
}
