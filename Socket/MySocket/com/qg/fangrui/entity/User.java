package com.qg.fangrui.entity;

/**
 * 
 * @author Administrator
 * <pre>
 * ����һ�������û��������
 * </pre>
 */


public class User
{
	private String username;
	private String password;
	
	/**
	 * �����û�����Ĺ�����
	 * @param username  �û���
	 * @param password  ����
	 */
	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	
	/**
	 * ���ڶ����и������Ե�get()��set()����
	 * 
	 */
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
	
	
}
