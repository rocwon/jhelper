package cn.techarts.jhelper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

/**
 * The class provides some methods to help you protect your system security.
 */
public final class Guarder {
	private static final String SPECIAL_CHARS = "&%$#@!*_";
	private static final String LETTERS = "ABCDEFGHJKLMNPQRSTUVWXY";
	
	/**@return A unique sequence contains 32 chars*/
	public static String uuid(){
		var result = UUID.randomUUID().toString();
		return result.replace("-", "");
	}
		
	//------------------------Verify Code---------------------------------
	
	/**
	 * Generates a random code and draw a picture
	 *@param length The number length of in the picture
	 *@param w, h the width and the height of the picture
	 *@return A string array of the code and picture(base64), e.g.<p>
	 *["1324", "data:image/png;base64,picture-base64-string"] 
	 */
	public static String[] getVerificationCode(int length, int w, int h) {
		var random = new Random();  
		var type = BufferedImage.TYPE_INT_RGB;
		var image = new BufferedImage(w, h, type);     
	    var g = drawBackground(image, random, w, h);
	    var code = drawVerifyCode(g, random, length);
	    try {
		    var baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			baos.flush();
			var prefix = "data:image/png;base64,";
			var b64 = Codec.toBase64(baos.toByteArray());
			baos.close(); //Close the stream
			return new String[]{code, prefix + b64};
	    }catch(Exception e) {
	    	return null;
	    }
	}
	
	/**
	 * Returns the code and picture using default parameters:<p>
	 * length = 4, width = 60, height = 20
	 */
	public static String[] getVerificationCode() {
		return getVerificationCode(4, 60, 20);
	}
	
	private static String drawVerifyCode(Graphics g, Random random, int length){
		var result = new StringBuilder(length);
		for (int i = 0;i < length; i++) {        
			 var ch = String.valueOf(random.nextInt(10));        
			 result.append(ch);
			 g.setColor(getRandColor(random));        
			 g.drawString(ch, 13 * i + 6, 16);     
		}     
		g.dispose();
		return result.toString();
	}
	
	private static Graphics drawBackground(BufferedImage image, Random random, int w, int h){
		 Graphics result = image.getGraphics();
		 result.setColor(getRandColor(200, 250, random));    
		 result.fillRect( 0, 0, w, h);     
		 result.setFont(new Font("Times New Roman", Font.PLAIN, 18));     
		 result.setColor(new Color( 20, 20, 20));    
		 result.drawRect( 0, 0, w - 1, h - 1);      
		 result.setColor(getRandColor(160, 200, random));    
		 for (int i = 0; i < 60; i++) {      
			int x = random.nextInt(w);        
			int y = random.nextInt(h);        
			int xl = random.nextInt(12);        
			int yl = random.nextInt(12);        
			result.drawLine(x, y, x + xl, y + yl);    
		}
		 return result;
	}
	
	private static Color getRandColor(Random rnd){
		int r = 20 + rnd.nextInt(110);
		int g = 20 + rnd.nextInt(110);
		int b = 20 + rnd.nextInt(110);
		return new Color(r, g, b); //
	}
	
	private static Color getRandColor(int fc,int bc, Random random) {    
		 fc = fc > 255 ? 255 : fc;
		 bc = bc > 255 ? 255 : bc;
		 int r = fc + random.nextInt(bc - fc);    
		 int g = fc + random.nextInt(bc - fc);    
		 int b = fc + random.nextInt(bc - fc);    
		 return new Color(r, g, b); 
	}
	
	//-----------------------------Password-----------------------------------------
	
	/**
	 * Encrypt the given password with specified salt using MD5<p>
	 * If you want to use other algorithms, please refer to {@link Cryptor}
	 * @param salt It's a string to make your password stronger
	 */
	public static String encryptPassword(String password, String salt) {
		if(Empty.is(password)) return null;
		if(Empty.is(salt)) {
			return Cryptor.md5(password);
		}else {
			return Cryptor.md5(password.concat(salt));
		}
	}
	
	/**
	 * @return A 6-chars random string that contains letters, numbers and special chars 
	 */
	public static String randomPassword() {
		var rand = new Random();
		var num1 = rand.nextInt(10) + "";;
		var num2 = (rand.nextInt(88) + 10) + "";
		char rc1 = LETTERS.charAt(rand.nextInt(23));
		char rc2 = LETTERS.charAt(rand.nextInt(23));
		char rc3 = SPECIAL_CHARS.charAt(rand.nextInt(8));
		return num2 + (char)(rc1 + 32) + rc3 + num1 + rc2;
	}
	
	//----------------------------------Session--------------------------------------------
	
	/**
	 * @return  Generates an encrypted session string with the specified key and salt. Specially, you
	 * 			don't need to save/cache the session in memory(e.g. REDIS). <p>
	 * @param userId A unique number or string(e.g. mobile phone number) identifies the user
	 * @param ip The remote client IP address. If you don't want to check it, please pass a null string
	 * @param userAgent A number between 0 - 9 you defined is to indicate the client device types
	 * @param key A required byte array generated by {@link Cryptor.getKey} for encrypt.
	 * @param salt A specific string you predefined is to improve the security. It's strongly recommend.
 	 */
	public static String generateSession(String userId, String ip, int userAgent, String key, String salt) {
		if(Empty.is(userId) || Empty.is(key)) return null;
		var ipAddr = Empty.is(ip) ? "0000" : ip;
		var theSalt = Empty.is(salt) ? "" : salt;
		var result = ipAddr + userAgent + userId;
		var minutes = String.valueOf(Time.minutes());
		result = minutes.concat(result).concat(theSalt);
		return Cryptor.encrypt(result, Cryptor.toBytes(key));
	}
	
	
	/**
	 * Check the given session according to the specified parameters<p>
	 * @param session A string that generated by the above method {@link generateSession}
	 * @param userId A unique number or string(e.g. mobile phone number) identifies the user
	 * @param ip The remote client IP address. If you don't want to check it, please pass a null string
	 * @param userAgent A number between 0 - 9 you defined is to indicate the client device types
	 * @param key A required byte array generated by {@link Cryptor.getKey} for encrypt.
	 * @param salt A specific string you predefined is to improve the security. It's strongly recommend.
	 * @param ttl Time To Live. A number in minutes is used to check the session expiration
 	 */
	public static boolean verifySession(String session, String userId, String ip, int userAgent, String key, String salt, int ttl) {
		if(Empty.is(userId) || Empty.is(key)) return false; //Invalid UserId
		var tmp = Cryptor.decrypt(session, Cryptor.toBytes(key));
		if(Empty.is(tmp)) return false; //An invalid session
		if(ttl > 0) {
			var bgn = Converter.toInt(tmp.substring(0, 8));
			if(Time.minutes() - bgn > ttl) return false;
		}
		var ipAddr = Empty.is(ip) ? "0000" : ip;
		var theSalt = Empty.is(salt) ? "" : salt;
		var result = ipAddr + userAgent + userId + theSalt;
		return result.equals(tmp.substring(8));
	}
	
}