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
 * ��������
 * </pre>
 */

public class Server
{
    	//�˿ں�
	private static final int PORT = 30000;
	ServerSocket ss = null;
	
	//��¼���������û���hashMap��
	public static MyMap<String, PrintStream> clients =
			new MyMap<String,PrintStream>();
	//��¼���������û���hashMap��
	public static MyMap<String, PrintStream> chatClients = 
			new MyMap<String,PrintStream>();
	
	/**
	 * �������˳�ʼ������
	 */
	public void init()
	{
		try
		{
			ss = new ServerSocket(PORT);
			//��ѭ�����Ͻ��ܿͻ���
			while(true)
			{
				Socket socket = ss.accept();
				//�����ͻ��˷����߳�
				new ServerThread(socket).start();
			}
		} catch (Exception e)
		{
			System.out.println("�����������쳣��");
		}finally 
		{
			serverClose();
			System.exit(1);
		}
	}
	
	/**
	 * ר�����ڹرշ�������Դ�ķ���
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
	 * ����������
	 * @param args
	 */
	public static void main(String[] args)
	{
		Server server = new Server();
		server.init();
	}
}
