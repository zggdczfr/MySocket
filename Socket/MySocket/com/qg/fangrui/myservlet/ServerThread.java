package com.qg.fangrui.myservlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.qg.fangrui.dao.BansDAO;
import com.qg.fangrui.dao.FriendDAO;
import com.qg.fangrui.dao.MessageDAO;
import com.qg.fangrui.dao.UserDAO;
import com.qg.fangrui.entity.Message;
import com.qg.fangrui.entity.User;
import com.qg.fangrui.util.Identifier;

/**
 * 
 * @author Administrator
 * <pre>
 * ���������߳�
 * </pre>
 */

public class ServerThread extends Thread
{
	private Socket socket;
	BufferedReader br = null;  //������
	PrintStream ps = null;     //�����
	UserDAO userDAO = new UserDAO(); //������Ӧ�������ݿ�ķ���
	MessageDAO messageDAO = new MessageDAO();
	FriendDAO friendDAO = new FriendDAO();
	BansDAO bansDAO = new BansDAO();
	
	
	/**
	 * �������̹߳�����
	 * @param socket ��ͻ������ӵ� Socket
	 */
	public ServerThread(Socket socket)
	{
		this.socket = socket;
	}
	
	
	/**
	 * ʵ�ַ������̵߳�run()����
	 */
	public void run()
	{
		try
		{
			//��ȡSocket��Ӧ��������
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			//��ȡSocket��Ӧ�������
			ps = new PrintStream(socket.getOutputStream());
			
			/*
			 * �������˶����û��̵߳Ĵ���
			 */
			
			//������ѭ�����Ͻ�����Ӧ�̵߳���Ϣ
			String line = null;
			while((line = br.readLine()) != null)
			{
				//�û����빫��������
				if(line.equals(Identifier.ADD_CHAT))
				{
				    	//���ͻ����̼߳��뵽��¼�û���Map����
					Server.chatClients.put(Server.clients.getKeyByValue(ps), ps);
					ps.println();
					//��ȫ�����߳�Ա��ʾ�û�����������
					pritlnToAllChatusers("ϵͳ��Ϣ��ʾ��" + Server.clients.getKeyByValue(ps) + "�Ѿ����뵽���������ҡ�");
					//���͹���������������Ϣ
					getOffLineMessage("all");
				}
				//�û��˳�������
				else if(line.equals(Identifier.EXIT_CHAT))
				{
					//��ȫ�����߳�Ա��ʾ�û��뿪
					pritlnToAllChatusers("ϵͳ��Ϣ��ʾ��" + Server.clients.getKeyByValue(ps) + "�Ѿ��뿪���������ҡ�");
					ps.println();
					//��HashMap�����Ƴ�����
					Server.chatClients.removeByValue(ps);
					//�����û��뿪������ʱ��
					userDAO.updateExitTime(Server.clients.getKeyByValue(ps),
							Identifier.ExitFromChat);
				}
				//��ù�����������ȫ�����߳�Ա
				else if (line.equals(Identifier.ALL_INCHAT)) 
				{
				    	//����������ȫ�������û��б�
					List<String> allChats = new ArrayList<String>(Server.chatClients.keySet());
					ps.println();
					ps.println("��ǰ�����������û��б�");
					//���б�������
					allPritln(allChats);
					ps.println();
				}
				//������еĺ���
				else if (line.equals(Identifier.ALL_FRI)) 
				{
				    	//����û����к��ѵ��б�
					List<String> allFriends = friendDAO.getAllFriends(Server.clients.getKeyByValue(ps));
					ps.println();
					ps.println("���ĺ����б�");
					//�������б�������
					allPritln(allFriends);
					ps.println();
				}
				//������е�˽����Ϣ
				else if (line.equals(Identifier.ALL_PRI)) 
				{
				    	//�������˽����Ϣ���б�
					List<String> allPriChat = messageDAO.allPrivateChat(Server.clients.getKeyByValue(ps));
					ps.println();
					ps.println("����˽����Ϣ�б�");
					//��˽����Ϣ�б�������
					allPritln(allPriChat);
					ps.println();
				}
				//������еĺ������б�
				else if (line.equals(Identifier.ALL_BAN)) 
				{
				    	//����û��������б�
					List<String> allBans = bansDAO.allBans(Server.clients.getKeyByValue(ps));
					ps.println();
					ps.println("���ĺ������б�");
					//���б�������
					allPritln(allBans);
					ps.println();
				}
				//���û���ӵ�������
				else if (line.startsWith(Identifier.ADD_BAN) && line.endsWith(Identifier.ADD_BAN)) 
				{
				    	//��ñ��������������û�
					String banUsername = getRealMessage(line, Identifier.ADD_BAN.length());
					sendAdd(banUsername, Identifier.ADD_BAN);
				}
				//���û��Ƴ�������
				else if (line.startsWith(Identifier.DEL_BAN) && line.endsWith(Identifier.DEL_BAN)) 
				{
				    	//��ò������û���
					String banUsername = getRealMessage(line, Identifier.DEL_BAN.length());
					bansDAO.deleteBan(Server.clients.getKeyByValue(ps), banUsername);
					int result = 0;
					//�鿴�û��Ƿ��Ѿ����Ƴ��������б�
					if((result = bansDAO.isBan(Server.clients.getKeyByValue(ps), banUsername))==1)
					{
						
						ps.println("ϵͳ��Ϣ��ʾ���Ѿ��ɹ����û� "+banUsername+" �Ƴ���������");
					}
					else 
					{
						ps.println("ϵͳ��Ϣ��ʾ��δ֪���󣬲���ʧ�ܣ������²�����");
					}
				}
				//�ж��û����Ƿ����
				else if(line.startsWith(Identifier.USERNAME) && line.endsWith(Identifier.USERNAME))
				{
					 //�������������
					String userName = getRealMessage(line, Identifier.USERNAME.length());
					
					//������ݿ��ж�
					int result = 1;
					result = userDAO.isExist(userName);
					if(result == 0)  //�û���δ����
					{
						ps.println(Identifier.SUCCESS);
					}
					else  //�û��Ѿ�����
					{
						ps.println(Identifier.USER_ERROR);
					}
				}
				//������û�ע��
				else if(line.startsWith(Identifier.REGISTER)
							&& line.endsWith(Identifier.REGISTER)) 
				{
					String realMessage = getRealMessage(line, Identifier.REGISTER.length());
					//���ݷָ�������û���������
					String[] mesArray = realMessage.split(Identifier.SPARATOR);
					String userName = mesArray[0];
					String passWord = mesArray[1];
					
					//�ٴ��ж��û����Ƿ����
					int i = userDAO.isExist(userName);
					if(i == 0)
					{
						//���û�ע����Ϣ�������ݿ�
						userDAO.addUser(new User(userName, passWord));
						userDAO.addUserToChats(userName);
						
						//���سɹ��ķ�����������ͬʱ��HashMap�������Ϣ
						ps.println(Identifier.SUCCESS);
						Server.clients.put(userName, ps);
					}
					else 
					{
					    	//�����û������ڵķ���
						ps.println(Identifier.USER_ERROR);
					}
				}
				
				//������û���¼
				else if(line.startsWith(Identifier.LOGIN)
							&& line.endsWith(Identifier.LOGIN))
				{
					String realMessage = getRealMessage(line, Identifier.LOGIN.length());
					//���ݷָ�������û���������
					String[] mesArray = realMessage.split(Identifier.SPARATOR);
					String userName = mesArray[0];
					String passWord = mesArray[1];
					
					//�ж��û��Ƿ��¼�ɹ�
					User user = new User(userName, passWord);
					int i = userDAO.isLogin(user);
					if(i == 1)   //��¼�ɹ�
					{
						//��HashMap�������Ϣ,���Զ����쳣���в���
						try
						{
							//���������û��б�
							Server.clients.put(userName, ps);
							//���سɹ��ķ���������
							ps.println(Identifier.SUCCESS);
							//���÷���ͣ�٣��Ա��û����ܽ�����ʾ
							Thread.sleep(500);
							//��������˽�ź�ϵͳ֪ͨ
							getOffLineMessage(userName);
						} catch (RuntimeException e)
						{
							//ר�Ŷ���ͬһ���û�����̵߳�¼
							ps.println(Identifier.HAS_LOGIN);
						}

					}//��¼ʧ�ܣ��������
					else
					{
						ps.println(Identifier.PASS_ERROR);
					}
				}
				//����˽��
				else if(line.startsWith(Identifier.PRIVATE_MESSAGE)
							&& line.endsWith(Identifier.PRIVATE_MESSAGE)) 
				{
					String realMessage = getRealMessage(line, Identifier.PRIVATE_MESSAGE.length());
					//���˽�Ķ�����˽����Ϣ
					String[] mesArray = realMessage.split(Identifier.SPARATOR);
					String recipient = mesArray[0];
					String content = mesArray[1];
					
					//����˽��
					sendOffLineMessage(recipient, content);
				}
				//���ͺ�������
				else if(line.startsWith(Identifier.ADD_FRI) && 
						line.endsWith(Identifier.ADD_FRI))
				{
					//�����Ҫ��ӵĺ����û���
					String realMessage = getRealMessage(line, Identifier.ADD_FRI.length());
					sendAdd(realMessage,Identifier.ADD_FRI);
				}
				//������ӵķ���
				else if (line.startsWith(Identifier.RESPONE_FRI) 
						&& line.endsWith(Identifier.RESPONE_FRI)) 
				{
					String realMessage = getRealMessage(line, Identifier.RESPONE_FRI.length());
					//��ûظ�����
					String[] mesArray = realMessage.split(Identifier.SPARATOR);
					String friend = mesArray[0];
					String respone = mesArray[1];
					//�Ժ������뷴������Ӧ
					respondApply(friend, respone);
				}
				//��������������ȫ���Ա������Ϣ
				else 
				{
					String realMessage = getRealMessage(line, Identifier.ALL_MESSAGE.length());
					String send = Server.clients.getKeyByValue(ps);
					//���浽���ݿ�
					messageDAO.saveMessage(new Message(send, "all", null, realMessage));
					//�������
					pritlnToAllChatusers(send+"˵��"+realMessage);
				}
			}
		} 
		catch(InterruptedException i)
		{
			i.printStackTrace();
		}
		//�����쳣��֤���ÿͻ����Ѿ��رգ����Խ����Ƴ�
		catch (IOException e)
		{
			//�������ݿ�����
			userDAO.updateExitTime(Server.clients.getKeyByValue(ps), Identifier.ExitFromProcedure);
			//��HashMap�����Ƴ�����
			Server.clients.removeByValue(ps);
		
		}finally 
		{
			//�ر�������Դ
			serverThreadClose();
		}
	}
	
