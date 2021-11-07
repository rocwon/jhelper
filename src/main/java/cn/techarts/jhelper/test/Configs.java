package cn.techarts.jhelper.test;

public interface Configs {
	public static final int TCP_PORT=3724;
	public static final int WEB_PORT=3742;
	public static final int MQT_PORT=1883;
	public static final String ENDIAN="hbf";
	public static final boolean EPOLL = false;
	
	public static final String USER = "root";
	public static final String TOKEN = "123456";
	//public static final String TOKEN = "rcs$2021&niu";
	public static final String DRIVER = "org.mariadb.jdbc.MariaDbDataSource";
	public static final String URL = "jdbc:mariadb://localhost:3306/test";
	
	
}
