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
 * ���Ǻ��ѹ�ϵ�������
 * </pre>
 */

public class FriendDAO
{
	private Connection con = null;
	private PreparedStatement ps = null;
	
	/**
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
				con.close();
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ����һ�������û�˫����Ӻ��ѵķ���
	 * @param username  �����û�
	 * @param friend   ����ӵ��û�
	 */
	public void addFriend(String username, String friend)
	{
		try
		{
			con = DBUIT.getConnection();
			String strSql = "insert into friends(username,friend) values(?,?)";
			ps = con.prepareStatement(strSql);
			//������ӵ��û�����ڲ����û��ĺ����б�
			ps.setString(1, username);
			ps.setString(2, friend);
			ps.executeUpdate();
			//�������û�Ҳ����ڱ���ӵ��û��ĺ����б�
			ps.setString(1, friend);
			ps.setString(2, username);
			ps.executeUpdate();
			
			//ɾ�����ݿ��к�������
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
	 * ����һ�����ڴ����û���������ķ���
	 * @param applicant ����������û�
	 * @param approver  ����������û�
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
	 * ����һ������ɾ����������ķ���
	 * @param applicant ԭ������������û�
	 * @param approver  ԭ������������û�
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
	 * ����һ���ж�ĳ���û��Ƿ�������һ���û����ͺ�������ķ���
	 * @param applicant ԭ������������û�
	 * @param approver  ԭ������������û�
	 * @return �����ں������룬���� 1�����򷵻�0
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
	 * ����һ���ж��û�֮���Ƿ�Ϊ���ѹ�ϵ�ķ���
	 * @param applicant ���ѹ�ϵ�е�һ��
	 * @param approver  ���ѹ�ϵ�е�����һ��
	 * @return �����ߴ��ں��ѹ�ϵ������ 1�����򷵻�0
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
 	 * ����һ�����ڻ���û����к����б�ķ���
 	 * @param username ��ѯ�б���û�
 	 * @return ����һ���ַ��� List �б�
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
