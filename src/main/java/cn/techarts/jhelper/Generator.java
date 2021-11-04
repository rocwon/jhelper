package cn.techarts.jhelper;

import java.util.Date;
import java.util.Random;

public final class Generator {
	
	/**
	 *@return Returns a unique string that contains 32 chars  
	 */
	public static String uuid() {
		return Guarder.uuid();
	}
	
	/**
	 *@return Returns a 6 chars random password which contains number, letters and special chars 
	 */
	public static String randomPassword() {
		return Guarder.randomPassword();
	}
	
	/**
	 * @return Returns an integer between 0(inclusive) to max(exclusive)
	 */
	public static int randomNumber(int max) {
		var random = new Random(Time.seconds());
		return random.nextInt(max);
	}
	
	/**
	 * @return Returns a string (3 ~ 6 chars number)<p>
	 * If you want a picture verification code, please call the method {@link Guarder.getVerificationCode}
	 */
	public static String verificationCode(int length) {
		var result = new StringBuilder("");
		var random = new Random(Time.seconds());
		var len = length <= 3 || length > 6 ? 4 : length;
		for (int i = 0;i < len; i++) {        
			 result.append(random.nextInt(10));
		}
		return result.toString();
	}
	
	/**
	 * The method is design to generate an order-number, a project number etc.
	 * @return Returns a string follows the pattern like: <b>XIAOMI-20211101-0012</b>
	 * @param prefix The leading part of the sequence such as company name, order type
	 * @param postfix The tail part of  the sequence such as a unique number<p>
	 * 
	 * @see Time.format(Date date)
	 */
	public static String dateStyleSequence(String prefix, String postfix) {
		if(Empty.is(prefix) || Empty.is(postfix)) return null;
		var date = Time.format(new Date());
		return new StringBuilder(prefix).append('-')
			   .append(date).append('-').append(postfix).toString();
	}
}
