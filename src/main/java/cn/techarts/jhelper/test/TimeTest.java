package cn.techarts.jhelper.test;

import java.util.Date;

import org.junit.Test;

import cn.techarts.jhelper.Time;
import junit.framework.TestCase;

public class TimeTest {
	
	@Test
	public void testGetQuarter() {
		var date = Time.parse("2021-10-29");
		TestCase.assertEquals(4, Time.quarter(date));
	}
	
	@Test
	public void testGetMonthMaxDays() {
		var date = Time.parse("2021-10-29");
		TestCase.assertEquals(31, Time.monthMaxDays(date));
	}
	
	@Test
	public void testFormatDate() {
		var date = Time.parse("2021-10-29");
		TestCase.assertEquals("2021-10-29", Time.format(date, false));
	}
	
	@Test
	public void testIsDateExpired() {
		var date = Time.parse("2021-10-28");
		TestCase.assertEquals(false, Time.expired(new Date()));
		TestCase.assertEquals(false, Time.expired(date, new Date()));
	}
	
	@Test
	public void testBundariesOfMonth() {
		var date = Time.parse("2021-10-29");
		var bgn = Time.beginOfMonth(date);
		var end = Time.endOfMnoth(date);
		TestCase.assertEquals(20211001, Time.format(bgn));
		TestCase.assertEquals(20211031, Time.format(end));
	}
	
	@Test
	public void testIsLeapYear() {
		var date = Time.parse("2021-10-29");
		TestCase.assertEquals(false, Time.isLeapYear(date));
		date = Time.parse("2020-10-29");
		TestCase.assertEquals(true, Time.isLeapYear(date));
	}
}