	/**
	 * ���û�����˽�ŵķ����������жϽ����û��Ƿ����,�Ƿ�Ϊ��������
	 * @param recipient ����˽�ŵ��û�
	 * @param content   ˽������
	 */
	private void sendOffLineMessage(String recipient, String content)
	{
		//�ж��û��Ƿ����
		int result = userDAO.isExist(recipient);
		//�ж��û��Ƿ��ں�������,���Թ�ϵ����0
		int i = bansDAO.isBan(recipient, Server.clients.getKeyByValue(ps));
		if(result == 0)
		{
			ps.println("ϵͳ��Ϣ��ʾ�����û������ڣ�");
		} 
		else if(result == 1 && i == 1)
		{
		    	//��÷�����Ϣ���û���
			String send = Server.clients.getKeyByValue(ps);
			//��˽����Ϣ��������ݿ�
			messageDAO.saveMessage(new Message(send, recipient, null, content));
			if(Server.clients.get(recipient) != null)
			{
				//��˽�Ķ�������Ϣ
				Server.clients.get(recipient).println(
						send+" ���Ķ���˵��"+content);
			}
		}
	}
	
	/**
	 * ����û���������Ϣ�б������͵��ͻ���
	 * @param model ģʽ����Ϊ��all�����͹����������е�������Ϣ�����򷵻�˽��������Ϣ
	 */
	private void getOffLineMessage(String model)
	{
		//���������Ϣ��:��������������Ϣ����������Ϣ
		List<String> messages = null; 
		messages = messageDAO.offLineMessage(Server.clients.getKeyByValue(ps), model);
		for(String mes : messages)
		{
			ps.println(mes);
		}
	}
	
