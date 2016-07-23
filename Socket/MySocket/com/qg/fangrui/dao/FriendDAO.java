package com.qg.fangrui.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qg.fangrui.util.DBUIT;

/**
 * 
 * @author Administrator
 * <pre>
 * 这是好友关系处理的类
 * </pre>
 */

public class FriendDAO
{
	private Connection con = null;
	private PreparedStatement ps = null;
	
	/**
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
				con.close();
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 这是一个用于用户双方添加好友的方法
	 * @param username  操作用户
	 * @param friend   被添加的用户
	 */
	public void addFriend(String username, String friend)
	{
		try
		{
			con = DBUIT.getConnection();
			String strSql = "insert into friends(username,friend) values(?,?)";
			ps = con.prepareStatement(strSql);
			//将被添加的用户添加于操作用户的好友列表
			ps.setString(1, username);
			ps.setString(2, friend);
			ps.executeUpdate();
			//将操作用户也添加于被添加的用户的好友列表
			ps.setString(1, friend);
			ps.setString(2, username);
			ps.executeUpdate();
			
			//删除数据库中好友申请
			deleteApply(friend, username);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally 
		{
			daoClose();
		}
		
	}
	
	/**
	 * 这是一个用于储存用户好友申请的方法
	 * @param applicant 发送申请的用户
	 * @param approver  接收申请的用户
	 */
	public void sendApply(String applicant, String approver)
	{
		try
		{
			con = DBUIT.getConnection();
			String strSql = "insert into temp_friends(username, friend) values(?,?)";
			ps = con.prepareStatement(strSql);
			ps.setString(1, applicant);
			ps.setString(2, approver);
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
	 * 这是一个用来删除好友申请的方法
	 * @param applicant 原本发送申请的用户
	 * @param approver  原本接收申请的用户
	 */
	private void deleteApply(String applicant, String approver)
	{
		try
		{
			con = DBUIT.getConnection();
			String strSql = "delete from temp_friends where username=? and friend=?";
			PreparedStatement ps = con.prepareStatement(strSql);
			ps.setString(1, applicant);
			ps.setString(2, approver);
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
	 * 这是一个判断某个用户是否向另外一个用户发送好友申请的方法
	 * @param applicant 原本发送申请的用户
	 * @param approver  原本接收申请的用户
	 * @return 若存在好友申请，返回 1；否则返回0
	 */
 	public int hasApply(String applicant, String approver)
	{
		int i = 0;
		try
		{
			con = DBUIT.getConnection();
			String strSql = "select * from temp_friends where username=? and friend=?";
			PreparedStatement ps = con.prepareStatement(strSql);
			ps.setString(1, applicant);
			ps.setString(2, approver);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				i = 1;
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
	 * 这是一个判断用户之间是否为好友关系的方法
	 * @param applicant 好友关系中的一方
	 * @param approver  好友关系中的另外一方
	 * @return 若两者存在好友关系，返回 1；否则返回0
	 */
 	public int isFriends(String applicant,String approver)
 	{
 		int i = 0;
 		String strSql = "select";
 		try
		{
 			con = DBUIT.getConnection();
			ps = con.prepareStatement(strSql);
			ps.setString(1, applicant);
			ps.setString(2, approver);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
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
 	 * 这是一个用于获得用户所有好友列表的方法
 	 * @param username 查询列表的用户
 	 * @return 返回一个字符串 List 列表
 	 */
	public List<String> getAllFriends(String username)
	{
		List<String> allFriends = new ArrayList<String>();
		String friend;
		try
		{
			con = DBUIT.getConnection();
			String strSql = "select friend from friends where username=?";
			ps = con.prepareStatement(strSql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if(rs != null)
			{
				while(rs.next())
				{
					friend = rs.getString("friend");
					allFriends.add(friend);
				}
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally 
		{
			daoClose();
		}
		
		return allFriends;
	}
	

}
