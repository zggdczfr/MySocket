package com.qg.fangrui.myclient;

import java.io.BufferedReader;
import java.io.IOException;

import com.qg.fangrui.util.Identifier;

/**
 * 
 * @author Administrator
 * <pre>
 * �ͻ����̣߳�ר���������������
 * </pre>
 */

public class ClientThread extends Thread
{
	//�����
	BufferedReader br = null;
	//������
	public ClientThread(BufferedReader br)
	{
		this.br = br;
	}
	
	/**
	 * ʵ��run()����
	 */
	public void run()
	{
		try
		{
			String line = null;
			//������ѭ�����Ͻ��ܴӷ������˴�������Ϣ
			while((line = br.readLine()) != null)
			{
				//ר��Ϊ˽�������û���������
				if(line.equals(Identifier.SUCCESS))
				{
					System.out.println("���û�������");
				}
				//��������������˵���Ϣ
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
	 * ר�����ڹر��߳�IO����Դ�ķ���
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
