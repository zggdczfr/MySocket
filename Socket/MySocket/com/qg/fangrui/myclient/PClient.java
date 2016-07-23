package com.qg.fangrui.myclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import com.qg.fangrui.util.Identifier;

/**
 * 
 * @author Administrator
 * <pre>
 * �ͻ����߳�
 * </pre>
 */

public class PClient
{
	private static final int PORT = 30000;
	private Socket socket;
	private BufferedReader br;   //������:���շ������˵���Ӧ
	private BufferedReader in;   //��������
	private PrintStream ps;      //�����:���͸�������
	
	/**
	 * �ͻ��˳�ʼ������
	 */
	public void init()
	{
		try
		{
			//��װ��������
			in = new BufferedReader(new InputStreamReader(System.in));
			//���ӵ�������
			socket = new Socket("127.0.0.1", PORT);
			//���Socket��Ӧ���������������
			ps = new PrintStream(socket.getOutputStream());
			br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			String line = null;
			
			while(true)
			{
				System.out.println("--------------------");
				System.out.println("	��ѡ��		");
				System.out.println("	��1����¼		");
				System.out.println("	��2��ע��		");
				System.out.println("	��3���˳�		");
				System.out.println("--------------------");
				
				//��ȡ��������
				line = in.readLine();
				if(line.equals("1"))
				{
					login();  //��ת����¼����
					System.out.println("��¼�ɹ����ѽ��������ң�");
					break;
				}
				else if (line.equals("2"))
				{
					register();   //��ת��ע�᷽��
					break;
				}
				else if (line.equals("3"))  //�˳�����
				{
					System.out.println("ллʹ�ã�");
					System.exit(1);
				}
				else 
				{
					System.out.println("����������������룡");
				}
			}
			
			/*
			 * ����ר�����ڽ��յ��߳�
			 */
			
			//�ͻ����߳�
			new ClientThread(br).start();
			
		}catch (IOException e)
		{
			System.out.println("����ͨ���쳣��");
			clientClose();
			System.exit(1);
		}
	}
	
	/**
	 * �ͻ��˹��ܲ˵�����ʾ
	 */
	public void pattern()
	{
		try
		{
			while(true)
			{
				//���߳���ͣ����
				Thread.sleep(200);
				System.out.println("================");
				System.out.println("��ѡ���ܣ�");
				System.out.println("1������������");
				System.out.println("2��˽��");
				System.out.println("3���鿴�����б�");
				System.out.println("4���鿴˽���б�");
				System.out.println("5���鿴�������б�");
				System.out.println("6���˳�ϵͳ");
				System.out.println("================");
				
				String result;
				result = in.readLine();
				switch (result)
				{
				case "1":
					chat();  //���빫��������
					break;
				case "2":
					privateChat();  //����˽��ģʽ
					break;
				case "3":
					ps.println(Identifier.ALL_FRI);   //���Ͳ鿴�û����к��ѵı�ʶ��
					break;
				case "4":
					ps.println(Identifier.ALL_PRI);   //���Ͳ鿴�û�����˽����Ϣ�ı�ʶ��
					break;
				case "5":
					ps.println(Identifier.ALL_BAN);   //���Ͳ鿴�û��������б�ı�ʶ��
					break;
				case "6":
					clientClose();  //�رտͻ�����Դ���˳�
					System.exit(1);
					break;
				default:
					break;
				}
				
			}
			
			
		}catch(InterruptedException i)
		{
			i.printStackTrace();
			clientClose();
			System.exit(1);
		}
		catch (IOException e)
		{
			System.out.println("����ͨ���쳣��");
			clientClose();
			System.exit(1);
		}
		
	}
	
