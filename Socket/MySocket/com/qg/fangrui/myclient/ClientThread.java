package com.qg.fangrui.myclient;

import java.io.BufferedReader;
import java.io.IOException;

import com.qg.fangrui.util.Identifier;

/**
 * 
 * @author Administrator
 * <pre>
 * 客户端线程，专门用来处理输出流
 * </pre>
 */

public class ClientThread extends Thread
{
	//输出流
	BufferedReader br = null;
	//构造器
	public ClientThread(BufferedReader br)
	{
		this.br = br;
	}
	
	/**
	 * 实现run()方法
	 */
	public void run()
	{
		try
		{
			String line = null;
			//利用死循环不断接受从服务器端传来的信息
			while((line = br.readLine()) != null)
			{
				//专门为私聊配置用户不存在与
				if(line.equals(Identifier.SUCCESS))
				{
					System.out.println("该用户不存在");
				}
				//正常输出服务器端的信息
				else 
				{
					System.out.println(line);
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}finally
		{
			clientThreadClose();
		}
	}
	
	
	/**
	 * 专门用于关闭线程IO流资源的方法
	 */
	public void clientThreadClose()
	{
		try
		{
			if(br != null)
			{
				br.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
