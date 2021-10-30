package cn.techarts.jhelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Reflector {
	
	public static<T> void setValue(Object obj, String field, Class<T> paramType, T value) {
		if(obj == null || Empty.is(field)) return;
		var method = toMethodName("set", field);
		try {
			Class<? extends Object> raw = obj.getClass();
			var fun = raw.getMethod(method, paramType);
			if(fun != null) fun.invoke(obj, value);
		}catch(Exception e) {
			throw new RuntimeException("Failed to set value.", e);
		}
	}
	
	public static<T> T getValue(Object obj, String field, Class<T> t) {
		if(obj == null || Empty.is(field)) return null;
		var method = toMethodName("get", field);
		try {
			var raw = obj.getClass();
			var getter = raw.getMethod(method);
			if(getter == null) return null;
			var o = getter.invoke(obj);
			return o != null ? t.cast(o) : null;
		}catch(Exception e) {
			throw new RuntimeException("Failed to get value.", e);
		}
	}
	
	//----------------------------------------------------------------
	
	public static int getInt(Object obj, String field) {
		var result = getValue(obj, field, Integer.class);
		return result != null ? result.intValue() : 0;
	}
	
	public static long getLong(Object obj, String field) {
		var result = getValue(obj, field, Long.class);
		return result != null ? result.longValue() : 0L;
	}
	
	public static float getFloat(Object obj, String field) {
		var result = getValue(obj, field, Float.class);
		return result != null ? result.floatValue() : 0F;
	}
	
	public static double getDouble(Object obj, String field) {
		var result = getValue(obj, field, Double.class);
		return result != null ? result.doubleValue() : 0d;
	}
	
	public static byte getByte(Object obj, String field) {
		var result = getValue(obj, field, Byte.class);
		return result != null ? result.byteValue() : 0x00;
	}
	
	public static char getChar(Object obj, String field) {
		var result = getValue(obj, field, Character.class);
		return result != null ? result.charValue() : 0x00;
	}
	
	public static short getShort(Object obj, String field) {
		var result = getValue(obj, field, Short.class);
		return result != null ? result.shortValue() : 0x00;
	}
	
	public static String getString(Object obj, String field) {
		return getValue(obj, field, String.class);
	}
	
	//------------------------------------------------------------------------------
	
	public static void setValue(Object obj, String field, int value) {
		setValue(obj, field, int.class, value);
	}
	
	public static void setValue(Object obj, String field, long value) {
		setValue(obj, field, long.class, value);
	}
	
	public static void setValue(Object obj, String field, float value) {
		setValue(obj, field, float.class, value);
	}
	
	public static void setValue(Object obj, String field, double value) {
		setValue(obj, field, double.class, value);
	}
	
	public static void setValue(Object obj, String field, byte value) {
		setValue(obj, field, byte.class, value);
	}
	
	public static void setValue(Object obj, String field, char value) {
		setValue(obj, field, char.class, value);
	}
	
	public static void setValue(Object obj, String field, short value) {
		setValue(obj, field, short.class, value);
	}
	
	public static void setValue(Object obj, String field, String value) {
		setValue(obj, field, String.class, value);
	}
	
	//-----------------------------------------------------------------------
	
	private static String toMethodName(String prefix, String field) {
		var chars = field.toCharArray();
		if (chars[0] >= 'a' && chars[0] <= 'z') {
			chars[0] = (char) (chars[0] - 32);
		}
		return prefix.concat(String.valueOf(chars));
	}
	
	private static String toFieldName(String method) {
		var chars = method.toCharArray();
		var idx = method.startsWith("is") ? 2 : 3;
		chars[idx] = (char)(chars[idx] + 32); //To lower-case
		return new String(Slicer.slice(chars, idx, 100));
	}
	
	private static boolean isGetter(String name) {
		if(Empty.is(name)) return false;
		if(name.startsWith("is")) return true;
		return name.startsWith("get");
	}
	
	/**
	 * Map to Bean
	 */
	public static void fill(Object target, Map<String, Object> data) {
		if(target == null || Empty.is(data)) return;
		try {
			var clazz = target.getClass();
			var methods = clazz.getMethods();
			if(Empty.is(methods)) return;
			for(var m : methods) {
				var name = m.getName();
				if(!name.startsWith("set")) continue;
				if(m.getParameterCount() != 1) continue;
				var param = data.get(toFieldName(name));
				if(param != null) m.invoke(target, param);
			}
		}catch(Exception e) {
			throw new RuntimeException("Failed to fill values to the bean", e);
		}	
	}
	
	/**
	 * Bean to Map
	 */
	public static Map<String, Object> dump(Object target) {
		if(target == null) return Map.of();
		try {
			var clazz = target.getClass();
			var methods = clazz.getMethods();
			if(Empty.is(methods)) return Map.of();
			var getters = new ArrayList<String>();
			for(var method : methods) {
				var name = method.getName();
				if(!isGetter(name)) continue;
				if(method.getParameterCount() > 0) continue;
				getters.add(name); //A legal getter method
			}
			if(getters.isEmpty()) return Map.of();
			var result = new HashMap<String, Object>(32);
			for(var getter : getters) {
				var m = clazz.getMethod(getter);
				if(m != null) {
					var f = toFieldName(getter);
					result.put(f, m.invoke(target));
				}
			}
			return result;
		}catch(Exception e) {
			throw new RuntimeException("Failed to dump the values to map", e);
		}
	}
}