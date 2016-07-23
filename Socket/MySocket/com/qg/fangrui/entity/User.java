package com.qg.fangrui.entity;

/**
 * 
 * @author Administrator
 * <pre>
 * 这是一个构造用户对象的类
 * </pre>
 */


public class User
{
	private String username;
	private String password;
	
	/**
	 * 关于用户对象的构造器
	 * @param username  用户名
	 * @param password  密码
	 */
	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	
	/**
	 * 关于对象中各个属性的get()与set()方法
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
