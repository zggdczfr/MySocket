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
 *  这是一个黑名单
 * </pre>
 * 
 */

public class BansDAO
{
	private Connection con = null;
	private PreparedStatement ps = null;
	
	
	/**
	 * 
	 * 这是一个关闭连接数据库资源的方法
	 * 
	 */
	private void daoClose() {
		try {
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
	 * 这是一个添加黑名单的方法
	 * @param username 用户名
	 * @param b_username 黑名单
	 * @returj
	 */
	public void addBanUser(String username,String b_username)
	{
		try
		{
			con = DBUIT.getConnection();
			String strSql = "insert into bans(username,b_username) value(?,?)";
			ps = con.prepareStatement(strSql);
			ps.setString(1, username);
			ps.setString(2, b_username);
			ps.executeUpdate();
			
		} catch (SQLException e){
			e.printStackTrace();
		}finally 
		{
			daoClose();
		}
	}
	
	
	/**
	 * 这是一个判断某个用户是否存在于另一个用户的黑名单中的方法
	 * @param username  操作该方法的用户
	 * @param b_username  被查询的用户
	 * @return 若存在于黑名单中返回0，否则返回1.
	 */
	public int isBan(String username, String b_username)
	{
		int result = 1;
		try
		{
			con = DBUIT.getConnection();
			String strSql = "select * from bans where username=? and b_username=?";
			ps = con.prepareStatement(strSql);
			ps.setString(1, username);
			ps.setString(2, b_username);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				result = 0;
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			daoClose();
		}
		return result;
	}
	
	/**
	 * 这是一个解除用户之间黑名单关系的方法
	 * @param username  操作解除方法的用户
	 * @param b_username  被解除的用户
	 */
	public void deleteBan(String username, String b_username)
	{
		try
		{
			con = DBUIT.getConnection();
			String strSql = "delete from bans where username=? and b_username=?";
			ps = con.prepareStatement(strSql);
			ps.setString(1, username);
			ps.setString(2, b_username);
			ps.executeUpdate();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			daoClose();
		}
	}

	//返回所有的黑名单
	/**
	 * 这是一个查询某用户的黑名单列表的方法
	 * @param username 查询的用户名
	 * @return  返回一个字符串 List 列表 
	 */
	public List<String> allBans(String username)
	{
		List<String> myAllBans = new ArrayList<String>();
		try
		{
			con = DBUIT.getConnection();
			String strSql = "select * from bans where username=?";
			ps = con.prepareStatement(strSql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs != null)
			{
				while(rs.next())
				{
					String ban = rs.getString("b_username");
					myAllBans.add(ban);
				}
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally 
		{
			daoClose();
		}
		
		return myAllBans;
	}
}
