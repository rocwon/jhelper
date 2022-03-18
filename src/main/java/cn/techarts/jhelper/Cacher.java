package cn.techarts.jhelper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Response;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A REDIS client wrapper that's easier to use.<p>
 * 
 * Serialization is default with JSON REDUCE(Compact.REDUCED)<p>
 * <b>NOTE: The database number 0 is reserved for message queue<b>
 * 
 * */
public final class Cacher {
	/**
	 * The database of number 0 is reserved for message queue
	 */
	private static int CACHE_MSGQUEUE = 0;
	
	private static JedisPool connectionPool = null;
	
	private static boolean CONNECTION_POOL_INITED = false;
	
	/**
	 * The default server is 127.0.0.1:6379 if you pass null to these arguments.
	 */
	public static void init(String host, String port, String maxConnections) {
		if(host == null || port == null) return;
		int max = Converter.toInt(maxConnections);
		var server = host != null ? host : "127.0.0.1";
		var p = port != null ? Converter.toInt(port) : 6379; 
		initConnectionPool(server, p, max > 0 ? max : 20);
	}
	
	@Override
	protected void finalize() {
		destroy();
	}
	
	public static void initConnectionPool(String host, int port, int max) {
		CONNECTION_POOL_INITED = true;
		if (connectionPool == null) {
			var config = new JedisPoolConfig();
			config.setMinIdle(1);
			config.setMaxIdle(20);
			config.setMaxTotal(max);
			config.setTestOnBorrow(false);
			config.setTestOnReturn(false);
			config.setTestOnCreate(false);
		    connectionPool = new JedisPool(config, host, port);
		}
	}
	
	private static Jedis getConnection() {
		if(!CONNECTION_POOL_INITED) {
			initConnectionPool("localhost", 6379, 10);
		}		
		if(connectionPool == null) return null;
		if(connectionPool.isClosed()) return null;
		return connectionPool.getResource();
	}

	public static void destroy() {
		if(connectionPool == null) return;
		if(connectionPool.isClosed()) return;
		connectionPool.close();
		connectionPool.destroy();
	}
	