	/**
	 * ���û������û��Ĺ�ϵ�б��� (�����ж��û��Ƿ���ڣ��Ƿ��Ѿ����ڲ����Ĺ�ϵ)
	 * @param p_username ���������û�
	 * @param model ģʽ����ΪADD_FRI�����������б���������������б�
	 */
	private void sendAdd(String p_username,String model)
	{
		//�жϸ��û��Ƿ����
		int result = userDAO.isExist(p_username);
		if(result == 0)
		{
			ps.println("���û�������");
		}
		else  if(result==1 && model.equals(Identifier.ADD_FRI))
		{
			int i = friendDAO.isFriends(Server.clients.getKeyByValue(ps), p_username);
			//���ж�˫���Ƿ�Ϊ����
			if(i== 0)
			{
				//�����˲��Ǻ���
				friendDAO.sendApply(Server.clients.getKeyByValue(ps), p_username);
				//������û�����
				if(Server.clients.get(p_username) != null)
				{
					Server.clients.get(p_username).println(
							"ϵͳ��Ϣ��ʾ���û� "+Server.clients.getKeyByValue(ps)+" ���������Ϊ���ѣ�");
				}//�û�������,���浽 messages ����
				else
				{
					messageDAO.saveMessage(new Message("ϵͳ", p_username, null, "�û� "+Server.clients.getKeyByValue(ps)+" ���������Ϊ���ѣ�"));
				}
				
				
				ps.println("ϵͳ��Ϣ��ʾ���Ѿ����û� "+p_username+" ���ͺ�������");
			}
			else
			{
				ps.println("ϵͳ��Ϣ��ʾ�����Ѿ�����û� "+p_username+" Ϊ���ѣ�");
			}
			
			
		}
		else if (result == 1 && model.equals(Identifier.ADD_BAN)) 
		{
			int b = bansDAO.isBan(Server.clients.getKeyByValue(ps),p_username);
			if(b==1)
			{
				//��ӵ����ݿ�
				bansDAO.addBanUser(Server.clients.getKeyByValue(ps), p_username);
				//�ж��Ƿ���ӳɹ�
				int i = bansDAO.isBan(Server.clients.getKeyByValue(ps), p_username);
				if(i != 1)
				{
					ps.println("ϵͳ��Ϣ��ʾ���Ѿ��ɹ����û� "+p_username+" ��ӵ�������");   //��ӳɹ�
				}
				else
				{
					ps.println("ϵͳ��Ϣ��ʾ��δ֪����δ�ܽ��û� "+p_username+" ��ӵ�������");   //���ʧ��
				}
			
			}
			else 
			{
				ps.println("ϵͳ��Ϣ��ʾ���û� "+p_username+" �ѱ���ӵ�������!");
			}
			
		}
	}
	