	/**
	 * ר�����ڹرտͻ�����Դ�ķ���
	 */
	public void clientClose()
	{
		try
		{
			if(in != null)
			{
				in.close();
			}
			if (br != null)
			{
				br.close();
			}
			if (ps != null)
			{
				ps.close();
			}
			if(socket != null)
			{
				socket.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * �û�����ע��ķ���
	 */
	private synchronized void register()
	{
		try
		{
			/*
			 * ��ȡ�û���ģ��
			 */
			String username = null;
			while(true)
			{
				System.out.print("�������û�����");
				username = in.readLine();
				
				//���û�����Э���һ���͵�����������ȡ����������
				ps.println(Identifier.USERNAME+username+Identifier.USERNAME);
				String result = br.readLine();
				
				if(result.equals(Identifier.USER_ERROR))
				{
					System.out.println("�û����ظ���������ע�ᣡ");
					continue;
				}
				
				
				/*
				 * ��ȡ����ģ��
				 */
				System.out.print("���������룺");
				String password = in.readLine();
				//���û���������Э���һ���͵�������
				ps.println(Identifier.REGISTER+
						username+Identifier.SPARATOR+password
						+Identifier.REGISTER);
				result = br.readLine();
				if(result.equals(Identifier.SUCCESS))
				{
					break;
				}
				else
				{
					System.out.println("ע��ʧ�ܣ�������ע�ᣡ");
				}
				
			}
			
		} catch (IOException e)
		{
			System.out.println("����ͨ���쳣��");
			clientClose();
			System.exit(1);
		}	
	}
	
	
	/**
	 * �û���¼�ķ���
	 */
	private void login()
	{
		try
		{
			while(true)
			{
				String username = null;
				String password = null;
				String result = null;
				
				System.out.print("�������û�����");
				username = in.readLine();
				
				//���û�����Э���һ���͵�����������ȡ����������
				ps.println(Identifier.USERNAME+username+Identifier.USERNAME);
				result = br.readLine();
				System.out.println(result);
				if(result.equals(Identifier.SUCCESS))  //�����û������ڻ᷵��success
				{
					System.out.println("�û�������!");
					continue;
				}
				
				System.out.print("���������룺");
				password = in.readLine();
					
				//���û���������һ���͵�������
				ps.println(Identifier.LOGIN+username
						+Identifier.SPARATOR+password+Identifier.LOGIN);
				
				result = br.readLine();
				if (result.equals(Identifier.HAS_LOGIN))
				{
					System.out.println("���û��ѵ�¼��");
					continue;
				}	
				if(result.equals(Identifier.PASS_ERROR))
				{
					System.out.println("������������µ�¼��");
					continue;
				}
				if(result.equals(Identifier.SUCCESS))
				{
					break;
				}
			
			}
				
			
		} catch (IOException e)
		{
			System.out.println("����ͨ���쳣��");
			clientClose();
			System.exit(1);
		}		
	}
	
	
	/**
	 * �û����빫�������ҵķ���
	 */
	private void chat()
	{
		ps.println(Identifier.ADD_CHAT);
		String line = null;
		try
		{
			while((line = in.readLine()) != null)
			{
				//�û��˳�����������
				if (line.equals("bye"))
				{
					ps.println(Identifier.EXIT_CHAT);  //������������˳����������ҵı�ʶ��
					break;
				}
				//������������ȫ�����߳�Ա
				else if (line.equals("AllInChat")) 
				{
					ps.println(Identifier.ALL_INCHAT);  //����������Ͳ鿴�����û��ı�ʶ��
				}
				//����д���TO��ǰ׺
				else if(line.startsWith("TO")) 
				{
				    	//���������Һ�˽���µĹ��÷���
					communal(line);
				}
				else
				{
				    	//��������ǰ׺��ֱ�ӽ��û���Ϣ����Ⱥ����Ϣ���͵�������
					ps.println(Identifier.ALL_MESSAGE+line
							+Identifier.ALL_MESSAGE);
					
				}
				
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * �û�����˽��ģʽ�ķ���
	 */
	private void privateChat()
	{
		String line = null;
		try
		{
			while((line = in.readLine()) != null)
			{
			    	//�û��˳�˽��ģʽ
				if(line.equals("bye"))
				{
					break;
				}
				//�û����ʹ��б�ʶ��"TO"����Ϣ
				if(line.startsWith("TO") )
				{
					communal(line);
				}
				else{
				    System.out.println("��Ϣ��ʾ�������ʽ�д����������룡");
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * ���������Һ�˽��ģ��Ĺ��÷���
	 * 
	 * ��Ӻ�����
	 * ɾ��������
	 * ��������
	 * �ظ���������
	 * ˽��
	 */
	public void communal(String line)
	{
		//���û���ӵ�����������ʽ TO+�û���+BAN
		if(line.startsWith("TO") && line.endsWith("BAN")) 
		{
			//�ص�ͷβ
			line = line.substring(2,line.length()-3);
			//��lineͷβ�ӱ�ʶ���������͵�������
			ps.println(Identifier.ADD_BAN + line + Identifier.ADD_BAN);
		}
		//ɾ������������ʽ TO+�û���+DEL
		else if(line.startsWith("TO") && line.endsWith("DEL"))
		{
			//��ȡͷβ
			line = line.substring(2,line.length()-3);
			//��lineͷβ�ӱ�ʶ���������͵�������
			ps.println(Identifier.DEL_BAN+line
					+Identifier.DEL_BAN);
		}
		//�������룬��ʽ TO+�û���+ADD
		else if(line.startsWith("TO") && line.endsWith("ADD"))
		{
			//�ص�ͷβ
			line = line.substring(2, line.length()-3);
			//��lineͷβ�ӱ�ʶ���������͵�������
			ps.println(Identifier.ADD_FRI+line
					+Identifier.ADD_FRI);
		}
		//����˽����Ϣ,��ʽ��TO+˽�Ķ���+:+˽����Ϣ+PRI��
		else if(line.startsWith("TO") && line.endsWith("PRI") && line.indexOf(":")>0)
		{
			//�ص�ͷβ
			line = line.substring(2,line.length()-3);
			ps.println(Identifier.PRIVATE_MESSAGE+line.replaceFirst(":", Identifier.SPARATOR)
					+Identifier.PRIVATE_MESSAGE);
		}
		//�Ժ�����������ж�,���ַ�����add����Ϊͬ����������־����ʽ TO+������û���+:+�û�������Ϣ+RES
		else if(line.startsWith("TO") &&  line.endsWith("RES") && line.indexOf(":")>0) 
		{
			//�ص�ͷβ
			line = line.substring(2, line.length()-3);
			ps.println(Identifier.RESPONE_FRI+line.replace(":", Identifier.SPARATOR)
						+Identifier.RESPONE_FRI);
		}
	}
	
	/**
	 * �ͻ������߳�
	 * @param args
	 */
	public static void main(String[] args)
	{
		PClient pClient = new PClient();
		pClient.init();    //��ʼ��
		pClient.pattern(); //�����б�
	}
	
}