	/**Clear all cached data
	 * @param cache: -1 Clear all
	 * */
	public static void clearCache(int cache) {
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			if(cache >= 0) {
				connection.select(cache);
				connection.flushDB();
			}else {
				connection.flushAll();
			}
		}
	}
	
	public static void clearCaches(int[] caches) {
		if(caches == null || caches.length == 0) return;
		try(Jedis connection = getConnection()){
			for(int cache : caches) {
				if(cache >= 0) {
					connection.select(cache);
					connection.flushDB();
				}
			}
		}
	}
	
	public static long size(int cache) {
		try(Jedis connection = getConnection()){
			if(connection == null) return 0;
			connection.select(cache);
			return connection.dbSize();
		}
	}
	
	public static Set<String> searchKeys(int cache, String keyPattern){
		if(keyPattern == null) return null;
		try(Jedis connection = getConnection()){
			if(connection == null) return null;
			connection.select(cache);
			return connection.keys(keyPattern);
		}
	}
	
	/**
	 * Remove the cached content(map, list, set or string) of the given KEY.
	 */
	public static void remove(int cache, String key) {
		if(key == null) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			if(connection.exists(key)) {
				connection.del(key);
			}
		}
	}
	
	//---------Single Value (String and Object)-------------------------------------/
	/**
	 * Save or replace the value of the key
	 * */
	public static boolean saveString(int cache, String key, String value, int ttl) {
		if(key == null || value == null) return false;
		try(Jedis connection = getConnection()){
			if(connection == null) return false;
			connection.select(cache);
			connection.set(key, value);
			if(ttl > 0) connection.expire(key, ttl);
			return true;
		}
	}
	
	public static String getString(int cache, String key) {
		if(key == null) return null;
		try(Jedis connection = getConnection()){
			if(connection == null) return null;
			connection.select(cache);
			return connection.get(key);
		}
	}
	
	public static void removeString(int cache, String key) {
		if(key == null) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			if(connection.exists(key)) {
				connection.del(key);
			}
		}
	}
	
	/**
	 * The method is implemented via pipeline because MDEL is not supported 
	 */
	public static void removeStrings(int cache, List<String> keys) {
		if(keys == null || keys.isEmpty()) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			try(var pipeLine = connection.pipelined()){
				keys.forEach(key->pipeLine.del(key));
				pipeLine.sync();
			}
		}
	}
	
	/**
	 * Save or replace the value of the key
	 * */
	public static void saveObject(int cache, String key, Object object, int ttl) {
		if(key == null || object == null) return;
		String value = serialize(object);
		if(Empty.is(value)) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			connection.set(key, value);
			if(ttl > 0) connection.expire(key, ttl);
		}
	}
	
	public static<T> T getObject(int cache, String key, Class<T> t) {
		if(key == null || t == null) return null;
		try(Jedis connection = getConnection()){
			if(connection == null) return null;
			connection.select(cache);
			var obj = connection.get(key);
			return deserialize(obj, t, true);
		}
	}
	
	public static void removeObject(int cache, String key) {
		if(key == null) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			if(connection.exists(key)) connection.del(key);
		}
	}
	
	//-------------------------------Map (Strings and Objects)--------------------------------/
	
	/**
	 * Map<String, String>
	 */
	public static void saveMap(int cache, String key, Map<String, String> value, int ttl) {
		if(key == null || value == null || value.isEmpty()) return;
			try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			connection.hmset(key, value);
			if(ttl > 0) connection.expire(key, ttl);
		}
	}
	
	/**
	 * Save or Replace
	 * */
	public static<T> void saveObjectMap(int cache, String key, Map<String, T> value, int ttl) {
		if(key == null || value == null || value.isEmpty()) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			var val = new HashMap<String, String>();
			for(var entry: value.entrySet()) {
				val.put(entry.getKey(), serialize(entry.getValue()));
			}
			connection.hmset(key, val);
			if(ttl > 0) connection.expire(key, ttl);
		}
	}
	
	/**
	 * O(N)?
	 * */
	public static Map<String, String> getMap(int cache, String key){
		var result = new HashMap<String, String>();
		if(key == null) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			var values = connection.hgetAll(key);
			if(values == null || values.isEmpty()) return result;
			for(var val : values.entrySet()) {
				if(!Empty.is(val.getValue())) {
					result.put(val.getKey(), val.getValue());
				}
			}
			return result;
		}
	}
	
	/**
	 * O(N)?
	 * */
	public static<T> Map<String, T> getMap(int cache, String key, Class<T> t){
		var result = new HashMap<String, T>();
		if(key == null) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			var values = connection.hgetAll(key);
			if(values == null || values.isEmpty()) return result;
			for(var val : values.entrySet()) {
				if(!Empty.is(val.getValue())) {
					result.put(val.getKey(), deserialize(val.getValue(), t));
				}
			}
			return result;
		}
	}
	
	/**
	 * Remove the entire map, "key" is the map in the cache
	 * */
	public static void removeMap(int cache, String key) {
		if(key == null) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			if(connection.exists(key)) {
				connection.del(key);
			}
		}
	}
	
	/**
	 * Remove an item (the given key) from the specific map
	 * */
	public static void removeMapItem(int cache, String map, String key) {
		if(map == null || key == null) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			if(connection.hexists(map, key)) {
				connection.hdel(map, key);
			}
		}
	}
	
	public static String getMapItem(int cache, String key, String field) {
		if(key == null || field == null) return "";
		try(Jedis connection = getConnection()){
			if(connection == null) return "";
			connection.select(cache);
			var result = connection.hget(key, field);
			return result == null ? "" : result;
		}
	}
	
	public static<T> T getMapItem(int cache, String key, String field, Class<T> t) {
		if(key == null || field == null) return null;
		try(Jedis connection = getConnection()){
			if(connection == null) return null;
			connection.select(cache);
			var result = connection.hget(key, field);
			return Empty.is(result) ? null : deserialize(result, t);
		}
	}
	
	/**
	 * @param value If you pass NULL, the field will be removed from REDIS
	 * If the field exists, replace it. Otherwise, save the object using the field 
	 */
	public static void setMapItem(int cache, String key, String field, String value) {
		if(key == null || field == null) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			if(value == null) {
				connection.hdel(key, field);
			}else {
				connection.hset(key, field, value);
			}
		}
	}
	
	/**
	 * @param value If you pass NULL, the field will be removed from REDIS<br> 
	 * If the field exists, replace it. Otherwise, save the object using the field
	 */
	public static<T> void setMapItem(int cache, String key, String field, T object) {
		if(key == null || field == null) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			if(object == null) {
				connection.hdel(key, field);
			}else {
				connection.hset(key, field, serialize(object));
			}
		}
	}
	
	//---------List (Strings and Objects)-------------------------------------------------/
	
	public static void saveList(int cache, String key, List<String> value, int ttl) {
		if(key == null || Empty.is(value)) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			if(connection.exists(key)) connection.del(key);
			connection.lpush(key, value.toArray(new String[] {}));
			if(ttl > 0) connection.expire(key, ttl);
		}
	}
	
	public static<T> void saveObjectList(int cache, String key, List<T> value, int ttl) {
		if(key == null || value == null || value.isEmpty()) return;
		var strValue = new ArrayList<String>(32);
		for(T v : value) strValue.add(serialize(v));
		saveList(cache, key, strValue, ttl);
	}
	
	public static List<String> getList(int cache, String key){
		var result = new ArrayList<String>();
		if(key == null) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			return connection.lrange(key, 0, -1);
		}
	}
	
	public static<T> List<T> getList(int cache, String key, Class<T> t){
		var result = new ArrayList<T>();
		if(key == null) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			var tmp = connection.lrange(key, 0, -1);
			if(tmp != null && !tmp.isEmpty()) {
				for(String obj : tmp) {result.add(deserialize(obj, t));}
			}
			return result;
		}
	}
	
	public static void appendToList(int cache, String key, String value) {
		if(key == null || Empty.is(value)) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			connection.lpush(key, value);
		}
	}
	
	public static<T> void appendToList(int cache, String key, T value) {
		if(key == null || value == null) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			connection.lpush(key, serialize(value));
		}
	}
	
	//------------Set API (It is very suitable to build index)--------------------------/
	
	public static void saveSet(int cache, String key, List<String> elements, int ttl) {
		if(key == null || Empty.is(elements)) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			if(connection.exists(key)) connection.del(key);
			connection.sadd(key, elements.toArray(new String[0]));
		}
	}
	
	public static<T> void saveObjectSet(int cache, String key, List<T> elements, int ttl) {
		if(key == null || Empty.is(elements)) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			if(connection.exists(key)) connection.del(key);
			int length = elements.size();
			var result = new String[length];
			for(int i = 0; i < length; i++) {
				result[i] = serialize(elements.get(i));
			}
			connection.sadd(key, result);
		}
	}
	
	public static Set<String> getAsSet(int cache, String key) {
		Set<String> result = new HashSet<String>();
		if(cache < 0 || key == null) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			result = connection.smembers(key);
			return result != null ? result : new HashSet<String>();
		}
	}
	
	public static<T> List<T> getAsSet(int cache, String key, Class<T> t) {
		if(cache < 0 || key == null) return new ArrayList<T>();
		try(Jedis connection = getConnection()){
			if(connection == null) return new ArrayList<T>();
			connection.select(cache);
			var items = connection.smembers(key);
			if(items == null || items.isEmpty()) return new ArrayList<T>();
			var result = new ArrayList<T>(items.size());
			items.forEach(item->result.add(deserialize(item, t)));
			return result;
		}
	}
	
	/**
	 * Append or Update
	 * */
	public static void setToSet(int cache, String key, String value) {
		if(key == null || Empty.is(value)) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			connection.sadd(key, value);
		}
	}
	
	/**
	 * Append or Update
	 * @param value How do we detect the value duplicating? TODO
	 * */
	public static<T> void setToSet(int cache, String key, T value) {
		if(key == null || value == null) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			connection.sadd(key, serialize(value));
		}
	}
	
	public static void removeSetItem(int cache, String key, String value) {
		if(key == null || Empty.is(value)) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			connection.srem(key, value);
		}
	}
	
	public static<T> void removeSetItem(int cache, String key, T value) {
		if(key == null || value == null) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			connection.srem(key, serialize(value));
		}
	}
	
	public static QueryResult<String> getSetItems(int cache, String key, String pattern, String start){
		if(key == null) return new QueryResult<String>();
		try(Jedis connection = getConnection()){
			if(connection == null) return new QueryResult<String>();
			connection.select(cache);
			var param = new ScanParams().count(200);
			if(!Empty.is(pattern)) param.match(pattern);
			var result = connection.sscan(key, start, param);
			return new QueryResult<String>(result.getCursor(), result.getResult());
		}
	}
	
	public static<T> QueryResult<T> getSetItems(int cache, String key, String pattern, String start, int size, Class<T> t){
		if(key == null) return new QueryResult<T>();
		try(Jedis connection = getConnection()){
			if(connection == null) return new QueryResult<T>();
			connection.select(cache);
			var param = new ScanParams().count(size);
			if(!Empty.is(pattern)) param.match(pattern);
			var result = connection.sscan(key, start, param);
			var objects = new ArrayList<T>();
			if(result.getResult() != null && !result.getResult().isEmpty()) {
				result.getResult().forEach(obj->objects.add(deserialize(obj, t)));
			}
			return new QueryResult<T>(result.getCursor(), objects);
		}
	}
	
	//-------------BATCH API---------------------------------------------------------/
	
	/**
	 * Implemented batch operations via pipeline.<p>
	 * Actually, the performance of the command MSET is a bit better than pipeline,
	 * but the implementation is too ugly(MUST check non-null one by one for each item) 
	 */
	public static void saveStrings(int cache, Map<String, String> values) {
		if(values == null || values.isEmpty()) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			try(var pipeLine = connection.pipelined()){
				for(var val : values.entrySet()) {
					if(val.getValue() == null) continue;
					pipeLine.set(val.getKey(), val.getValue());
				}
				pipeLine.sync();
			}
		}
	}
	
	/**
	 * Implemented via the command MGET<p>
	 * IMPORTANT:<br> 
	 * 1. You must guarantee the collection of keys does not contain NULL strings<br>
	 * 2. To ensure the result order is same to the input keys, NULL string is allowed. 
	 */
	public static List<String> getStrings(int cache, List<String> keys){
		List<String> result = new ArrayList<>();
		if(keys == null || keys.isEmpty()) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			var target = new String[keys.size()];
			result = connection.mget(keys.toArray(target));
			return result != null ? result : new ArrayList<>();
		}
	}
	
	/**
	 * Implemented batch operations via pipeline.<p>
	 * Actually, the performance of the command MSET is a bit better than pipeline,
	 * but the implementation is too ugly(MUST check non-null one by one for each item) 
	 */
	public static<T> void saveObjects(int cache, Map<String, T> values) {
		if(values == null || values.isEmpty()) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(cache);
			try(var pipeLine = connection.pipelined()){
				for(var item : values.entrySet()) {
					var val = serialize(item.getValue());
					if(val != null) pipeLine.set(item.getKey(), val);
				}
				pipeLine.sync();
			}
		}
	}
	
	/**
	 * Implemented via the command MGET<p>
	 * IMPORTANT:<br> 
	 * 1. You must guarantee the collection of keys does not contain NULL strings<br>
	 * 2. To ensure the result order is same to the input keys, NULL object is allowed. 
	 */
	public static<T> List<T> getObjects(int cache, List<String> keys, Class<T> t){
		var result = new ArrayList<T>();
		if(keys == null || keys.isEmpty()) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			var target = new String[keys.size()];
			var resultSet = connection.mget(keys.toArray(target));
			if(Empty.is(resultSet)) return result;
			for(var res : resultSet) {
				result.add(deserialize(res, t, true));
			}
			return result;
		}
	}
	
	/**
	 * Returns multiple fields from ONE map and it's implemented via the command HMGET
	 */
	public static List<String> getMapItems(int cache, String key, String... fields) {
		List<String> result = new ArrayList<>();
		if(key == null || fields == null || fields.length == 0) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			result = connection.hmget(key, fields);
			return result != null ? result : new ArrayList<>();
		}
	}
	
	/**
	 * Returns multiple fields from multiple maps and it's implemented via pipeline
	 */
	public static List<String> getMapItems(int cache, Map<String, String> kvs) {
		var result = new ArrayList<String>();
		if(kvs == null || kvs.isEmpty()) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			try(var pipeLine = connection.pipelined()){
				var tmp = new ArrayList<Response<String>>();
				for(var kv : kvs.entrySet()) {
					tmp.add(pipeLine.hget(kv.getKey(), kv.getValue()));
				}
				pipeLine.sync();
				for(var res : tmp) {
					String retval = res.get();
					result.add(retval != null ? retval : "");
				}
			}
			return result;
		}
	}
	
	/**
	 * Returns multiple fields from ONE map and it's implemented via the command HMGET
	 */
	public static<T> List<T> getMapObjects(int cache, String key, Class<T> t, String... fields) {
		var result = new ArrayList<T>();
		if(key == null || fields == null || fields.length == 0) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			var resultSet = connection.hmget(key, fields);
			if(Empty.is(resultSet)) return result;
			for(var res : resultSet) {
				result.add(deserialize(res, t));
			}
			return result;
		}
	}
	
	/**
	 * Returns multiple fields from multiple maps and it's implemented via pipeline
	 */
	public static<T> List<T> getMapObjects(int cache, Map<String, String> kvs, Class<T> t) {
		var result = new ArrayList<T>();
		if(kvs == null || kvs.isEmpty()) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			try(var pipeLine = connection.pipelined()){
				var tmp = new ArrayList<Response<String>>();
				for(var kv : kvs.entrySet()) {
					tmp.add(pipeLine.hget(kv.getKey(), kv.getValue()));
				}
				pipeLine.sync();
				for(var res : tmp) {
					result.add(deserialize(res.get(), t));
				}
			}
			return result;
		}
	}
	
	public static<T> List<T> getMapObjects(int cache, String key, List<String> fields, Class<T> t) {
		var result = new ArrayList<T>();
		if(key == null || Empty.is(fields)) return result;
		try(Jedis connection = getConnection()){
			if(connection == null) return result;
			connection.select(cache);
			var target = new String[fields.size()];
			target = fields.toArray(target);
			var resultSet = connection.hmget(key, target);
			if(Empty.is(resultSet)) return result;
			for(var res : resultSet) {
				result.add(deserialize(res, t));
			}
			return result;
		}
	}
	
	//------------ Message Queue API-------------------------------------------------/
	//We implemented 2 kinds of message queue: P2P and PUB/SUB
	
	//The first: FIFO P2P MESSAGE QUEUE(The following 4 *Message methods)
	
	public static void produceMessage(QueuedMessage message) {
		if(message == null) return;
		if(Empty.is(message.getChannel())) return;
		if(Empty.is(message.getMessage())) return;
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			connection.select(CACHE_MSGQUEUE);
			connection.lpush(message.getChannel(), message.getMessage());
		}
	}	
	
	public static<T> T consumeMessage(String channel, Class<T> t) {
		if(channel == null || t == null) return null;
		try(Jedis connection = getConnection()){
			if(connection == null) return null;
			connection.select(CACHE_MSGQUEUE);
			var obj = connection.brpop(1000, channel);
			if(Empty.is(obj)) return null;
			return deserialize(obj.get(0), t);
		}
	}
	
	public static QueuedMessage consumeMessage(String channel) {
		if(channel == null) return null;
		try(Jedis connection = getConnection()){
			if(connection == null) return null;
			connection.select(CACHE_MSGQUEUE);
			var obj = connection.brpop(1000, channel);
			if(Empty.is(obj)) return null;
			return new QueuedMessage(channel, obj.get(0));
		}
	}
	
	public static long queuedMessages(String channel) {
		if(Empty.is(channel)) return size(CACHE_MSGQUEUE);
		return Cacher.getList(CACHE_MSGQUEUE, channel).size();
	}
	
	//The 2nd: PUB/SUB message queue (The below 2 methods)
	public static void publish(String channel, Object message) {
		try(Jedis connection = getConnection()){
			if(connection == null) return;
			var msg = serialize(message);
			connection.publish(channel, msg);
		}
	}
	
	/**
	 * The method supports automatic reconnecting
	 */
	public static void subscribe(String channel, JedisPubSub subscriber) {
		for(;;) {
			try { //Auto release resource while exception
				try(Jedis connection = getConnection()){
					if(connection == null) return;
					connection.subscribe(subscriber, channel);
				}
			}catch(JedisConnectionException e) {
				System.out.println("Connection is broken. Reconnect after 10s");
				Time.sleep(10000); //Sleeping 10 seconds then reconnecting again.
			}
		}
	}
	
	//----------------Helper Methods-----------------------------------------------/
	public static String serialize(Object object) {
		if(object == null) return null;
		var result = Codec.toJsonCompact(object);
		return result;
	}
	
	/**
	 * The method returns an object according the given parameter <b>t</b>.<br>
	 * If the parameter <b>t</b> is illegal or an exception is threw, an empty object is returned.
	 * */
	public static<T> T deserialize(String json, Class<T> t) {
		if(Empty.is(json)) return emptyObject(t);
		try {
			return Codec.decodeJson(json, t);
		}catch(Exception e) {
			return emptyObject(t);
		}
	}
	
	/**
	 * The method returns an object according the given parameter <b>t</b>.<br>
	 * @param allowNull An empty object is returned if it's true. Otherwise, returns NULL
	 * */
	public static<T> T deserialize(String json, Class<T> t, boolean allowNull) {
		if(Empty.is(json)) {
			return allowNull ? emptyObject(t) : null;
		}
		try {
			return Codec.decodeJson(json, t);
		}catch(Exception e) {
			return allowNull ? emptyObject(t) : null;
		}
	}
	
	private static<T> T emptyObject(Class<T> t){
		try {
			return t.getDeclaredConstructor().newInstance();
		}catch(Exception e) { return null;}
	}
	
	public static class QueryResult<T>{
		private List<T> result = null;
		private boolean firstime = true;
		private String cursor = ScanParams.SCAN_POINTER_START;
		public QueryResult(){}
		
		public QueryResult(String cursor, List<T> result){
			this.cursor = cursor;
			this.result = result;
			this.firstime = false;
		}
		
		public boolean more() {
			if(firstime) return true;
			return !"0".equals(cursor);
		}
		
		public String getCursor() {
			return cursor;
		}
		public void setCursor(String cursor) {
			this.cursor = cursor;
		}
		public List<T> getResult() {
			return result != null ? result : new ArrayList<T>();
		}
		public void setResult(List<T> result) {
			this.result = result;
		}
	}
}

class QueuedMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String channel;
	private String message;
	
	public QueuedMessage(String channel, Object msg) {
		this.channel = channel;
		if(msg == null) return;
		message = Codec.toJson(msg);
	}
	
	public QueuedMessage(String channel, String msg) {
		this.channel = channel;
		this.message = msg;
	}
	
	public<T> T trans(Class<T> t) {
		if(t == null) return null;
		if(Empty.is(message)) return null;
		return Codec.decodeJson(message, t);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}