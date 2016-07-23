package com.qg.fangrui.myservlet;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.qg.fangrui.util.MyMap;

/**
 * 
 * @author Administrator
 * <pre>
 * 服务器端
 * </pre>
 */

public class Server
{
    	//端口号
	private static final int PORT = 30000;
	ServerSocket ss = null;
	
	//记录所有在线用户的hashMap表
	public static MyMap<String, PrintStream> clients =
			new MyMap<String,PrintStream>();
	//记录公共聊天用户的hashMap表
	public static MyMap<String, PrintStream> chatClients = 
			new MyMap<String,PrintStream>();
	
	/**
	 * 服务器端初始化方法
	 */
	public void init()
	{
		try
		{
			ss = new ServerSocket(PORT);
			//死循环不断接受客户端
			while(true)
			{
				Socket socket = ss.accept();
				//开启客户端服务线程
				new ServerThread(socket).start();
			}
		} catch (Exception e)
		{
			System.out.println("服务器启动异常！");
		}finally 
		{
			serverClose();
			System.exit(1);
		}
	}
	
	/**
	 * 专门用于关闭服务器资源的方法
	 */
	public void serverClose()
	{
		try
		{
			if (ss != null)
			{
				ss.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	
	/**
	 * 启动服务器
	 * @param args
	 */
	public static void main(String[] args)
	{
		Server server = new Server();
		server.init();
	}
}
