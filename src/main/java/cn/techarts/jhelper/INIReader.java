package cn.techarts.jhelper;

import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

/**
 * An implementation of Windows INI configuration file.
 */
public final class INIReader{
	
	private static final String SECTION = "\\[.*\\]";
	private Map<String, Map<String, String>> sections = new HashMap<>(32);
	
	public INIReader(String path){
		try{
			var reader = new BufferedReader(new FileReader( path));
			if( reader != null) { 
				loadIniData(reader); 
				reader.close();
			}
		}catch( IOException e){
			throw new RuntimeException( "Fail to load the ini file", e);
		}
	}
	
	public Map<String, Map<String, String>> getSections(){
		return this.sections;
	}
	
	public Map<String, String> getSection(String section){
		return this.sections.get(section);
	}
	
	public String get(String section, String key){
		var p = sections.get(section);
		return p != null ? p.get( key) : "";
	}
	
	public int getInt(String section, String key){
		return Converter.toInt(get( section, key));
	}
	
	public float getFloat(String section, String key){
		return Converter.toFloat(get( section, key));
	}
	
	public boolean getBool(String section, String key){
		return Converter.toBoolean(get(section, key));
	}
	
	//-------------------------------------------------------------------------------------------------------
	
	private void loadIniData( BufferedReader reader) throws IOException{
		if( reader == null) return; 
		String line = null;
		Map<String, String> section = null; 
		while( (line = reader.readLine()) != null) {
			line = line.trim();
			if( line.isEmpty()) continue;
			if( line.startsWith( "//")) continue;
			if( line.matches( SECTION)){
				section = new HashMap<String, String>(12);
				sections.put( sect( line), section);
			}else if( line.matches( ".*=.*")){
				if( section != null) set( line, section);
			}
		}
	}
	
	private void set( String line, Map<String, String> section){
		int i = line.indexOf( '='); if( i < 1) return;
		section.put( line.substring(0, i).trim(),
						  line.substring( i + 1).trim());
	}
	
	private String sect( String line){
		return line.replaceFirst("\\[(.*)\\]", "$1");
	}
}