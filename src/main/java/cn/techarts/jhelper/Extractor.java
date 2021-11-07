package cn.techarts.jhelper;

public final class Extractor {
	
	/**
	 * @return Returns the birthday from a Citizen-Identification-Number
	 */
	public static String extractBirthday(String cin) {
		if(!Validator.validateCIN(cin)) return null;
		var bs = new char[] {0, 0, 0, 0, '-', 0, 0, '-', 0, 0};
		cin.getChars(6, 10, bs, 0);
		cin.getChars(10, 12, bs, 5);
		cin.getChars(12, 14, bs, 8);
		return String.valueOf(bs);
	}
}
