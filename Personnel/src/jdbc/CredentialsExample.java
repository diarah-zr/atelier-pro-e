package jdbc;

public class CredentialsExample 
{
	private static String driver = "mysql";
	private static String driverClassName = "com.mysql.cj.jdbc.Driver";
	private static String host = "192.168.112.148";
	private static String port = "3306";
	private static String database = "personnel";
	private static String user = "personnel";
	private static String password = "personnel";
	
	static String getUrl()  
	{
		return "jdbc:" + driver + "://" + host + ":" + port + "/" + database ;
	}
	
	static String getDriverClassName()
	{
		return driverClassName;
	}
	
	static String getUser() 
	{
		return user;
	}

	static String getPassword() 
	{
		return password;
	}
}