	/**
	 * ���ں�������ķ���
	 * @param applicant ���ͺ���������û�
	 * @param mes ��������ķ���:��Ϊ��add��ͬ��������룻����ܾ���������
	 */
	private void respondApply(String applicant, String mes)
	{
		//���ж��Ƿ���ڸú������룬���������������
		int result = friendDAO.hasApply(applicant, Server.clients.getKeyByValue(ps));
		String message = null;
		
		if(result == 1)
		{
			if(mes.equals("add")) //ͬ��
			{
				friendDAO.addFriend(applicant, Server.clients.getKeyByValue(ps));
				message = "�û� "+Server.clients.getKeyByValue(ps)+" �Ѿ�ͬ�����ĺ������롣";
			}
			else
			{
				message = "�û� "+Server.clients.getKeyByValue(ps)+" �Ѿ��ܾ����ĺ������롣";
			}
			
			//�ж϶Է��Ƿ�����
			if (Server.clients.get(applicant) != null)
			{
			    	//���Է�����ֱ�ӷ��ͷ���
				Server.clients.get(applicant).println("ϵͳ��Ϣ��ʾ��"+message);
				messageDAO.saveMessage(new Message("ϵͳ", applicant, null, message));
			} else
			{
			    	//���Է�����������Ϣ�������ݿ�
				messageDAO.saveMessage(new Message("ϵͳ", applicant, null, message));
			}
		}
	}
	
	/**
	 * �ر�������Դ�ķ���
	 */
	private void serverThreadClose()
	{
		try
		{
			if (br != null)
			{
				br.close();
			}
			if(ps != null)
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
	 * ��ԭ�û����͵�����
	 * @param line �ͻ��˷��͵���Ϣ
	 * @param len ��Ϣ��ͷ��ʶ���ĳ���
	 * @return �����û����͵���Ϣ
	 */
	private String getRealMessage(String line,int len)
	{
		return line.substring(len, line.length()-len);
	}
	
	/**
	 * �Թ����������û����ϱ������һ����Ϣ
	 * @param message ��Ϣ����
	 */
	private void pritlnToAllChatusers(String message)
	{
		for(PrintStream p_ps : Server.chatClients.valueSet())
		{
			p_ps.println(message);
		}
	}
	
	/**
	 * �Ա��û��������һ����Ϣ�ļ���
	 * @param mesArray ��Ϣ�ļ���
	 */
	private void allPritln(List<String> mesArray)
	{
		for(String mes : mesArray)
		{
			ps.println(mes);
		}
	}


}
