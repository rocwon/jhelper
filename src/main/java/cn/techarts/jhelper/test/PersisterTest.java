package cn.techarts.jhelper.test;

import org.junit.Before;
import org.junit.Test;

import cn.techarts.jhelper.Persister;
import junit.framework.TestCase;

public class PersisterTest {
	private static String driver = "org.mariadb.jdbc.MariaDbDataSource";
	private static String url = "jdbc:mariadb://localhost:3306/test?useUnicode=true&useSSL=false&generateSimpleParameterMetadata=true";
	
	private static String sql_insert0 = "insert into employee(name, age, gender, mobile) values(?,?,?,?)";
	
	private static String sql_insert = "insert into employee(name, age, gender, mobile) values(#{name}, #{age}, #{gender}, #{mobile})";
	private static String sql_update = "update employee set age=#{age}, mobile=#{mobile} where id=#{id}";
	private static String sql_delete = "delete from employee where id=#{id}";
	private static String sql_select = "select * from employee where id=#{id}";
	private static String sql_select0 = "select * from employee where id=?";
	private static String sql_select_all = "select * from employee where age > #{age}";
	
	private Persister persister = null;
	
	@Before
	public void init() {
		persister = new Persister(driver, url, "root", "123456", 0);
	}
	
	
	@Test
	public void testPersister() {
		var user = new Employee("Eric Clapton", 1, 62, "13988886666");
		persister.save(sql_insert, user, true);
		persister.update(sql_insert0, new Object[] {"John Denver", 1, 51, "13800001111"});
		
		var employee = persister.get(sql_select0, Employee.class, 1);
		TestCase.assertEquals("Eric Clapton", employee.getName());
		
		employee = persister.find(sql_select, 1, Employee.class);
		TestCase.assertEquals("Eric Clapton", employee.getName());
	}
}
