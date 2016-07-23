package com.qg.fangrui.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author Administrator
 * <pre>
 * 这是一个连接数据库的公用方法
 * </pre>
 */

public class DBUIT
{
    /**
     * 获取数据库连接的静态方法
     * @return 返回一个连接数据库的connection对象
     */
	public static Connection getConnection()
	{
		Connection con = null;
		String data_username = "root";
		String data_password = "123456";
		String url = "jdbc:mysql://localhost:3306/mysocket?useUnicode=true&characterEncoding=utf-8";
		String driver = "com.mysql.jdbc.Driver";
		
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(url, data_username, data_password);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return con;
	}
	
	
	/**
	 * 关闭连接的静态方法
	 * @param con 连接数据库的connection对象
	 */
	public static void closeConnection(Connection con)
	{
		if(con != null)
		{
			try
			{
				con.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

}
