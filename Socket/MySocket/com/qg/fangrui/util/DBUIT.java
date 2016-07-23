package com.qg.fangrui.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author Administrator
 * <pre>
 * ����һ���������ݿ�Ĺ��÷���
 * </pre>
 */

public class DBUIT
{
    /**
     * ��ȡ���ݿ����ӵľ�̬����
     * @return ����һ���������ݿ��connection����
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
	 * �ر����ӵľ�̬����
	 * @param con �������ݿ��connection����
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
