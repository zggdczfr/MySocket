package com.qg.fangrui.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.qg.fangrui.entity.User;
import com.qg.fangrui.util.DBUIT;
import com.qg.fangrui.util.Identifier;

/**
 * 
 * @author Administrator
 * <pre>
 * 这是一个专门用于处理用户资料的类 
 * </pre>
 */

public class UserDAO
{
	private Connection con = null;
	private PreparedStatement ps = null;
	
	/**
	 * 
	 * 这是一个关闭连接数据库资源的方法
	 * 
	 */
	private void daoClose()
	{
		try
		{
			if(ps != null)
			{
				ps.close();
			}
			if(con != null)
			{
				DBUIT.closeConnection(con);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 这是一个用于判断用户是否存在的方法
	 * @param username  查询的用户名
	 * @return 若用户存在返回1，否则返回0
	 */
	public int isExist(String username)
	{
		int i = 1;
		try
		{
			con = DBUIT.getConnection();
			String strSql = "select * from users where username=?";
			ps = con.prepareStatement(strSql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				i = 1;   //找到用户名
			}
			else
			{
				i = 0;   //找不到用户名
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally 
		{
			daoClose();
		}
		
		return i;
	}
	
	
	/**
	 * 这是一个用于储存用户资料的方法
	 * @param user 储存用户资料的对象
	 */
	public void addUser(User user)
	{
		try
		{
			String styrSql = "insert into users(username,password) values(?,?)";
			con = DBUIT.getConnection();
			ps = con.prepareStatement(styrSql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.executeUpdate();
		} catch (Exception e)
		{
			e.printStackTrace();
		}finally 
		{
			daoClose();
		}
	}
	
	
	/**
	 * 这是一个用于记录用户退出公共聊天室时间的方法
	 * @param username
	 */
	public void addUserToChats(String username)
	{
		try
		{
			con = DBUIT.getConnection();
			String strSql = "insert into chats(username,exit_time) value(?,?)";
			ps = con.prepareStatement(strSql);
			ps.setString(1, username);
			ps.setTime(2, null);
			ps.executeUpdate();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally 
		{
			daoClose();
		}
	}
	
	
	
	/**
	 * 这是一个用于判断用户的是否成功登陆的方法
	 * @param user 存放用户资料的对象
	 * @return 如果用户名与密码正确匹配，返回1，否则返回0
	 */
 	public int isLogin(User user)
	{
		int i = 0;
		try
		{
			con = DBUIT.getConnection();
			String strSql = "select * from users where username=?";
			ps = con.prepareStatement(strSql);
			ps.setString(1, user.getUsername());
			ResultSet rs = ps.executeQuery();
			String real_password = null;
			if(rs.next())
			{
				real_password = rs.getString("password");
			}
			//判断用户输入密码与真正的密码是否一致
			if(real_password.equals(user.getPassword()))
			{
				i=1;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			daoClose();
		}
		
		return i;
	}
	
	
 	/**
 	 * 这是一个用于更新用户退出公共聊天室或者退出程序的方法
 	 * @param username 用户
 	 * @param chart 模式：若为 ExitFromChat 就更新用户离开公共聊天室的时间；否则更新用户退出程序的时间
 	 */
	public void updateExitTime(String username, String chart)
	{
		String name = null;
		if(chart.equals(Identifier.ExitFromChat))
		{
			name = "chats";    //调用储存记录用户离开公共聊天室的表
		}
		else
		{
			name = "users";    //调用用户退出程序的表
		}
		
		try
		{
			con = DBUIT.getConnection();
			String strSql = "update "+name+" set exit_time = ? where username = ? ";
			
			ps = con.prepareStatement(strSql);
			ps.setTimestamp(1, null);
			ps.setString(2, username);
			ps.executeUpdate();
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally 
		{
			daoClose();
		}
	}

}
