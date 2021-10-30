package cn.techarts.jhelper;

import java.io.UnsupportedEncodingException;

/**
 * <h3>Only GB2312 CHAR-SET is supported. </h3>
 * The class is very light-weight. it doesn't dependent on any Chinese dictionary.
 * A few Chinese characters can't be recognized because it's out of GB2312 char-set.
 */
public class Chinese {
	private final static int[] SP_BOUNDARIES = { 1601, 1637, 1833, 2078, 2274,
			2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
			4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590 };
	private final static String[] FIRST_LETTERS = { "A", "B", "C", "D", "E",
			"F", "G", "H", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "W", "X", "Y", "Z" };

	/**
	 * @return Returns the first letters of PINYIN for the given Chinese words<p>
	 * For example, "½­É½Èç»­" --> "JSRH"
	 */
	public static String getPinyinFirstLetters(String chinese) {
		if (chinese == null || "".equals(chinese.trim())) return "";
		var result = new StringBuilder();
		char[] chars = chinese.toCharArray();
		for(char ch : chars) {
			result.append(getInitialFirstLetter(String.valueOf(ch)));
		}
		return result.toString();
	}

 	private static String getInitialFirstLetter(String chinese) {
		var result = convertCharset(chinese, "GB2312", "ISO8859-1");
		if (result.length() > 1) { //It's Chinese chars
			int sectorCode = (int)result.charAt(0) - 160; //Section Code
			int positionCode = (int)result.charAt(1) - 160; //Position Code
			int secPosCode = sectorCode * 100 + positionCode; //Sec-Pos Code
			if (secPosCode > 1600 && secPosCode < 5590) {
				for (int i = 0; i < 23; i++) {
					if(secPosCode < SP_BOUNDARIES[i]) continue;
					if(secPosCode >= SP_BOUNDARIES[i + 1]) continue;
					result = FIRST_LETTERS[i]; break;
				}
			}else{
				result = convertCharset(chinese, "ISO8859-1", "GB2312");
				result = "".equals(result) ? "" : result.substring(0, 1);
			}
		}
		return result;
	}
 	
	private static String convertCharset(String source, String sourceCharset,String destCharset) {
		try {
			return new String(source.getBytes(sourceCharset), destCharset);
		} catch (UnsupportedEncodingException ex) { return "";}
	}
}