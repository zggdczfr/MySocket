package com.qg.fangrui.entity;

import java.sql.Timestamp;

/**
 * 
 * @author Administrator
 * <pre>
 * ����һ������������Ϣ�������
 * </pre>
 */

public class Message
{
	private String send;
	private String recipient;
	private Timestamp send_time;
	private String content;
	
	/**
	 * ����������Ϣ����Ĺ�����
	 * @param send ������Ϣ���û�
	 * @param recipient  ������Ϣ���û�����Ⱥ����Ϣ������Ĭ��Ϊ��all��
	 * @param send_time ������Ϣ��ʱ��
	 * @param content  ��Ϣ����
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
	 * ���ڶ����и������Ե�get()��set()���� 
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
