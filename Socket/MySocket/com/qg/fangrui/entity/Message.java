package com.qg.fangrui.entity;

import java.sql.Timestamp;

/**
 * 
 * @author Administrator
 * <pre>
 * 这是一个构造聊天信息对象的类
 * </pre>
 */

public class Message
{
	private String send;
	private String recipient;
	private Timestamp send_time;
	private String content;
	
	/**
	 * 关于聊天信息对象的构造器
	 * @param send 发送信息的用户
	 * @param recipient  接收信息的用户；若群聊信息，该项默认为“all”
	 * @param send_time 发送信息的时间
	 * @param content  信息内容
	 */
	public Message(String send,String recipient,
			Timestamp send_time,String content)
	{
		this.send = send;
		this.recipient = recipient;
		this.send_time = send_time;
		this.content = content;
	}

	/**
	 * 关于对象中各个属性的get()与set()方法 
	 * 
	 */
	
	public String getSend()
	{
		return send;
	}

	public void setSend(String send)
	{
		this.send = send;
	}

	public String getRecipient()
	{
		return recipient;
	}

	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
	}

	public Timestamp getSend_time()
	{
		return send_time;
	}

	public void setSend_time(Timestamp send_time)
	{
		this.send_time = send_time;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
