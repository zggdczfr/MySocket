package com.qg.fangrui.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.qg.fangrui.entity.Message;
import com.qg.fangrui.util.DBUIT;

/**
 * 
 * @author Administrator
 * <pre>
 * ����һ�����ڴ����û����͵���Ϣ��ϵͳ��Ϣ����
 * </pre>
 */

public class MessageDAO
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
	 * ����һ�����ڱ����û�������Ϣ�ķ���
	 * @param mes ������Ϣ�Ķ���
	 */
	public void saveMessage(Message mes)
	{
		try
		{
			con = DBUIT.getConnection();
			String strSql = "insert into messages(send,recipient,send_time,content) value(?,?,?,?)";
			ps = con.prepareStatement(strSql);
			
			ps.setString(1, mes.getSend());
			ps.setString(2, mes.getRecipient());
			ps.setTimestamp(3, null);
			ps.setString(4, mes.getContent());
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
	 * ����һ�����ڻ���û�����ʱ��ķ���
	 * @param username �û�
	 * @param charts  ģʽ����all�����������û���һ���뿪���������ҵ�ʱ�䣻���������û��˳������ʱ��
	 * @return timestamp ���͵�ʱ��
	 */
	private Timestamp getExitTime(String username,String charts)
	{
		Timestamp exitTime = null;
		String name = null;
		if(charts.equals("all"))
		{
			//��õ��ǹ����û��˳������ʱ��
			name = "chats";
		}
		else
		{
			name = "users";
		}
		try
		{
			con = DBUIT.getConnection();
			String strSql = "select * from "+name+" where username=?";
			ps = con.prepareStatement(strSql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				exitTime = rs.getTimestamp("exit_time");
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally 
		{
			daoClose();
		}
		
		return exitTime;
	}
	
	/**
	 * ����һ������û���������Ϣ�ķ���
	 * @param username �û�
	 * @param model  ģʽ����all����ʾΪ����������������Ϣ��������ʾΪ˽����Ϣ����ϵͳ��ʾ��Ϣ��
	 * @return ����һ���ַ��� List �б�
	 */
	public List<String> offLineMessage(String username,String model)
	{
		String strSql = null;
		List<String> allMessages = new ArrayList<String>();
		//����û�����ʱ��
		Timestamp exit_time = getExitTime(username,model);
		System.out.println(exit_time);
		
		try
		{
			con = DBUIT.getConnection();
			strSql = "select * from messages where send_time>? and recipient=?";
			ps = con.prepareStatement(strSql);
			ps.setTimestamp(1, exit_time);
			ps.setString(2, model);
			ResultSet rs = ps.executeQuery();
			String message = null;
			
			if(rs != null)
			{
				while(rs.next())
				{
					String send = rs.getString("send");
					String recipiten = rs.getString("recipient");
					String send_time = rs.getTimestamp("send_time").toString();
					String content = rs.getString("content");
					//��ʱ����н�ȡ
					send_time = send_time.substring(0, 19);
					//��õ���Ϣ��ȫ����Ϣ
					if(recipiten.equals("all"))
					{
						message = send+"("+send_time+") ˵��"+content+"��";
						allMessages.add(message);
					}
					else 
					{
						message = send+"("+send_time+") ���ĵض���˵��"+content+"��";
						allMessages.add(message);
					}
				}
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			daoClose();
		}
		
		return allMessages;
	}

	/**
	 * ����һ������û�����������Ϣ�ķ���
	 * @param username �û�
	 * @return  ����һ���ַ��� List �б�
	 */
	public List<String> allPrivateChat(String username)
	{
		List<String> allPriChat = new ArrayList<String>();
		
		try
		{
			String message = null;
			con = DBUIT.getConnection();
			String strSql = "select * from messages where recipient=?";
			ps = con.prepareStatement(strSql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if(rs != null)
			{
				while(rs.next())
				{
					String send = rs.getString("send");
					String send_time = rs.getTimestamp("send_time").toString();
					String content = rs.getString("content");
					//��ʱ����н�ȡ
					send_time = send_time.substring(0, 19);
					
					message = send+"("+send_time+") ���ĵض���˵��"+content+"��";
					allPriChat.add(message);
				}
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally 
		{
			daoClose();
		}
		
		
		return allPriChat;
	}
}
