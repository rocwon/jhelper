package cn.techarts.jhelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * A lightweight utility that's designed to access database.<br> 
 * It's based on APACHE DBUTILS and HIKARI connection pool.<p>
 * 
 * Persister supports 3 ways as bellow: <br>
 * 1. Raw JDBC style: {@link getDataSource}, {@link getConnection}<br>
 * 2. Apache DbUtils style: 
 *    {@link update}, {@link get}, {@link getAll} and {@link getQueryRunner}<br>
 * 3. Name-parameter style: The placeholder in SQL is compatible to MyBatis 
 */
public final class Persister {
	private HikariDataSource dataSource = null;
	private Map<Integer, SqlMeta> cachedStatements = null;
	
	public Persister(String driver, String url, String user, String password, int maxPoolSize) {
		cachedStatements = new HashMap<>(512);
		int poolsize = maxPoolSize <= 0 ? 10 : maxPoolSize;
		this.prepareDataSource(driver, url, user, password, poolsize);
	}
	
	public void close() {
		if(dataSource == null) {
			this.dataSource.close();
		}
	}
	
	private void prepareDataSource(String driver, String url, String user, String token, int maxPoolSize) {
		var config = new HikariConfig();
		if(driver.contains("Driver")) {
			config.setJdbcUrl(url);
			config.setUsername(user);
			config.setPassword(token);
			config.setDriverClassName(driver);
		}else {
			config.setDataSourceClassName(driver);
			config.addDataSourceProperty("url", url);
			config.addDataSourceProperty("user", user);
			config.addDataSourceProperty("password", token);
		}		
		config.setMaximumPoolSize(maxPoolSize);
		dataSource = new HikariDataSource(config);
	}
	
	/**
	 * @return Returns an instance of {@link QueryRunner} with  data-source
	 */
	public QueryRunner getQueryRunner() {
		if(dataSource == null) return null;
		return new QueryRunner(this.dataSource);
	}
	
	/**
	 *@return Returns the pooled data source 
	 */
	public DataSource getDataSource() {
		return this.dataSource;
	}
	
	public Connection getConnection() {
		try {
			return this.dataSource.getConnection();
		}catch(SQLException e) {
			throw new RuntimeException("Failed to get connection from datasource.", e);
		}
	}
	
	/**
	 * Save the data into database table
	 * @return Returns the auto-increment key
	 * @param sql The named SQL string
	 * @param params A java bean stores SQL parameters
	 * @param returnKey true tells the method to return the auto-increment key 
	 */
	public long save(String sql, Object params, boolean returnKey){
		if(!returnKey) {
			return executeUpdateWithNamedParameters(sql, params);
		}else {
			return executeInsertWithNamedParameters(sql, params);
		}
	}
	
	/**
	 * Remove data-rows from database table according to the given conditions
	 * @param sql The named SQL string
	 * @param params A java bean stores SQL parameters 
	 * 
	 * */
	public int remove(String sql, Object params){
		return executeUpdateWithNamedParameters(sql, params);
	}
	
	/**
	 * Modify the data-rows in database table according to the given conditions
	 * @param sql The named SQL string
	 * @param params A java bean stores SQL parameters 
	 */	
	public int modify(String sql, Object params){
		return executeUpdateWithNamedParameters(sql, params);
	}
	
	/**
	 * Execute batch of operations of INSERT, UPDATE or DELETE
	 */
	public int batch(String sql, List<Object> params) {
		var meta = this.parseStatement(sql);
		int d0 = params.size(), d1 = meta.count();
		try {
			var args = new Object[d0][d1];
			for(int i = 0; i < d0; i++) {
				args[i] = meta.toParameters(params.get(i));
			}
			this.getQueryRunner().batch(meta.getSql(), args);
			return 0;			
		}catch(SQLException e) {
			throw new RuntimeException("Failed to execute the sql: " + sql, e);
		}
	}
	
	/**
	 * Retrieve a data-row from database table and convert to your specified type according to the given conditions.
	 * @param sql The named SQL string
	 * @param params A java bean stores SQL parameters 
	 */
	public<T> T find(String sql, Object params, Class<T> classOfResult){
		if(classOfResult == null) return null;
		var meta = this.parseStatement(sql);
		if(meta == null || !meta.check()) {
			throw new RuntimeException("Could not find the sql:" + sql);
		}
		var target = new BeanHandler<T>(classOfResult);
		try {
			if(!meta.hasArgs()) {
				return this.getQueryRunner().query(meta.getSql(), target);
			}else {
				var args = meta.toParameters(params);
				return this.getQueryRunner().query(meta.getSql(), target, args);
			}			
		}catch(SQLException e) {
			throw new RuntimeException("Failed to execute the sql: " + sql, e);
		}
	}
	
	/**
	 * Retrieve all data-rows what matched the given conditions and convert to your specified type
	 * @param sql The named SQL string
	 * @param params A java bean stores SQL parameters 
	 */
	public<T> List<T> findAll(String sql, Object params, Class<T> classOfResult){
		if(classOfResult == null) return null;
		var meta = this.parseStatement(sql);
		var target = new BeanListHandler<T>(classOfResult);
		try {
			if(!meta.hasArgs()) {
				return getQueryRunner().query(meta.getSql(), target);
			}else {
				var args = meta.toParameters(params);
				return this.getQueryRunner().query(meta.getSql(), target, args);
			}	
		}catch(SQLException e) {
			throw new RuntimeException("Failed to execute the sql: " + sql, e);
		}
	}
	
	//----------------------------------------------------------------------------------------------
	
