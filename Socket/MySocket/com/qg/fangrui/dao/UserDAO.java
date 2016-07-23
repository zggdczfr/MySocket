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
 * ����һ��ר�����ڴ����û����ϵ��� 
 * </pre>
 */

public class UserDAO
{
	private Connection con = null;
	private PreparedStatement ps = null;
	
	/**
	 * 
	 * ����һ���ر��������ݿ���Դ�ķ���
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
	 * ����һ�������ж��û��Ƿ���ڵķ���
	 * @param username  ��ѯ���û���
	 * @return ���û����ڷ���1�����򷵻�0
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
				i = 1;   //�ҵ��û���
			}
			else
			{
				i = 0;   //�Ҳ����û���
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
	 * ����һ�����ڴ����û����ϵķ���
	 * @param user �����û����ϵĶ���
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
	 * ����һ�����ڼ�¼�û��˳�����������ʱ��ķ���
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
	 * ����һ�������ж��û����Ƿ�ɹ���½�ķ���
	 * @param user ����û����ϵĶ���
	 * @return ����û�����������ȷƥ�䣬����1�����򷵻�0
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
			//�ж��û����������������������Ƿ�һ��
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
 	 * ����һ�����ڸ����û��˳����������һ����˳�����ķ���
 	 * @param username �û�
 	 * @param chart ģʽ����Ϊ ExitFromChat �͸����û��뿪���������ҵ�ʱ�䣻��������û��˳������ʱ��
 	 */
	public void updateExitTime(String username, String chart)
	{
		String name = null;
		if(chart.equals(Identifier.ExitFromChat))
		{
			name = "chats";    //���ô����¼�û��뿪���������ҵı�
		}
		else
		{
			name = "users";    //�����û��˳�����ı�
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
