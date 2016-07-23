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
 * 这是一个关于处理用户发送的信息和系统信息的类
 * </pre>
 */

public class MessageDAO
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
	 * 这是一个用于保存用户聊天信息的方法
	 * @param mes 储存信息的对象
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
	 * 这是一个用于获得用户离线时间的方法
	 * @param username 用户
	 * @param charts  模式：“all”代表是在用户上一次离开公共聊天室的时间；其他代表用户退出程序的时间
	 * @return timestamp 类型的时间
	 */
	private Timestamp getExitTime(String username,String charts)
	{
		Timestamp exitTime = null;
		String name = null;
		if(charts.equals("all"))
		{
			//获得的是关于用户退出程序的时间
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
	 * 这是一个获得用户的离线信息的方法
	 * @param username 用户
	 * @param model  模式：“all”表示为公共聊天室离线信息；其他表示为私聊信息或者系统提示消息。
	 * @return 返回一个字符串 List 列表
	 */
	public List<String> offLineMessage(String username,String model)
	{
		String strSql = null;
		List<String> allMessages = new ArrayList<String>();
		//获得用户离线时间
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
					//对时间进行截取
					send_time = send_time.substring(0, 19);
					//获得的信息是全体信息
					if(recipiten.equals("all"))
					{
						message = send+"("+send_time+") 说："+content+"。";
						allMessages.add(message);
					}
					else 
					{
						message = send+"("+send_time+") 悄悄地对你说："+content+"。";
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
	 * 这是一个获得用户所有离线信息的方法
	 * @param username 用户
	 * @return  返回一个字符串 List 列表
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
					//对时间进行截取
					send_time = send_time.substring(0, 19);
					
					message = send+"("+send_time+") 悄悄地对你说："+content+"。";
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
