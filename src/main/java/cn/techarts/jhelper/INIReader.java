package cn.techarts.jhelper;

import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

/**
 * An implementation of Windows INI configuration file.<p>
 * I always think the INI structure is more powerful than properties file.
 * Maybe you noticed that some very popular open source softwares using INI 
 * to store their configurations such as PHP(php.ini) and MYSQL(my.ini).
 */
public final class INIReader{
	
	private static final String SECTION = "\\[.*\\]";
	private Map<String, Map<String, String>> sections = new HashMap<>(32);
	
	/**
	 *@param path The absolute full path of the configuration file 
	 */
	public INIReader(String path){
		try{
			var reader = new BufferedReader(new FileReader(path));
			if(reader != null) { 
				loadIniData(reader); 
				reader.close();
			}
		}catch(IOException e){
			throw new RuntimeException("Fail to load the ini file", e);
		}
	}
	
	/**
	 * @return Returns all sections that defined in the configuration file
	 */
	public Map<String, Map<String, String>> getSections(){
		return this.sections;
	}
	
	/**
	 * @return Returns the entry section including all key-value pairs
	 */
	public Map<String, String> getSection(String section){
		return this.sections.get(section);
	}
	
	/**
	 * @return Returns the value of the specified key as a string
	 */
	public String get(String section, String key){
		var p = sections.get(section);
		return p != null ? p.get(key) : "";
	}
	
	/**
	 * @return Returns an integer value of the specified key<br>
	 * Actually, it converts the result of the method {@link get} to INT 
	 */
	public int getInt(String section, String key){
		return Converter.toInt(get(section, key));
	}
	
	/**
	 * @return Returns an integer value of the specified key<br>
	 * Actually, it converts the result of the method {@link get} to float 
	 */
	public float getFloat(String section, String key){
		return Converter.toFloat(get(section, key));
	}
	
	/**
	 * @return Returns an integer value of the specified key<br>
	 * Actually, it converts the result of the method {@link get} to boolean 
	 */
	public boolean getBool(String section, String key){
		return Converter.toBoolean(get(section, key));
	}
	
	//-------------------------------------------------------------------------------------------------------
	
	private void loadIniData(BufferedReader reader) throws IOException{
		if(reader == null) return; 
		String line = null;
		Map<String, String> section = null; 
		while((line = reader.readLine()) != null) {
			line = line.trim();
			if(line.isEmpty()) continue;
			// 2 types of comments are supported
			if(line.startsWith("#")) continue;
			if(line.startsWith("//")) continue;
			if(line.matches(SECTION)){
				section = new HashMap<String, String>(12);
				sections.put(sect(line), section);
			}else if(line.matches(".*=.*")){
				if(section != null) set(line, section);
			}
		}
	}
	
	private void set(String line, Map<String, String> section){
		int i = line.indexOf('='); if(i < 1) return;
		section.put(line.substring(0, i).trim(),
						  line.substring(i + 1).trim());
	}
	
	private String sect(String line){
		return line.replaceFirst("\\[(.*)\\]", "$1");
	}
}