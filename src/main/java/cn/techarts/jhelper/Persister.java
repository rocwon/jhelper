package cn.techarts.jhelper;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * A lightweight utility that's designed to access database 
 * which based on APACHE DBUTILS and HIKARI connection pool.
 */
public final class Persister {
	private HikariDataSource dataSource = null;
	
	public Persister(String driver, String url, String user, String password) {
		this.prepareDataSource(driver, url, user, password);
	}
	
	public void close() {
		if(dataSource == null) {
			this.dataSource.close();
		}
	}
	
	private void prepareDataSource(String driver, String url, String user, String token) {
		var config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername(user);
		config.setPassword(token);
		//config.setDriverClassName(driver);
		config.setDataSourceClassName(driver);
		dataSource = new HikariDataSource(config);
	}
	
	public QueryRunner getExecutor() {
		if(dataSource == null) return null;
		return new QueryRunner(this.dataSource);
	}
	
	/**
	 * The method is designed to handle the INSERT, UPDATE, DELETE statements 
	 */
	public int update(String sql, Object... params) throws RuntimeException{
		if(sql == null) return -1;
		try {
			if(!hasParameters(params)) {
				getExecutor().update(sql);
			}else {
				getExecutor().update(sql, params);
			}	
			return 0;
		}catch(SQLException e) {
			throw new RuntimeException("Failed save data.", e);
		}
	}
	
	public<T> T get(String sql, Class<T> classOfTarget, Object... params) throws RuntimeException{
		if(sql == null || classOfTarget == null) return null;
		try {
			var target = new BeanHandler<T>(classOfTarget);
			if(!hasParameters(params)) {
				return getExecutor().query(sql,target);
			}else {
				return getExecutor().query(sql, target, params);
			}			
		}catch(SQLException e) {
			throw new RuntimeException("Failed to search data with SQL[" + sql + "]", e);
		}
	}
	
	public<T> List<T> getAll(String sql, Class<T> classOfTarget, Object... params)  throws RuntimeException{
		if(sql == null || classOfTarget == null) return null;
		try {
			var target = new BeanListHandler<T>(classOfTarget);
			if(!hasParameters(params)) {
				return getExecutor().query(sql,target);
			}else {
				return getExecutor().query(sql, target, params);
			}	
		}catch(SQLException e) {
			throw new RuntimeException("Failed to search data with SQL[" + sql + "]", e);
		}
	}
	
	private static boolean hasParameters(Object ... params) {
		if(params == null) return false;
		if(params.length == 0) return false;
		return !(params.length == 1 && params[0] == null);
	}
}