	/**
	 * The method is designed to handle the INSERT, UPDATE, DELETE statements.<br> 
	 * It is a wrapper of DbUtils raw API and does not support named parameter.
	 */
	public int update(String sql, Object... params){
		try {
			if(!hasParameters(params)) {
				getQueryRunner().update(sql);
			}else {
				getQueryRunner().update(sql, params);
			}	
			return 0;
		}catch(SQLException e) {
			throw new RuntimeException("Failed to execute the sql: " + sql, e);
		}
	}
	
	/**
	 * @return Returns the matched data-row and wraps as the specified class.<p>
	 *  It is a wrapper of DbUtils API <code>QueryRunner.query</code> and does not support named parameter.
	 */
	public<T> T get(String sql, Class<T> classOfTarget, Object... params){
		if(classOfTarget == null) return null;
		try {
			var target = new BeanHandler<T>(classOfTarget);
			if(!hasParameters(params)) {
				return getQueryRunner().query(sql,target);
			}else {
				return getQueryRunner().query(sql, target, params);
			}			
		}catch(SQLException e) {
			throw new RuntimeException("Failed to execute the sql: " + sql, e);
		}
	}
	
	/**
	 * @return Returns all of matched data-rows and wraps as the specified class.<p>
	 *  It is a wrapper of DbUtils API <code>QueryRunner.query</code> and does not support named parameter.
	 */
	public<T> List<T> getAll(String sql, Class<T> classOfTarget, Object... params){
		if(sql == null || classOfTarget == null) return null;
		try {
			var target = new BeanListHandler<T>(classOfTarget);
			if(!hasParameters(params)) {
				return getQueryRunner().query(sql,target);
			}else {
				return getQueryRunner().query(sql, target, params);
			}	
		}catch(SQLException e) {
			throw new RuntimeException("Failed to execute the sql: " + sql, e);
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------
	
	private static boolean hasParameters(Object ... params) {
		if(params == null) return false;
		if(params.length == 0) return false;
		return !(params.length == 1 && params[0] == null);
	}
	
	/**
	 * Named Parameters
	 */
	private int executeUpdateWithNamedParameters(String sql, Object params){
		var meta = this.parseStatement(sql);
		try {
			if(!meta.hasArgs()) {
				return getQueryRunner().update(meta.getSql());
			}else {
				var args = meta.toParameters(params);
				return getQueryRunner().update(meta.getSql(), args);
			}
		}catch(SQLException e) {
			throw new RuntimeException("Failed to execute the sql: " + sql, e);
		}
	}
	
	private long executeInsertWithNamedParameters(String sql, Object params){
		Long result = null; //Auto-Increment ID
		var meta = this.parseStatement(sql);
		var rsh = new ScalarHandler<Long>();
		try {
			if(!meta.hasArgs()) {
				result = getQueryRunner().insert(meta.getSql(), rsh);
			}else {
				var args = meta.toParameters(params);
				result = getQueryRunner().insert(meta.getSql(), rsh, args);
			}
			return result != null ? result.longValue() : 0L;
		}catch(SQLException e) {
			throw new RuntimeException("Failed to execute the sql: " + sql, e);
		}
	}
	
	private SqlMeta parseStatement(String sql) {
		if(Empty.is(sql)) {
			throw new RuntimeException("The parameter sql is required!");
		}
		var key = Integer.valueOf(sql.hashCode());
		var result = this.cachedStatements.get(key);
		if(result == null) {
			result = parseNamedParameters(sql);
			if(result != null) cachedStatements.put(key, result);
		}
		if(result == null || !result.check()) {
			throw new RuntimeException("Could not find the sql: " + sql);
		}
		return result;
	}
	
	private static SqlMeta parseNamedParameters(String sql) {
		if(Empty.is(sql)) return null;
		var chars = sql.toCharArray();
		var length = chars.length;
		var matched = false;
		var param = new StringBuilder(24);
		var stmt = new StringBuilder(256);
		var params = new ArrayList<String>();
		for(int i = 0; i < length; i++) {
			char ch = chars[i];
			if(matched) {
				if(ch != '}') {
					param.append(ch);
				}else { //End of the parameter
					matched = false;
					params.add(param.toString());
					param = new StringBuilder(24);
				}
			}else {
				stmt.append(ch);
				if(ch != '#') continue;
				if(chars[i++ + 1] != '{') continue;
				stmt.setCharAt(stmt.length() - 1, '?');
				matched = true; //Start of a parameter
			}
		}
		return new SqlMeta(stmt.toString(), params);
	}
}

final class SqlMeta{
	private String sql;
	private List<String> args;
	
	public SqlMeta(String statement, List<String> params) {
		this.setArgs(params);
		this.setSql(statement);
	}
	
	public boolean check() {
		return !Empty.is(sql);
	}
	
	/**
	 * The parameters number count
	 */
	public int count() {
		return args != null ? args.size() : 0;
	}
	
	public boolean hasArgs() {
		return !Empty.is(args);
	}	
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}
	
	public Object[] toParameters(Object arg) {
		int count = this.count();
		if(arg == null || count == 0) return null;
		if(count == 1) {//1 parameter & Primitive Type
			if(arg instanceof Number) return new Object[] {arg};
			if(arg instanceof String) return new Object[] {arg};
			if(arg instanceof Boolean) return new Object[] {arg};
			if(arg instanceof Character) return new Object[] {arg};
		}
		var result = new Object[count];
		for(int i = 0; i < result.length; i++) {
			result[i] = Reflector.getValue(arg, args.get(i));
		}
		return result;
	}
}