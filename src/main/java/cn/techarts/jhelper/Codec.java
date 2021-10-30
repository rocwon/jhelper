package cn.techarts.jhelper;

import java.util.Base64;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The utility dependents on JACKSON(Faster JSON) library.<br>
 *  you MUST import the JACKSON to your classpath firstly before using this.<p>
 * jhelper Codec supports 3 protocols as follows:<br>
 * 1. base64: {@link https://datatracker.ietf.org/doc/html/rfc4648}<br>
 * 2. JSON: {@link https://www.json.org/json-en.html}<br>
 * 3. MSGPACK: {@link https://msgpack.org/}
 */
public final class Codec {
	private static ObjectMapper jcodec = null,	ccodec = null, bcodec = null;
	
	static {
		ccodec = new ObjectMapper(); //Compact JSON
		jcodec = new ObjectMapper(); //Normal JSON string(with all properties)(Raw JSON)
		bcodec = new ObjectMapper(new MessagePackFactory());//Serialize to BINARY bytes(MSGPACK)
		
		ccodec.setSerializationInclusion(Include.NON_DEFAULT);
		
		jcodec.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		ccodec.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		bcodec.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		
		jcodec.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ccodec.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		bcodec.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	//------------------------------------Base64--------------------------------------------------
	
	public static String toBase64(byte[] src) {
		if(Empty.is(src)) return null;
		return new String(Base64.getEncoder().encode(src));
	}
	
	/**
	 * Decode a base64 encoding string to a byte array
	 */
	public static byte[] decodeBase64(String src) {
		if(Empty.is(src)) return null;
		try {
			return Base64.getDecoder().decode(src);
		}catch(IllegalArgumentException e) {
			return null; //Invalid base64 format
		}
	}
	
	//--------------------------------JSON & MSGPACK------------------------------------------------
	
	public static String toJson(Object src)  throws RuntimeException{
		if(Empty.is(src)) return null;
		try{
			return jcodec.writeValueAsString(src);
		}catch( Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Encodes to JSON and ignores all properties which value is NULL or 0 to reduce the size. 
	 */
	public static String toJsonCompact(Object src)  throws RuntimeException{
		if(Empty.is(src)) return null;
		try{
			return ccodec.writeValueAsString(src);
		}catch( Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static<T> T decodeJson(String src, Class<T> targetClass) throws RuntimeException {
		try {
			return jcodec.readValue(src, targetClass);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//------------------------------------MSGPACK-----------------------------------------------
	
	public static byte[] toMsgPack(Object src) throws RuntimeException {
		try{
			return bcodec.writeValueAsBytes( src);
		}catch( Exception e){
			throw new RuntimeException(e);
		}
	}	
	
	public static<T> T decodeMsgPack(byte[] src, Class<T> targetClass) throws RuntimeException {
		try {
			return bcodec.readValue(src, targetClass);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}