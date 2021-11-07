package cn.techarts.jhelper.test;

import org.junit.Test;

import cn.techarts.jhelper.Validator;
import junit.framework.TestCase;

public class ValidatorTest {
	
	@Test
	public void testValidateCIN() {
		var myid = "512926197810302278";
		TestCase.assertEquals(true, Validator.validateCIN(myid));
	}
	
	@Test
	public void testValidateUSCI() {
		var usci = "91310114MA1GULBH3P";
		TestCase.assertEquals(true , Validator.validateUSCI(usci));
	}
	
	@Test
	public void testValidateICCID() {
		var iccid = "89860619140023790346";
		TestCase.assertEquals(true, Validator.validateICCID(iccid));
	}
	
	@Test
	public void testValideMobile() {
		var mobile = "13980092699";
		TestCase.assertEquals(true, Validator.validateMobile(mobile));
	}
}
