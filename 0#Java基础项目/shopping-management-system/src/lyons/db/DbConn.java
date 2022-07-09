package lyons.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

/**
 * 连接oracle数据库
 * @author lyons(zhanglei)
 */
//public final class DbConn
//{
//	public static  Connection getconn()
//	{
//		Connection conn = null;
//
//		String user   = "scott";
//		String passwd = "tiger";
//		String url = "jdbc:oracle:thin:@localhost:1521:orcl";//orcl为oracle数据库实例名字
//
//		//已加载完驱动
//		try
//		{
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			conn = DriverManager.getConnection(url,user,passwd);
//		}catch (SQLException e)
//		{
//			e.printStackTrace();
//		}
//		catch (ClassNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//		return conn;
//	}
//}

/**
 * 连接MySQL数据库
 */
public final class DbConn
{
	public static  Connection getconn()
	{
		Connection conn = null;

		String user   = "root";
		String passwd = "rootroot";
		String url = "jdbc:mysql://127.0.0.1:3306/sms_data1";//orcl为oracle数据库实例名字

		//已加载完驱动
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url,user,passwd);
		}catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return conn;
	}
}