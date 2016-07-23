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
 *  ����һ��������
 * </pre>
 * 
 */

public class BansDAO
{
	private Connection con = null;
	private PreparedStatement ps = null;
	
	
	/**
	 * 
	 * ����һ���ر��������ݿ���Դ�ķ���
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
	 * ����һ����Ӻ������ķ���
	 * @param username �û���
	 * @param b_username ������
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
	 * ����һ���ж�ĳ���û��Ƿ��������һ���û��ĺ������еķ���
	 * @param username  �����÷������û�
	 * @param b_username  ����ѯ���û�
	 * @return �������ں������з���0�����򷵻�1.
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
	 * ����һ������û�֮���������ϵ�ķ���
	 * @param username  ��������������û�
	 * @param b_username  ��������û�
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

	//�������еĺ�����
	/**
	 * ����һ����ѯĳ�û��ĺ������б�ķ���
	 * @param username ��ѯ���û���
	 * @return  ����һ���ַ��� List �б� 
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
