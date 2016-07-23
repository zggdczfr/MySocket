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
 * 服务器端线程
 * </pre>
 */

public class ServerThread extends Thread
{
	private Socket socket;
	BufferedReader br = null;  //输入流
	PrintStream ps = null;     //输出流
	UserDAO userDAO = new UserDAO(); //调用相应操作数据库的方法
	MessageDAO messageDAO = new MessageDAO();
	FriendDAO friendDAO = new FriendDAO();
	BansDAO bansDAO = new BansDAO();
	
	
	/**
	 * 服务器线程构造器
	 * @param socket 与客户端连接的 Socket
	 */
	public ServerThread(Socket socket)
	{
		this.socket = socket;
	}
	
	
	/**
	 * 实现服务器线程的run()方法
	 */
	public void run()
	{
		try
		{
			//获取Socket对应的输入流
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			//获取Socket对应的输出流
			ps = new PrintStream(socket.getOutputStream());
			
			/*
			 * 服务器端对于用户线程的处理
			 */
			
			//利用死循环不断接受相应线程的信息
			String line = null;
			while((line = br.readLine()) != null)
			{
				//用户加入公共聊天室
				if(line.equals(Identifier.ADD_CHAT))
				{
				    	//将客户端线程加入到记录用户的Map表中
					Server.chatClients.put(Server.clients.getKeyByValue(ps), ps);
					ps.println();
					//对全体在线成员提示用户加入聊天室
					pritlnToAllChatusers("系统信息提示：" + Server.clients.getKeyByValue(ps) + "已经加入到公共聊天室。");
					//发送公共聊天室离线信息
					getOffLineMessage("all");
				}
				//用户退出聊天室
				else if(line.equals(Identifier.EXIT_CHAT))
				{
					//对全体在线成员提示用户离开
					pritlnToAllChatusers("系统信息提示：" + Server.clients.getKeyByValue(ps) + "已经离开公共聊天室。");
					ps.println();
					//从HashMap表中移除数据
					Server.chatClients.removeByValue(ps);
					//更新用户离开聊天室时间
					userDAO.updateExitTime(Server.clients.getKeyByValue(ps),
							Identifier.ExitFromChat);
				}
				//获得公共聊天室中全部在线成员
				else if (line.equals(Identifier.ALL_INCHAT)) 
				{
				    	//获得浏览器中全体在线用户列表
					List<String> allChats = new ArrayList<String>(Server.chatClients.keySet());
					ps.println();
					ps.println("当前聊天室在线用户列表：");
					//将列表遍历输出
					allPritln(allChats);
					ps.println();
				}
				//获得所有的好友
				else if (line.equals(Identifier.ALL_FRI)) 
				{
				    	//获得用户所有好友的列表
					List<String> allFriends = friendDAO.getAllFriends(Server.clients.getKeyByValue(ps));
					ps.println();
					ps.println("您的好友列表：");
					//将好友列表遍历输出
					allPritln(allFriends);
					ps.println();
				}
				//获得所有的私聊信息
				else if (line.equals(Identifier.ALL_PRI)) 
				{
				    	//获得所有私聊信息的列表
					List<String> allPriChat = messageDAO.allPrivateChat(Server.clients.getKeyByValue(ps));
					ps.println();
					ps.println("您的私聊信息列表：");
					//将私聊信息列表遍历输出
					allPritln(allPriChat);
					ps.println();
				}
				//获得所有的黑名单列表
				else if (line.equals(Identifier.ALL_BAN)) 
				{
				    	//获得用户黑名单列表
					List<String> allBans = bansDAO.allBans(Server.clients.getKeyByValue(ps));
					ps.println();
					ps.println("您的黑名单列表：");
					//将列表遍历输出
					allPritln(allBans);
					ps.println();
				}
				//将用户添加到黑名单
				else if (line.startsWith(Identifier.ADD_BAN) && line.endsWith(Identifier.ADD_BAN)) 
				{
				    	//获得被添加入黑名单的用户
					String banUsername = getRealMessage(line, Identifier.ADD_BAN.length());
					sendAdd(banUsername, Identifier.ADD_BAN);
				}
				//将用户移出黑名单
				else if (line.startsWith(Identifier.DEL_BAN) && line.endsWith(Identifier.DEL_BAN)) 
				{
				    	//获得操作的用户名
					String banUsername = getRealMessage(line, Identifier.DEL_BAN.length());
					bansDAO.deleteBan(Server.clients.getKeyByValue(ps), banUsername);
					int result = 0;
					//查看用户是否已经被移除黑名单列表
					if((result = bansDAO.isBan(Server.clients.getKeyByValue(ps), banUsername))==1)
					{
						
						ps.println("系统信息提示：已经成功将用户 "+banUsername+" 移出黑名单！");
					}
					else 
					{
						ps.println("系统信息提示：未知错误，操作失败，请重新操作！");
					}
				}
				//判断用户名是否存在
				else if(line.startsWith(Identifier.USERNAME) && line.endsWith(Identifier.USERNAME))
				{
					 //获得真正的名字
					String userName = getRealMessage(line, Identifier.USERNAME.length());
					
					//获得数据库判断
					int result = 1;
					result = userDAO.isExist(userName);
					if(result == 0)  //用户还未存在
					{
						ps.println(Identifier.SUCCESS);
					}
					else  //用户已经存在
					{
						ps.println(Identifier.USER_ERROR);
					}
				}
				//如果是用户注册
				else if(line.startsWith(Identifier.REGISTER)
							&& line.endsWith(Identifier.REGISTER)) 
				{
					String realMessage = getRealMessage(line, Identifier.REGISTER.length());
					//根据分隔符获得用户名和密码
					String[] mesArray = realMessage.split(Identifier.SPARATOR);
					String userName = mesArray[0];
					String passWord = mesArray[1];
					
					//再次判断用户名是否存在
					int i = userDAO.isExist(userName);
					if(i == 0)
					{
						//将用户注册信息存入数据库
						userDAO.addUser(new User(userName, passWord));
						userDAO.addUserToChats(userName);
						
						//返回成功的服务器反馈，同时往HashMap中添加信息
						ps.println(Identifier.SUCCESS);
						Server.clients.put(userName, ps);
					}
					else 
					{
					    	//返回用户不存在的反馈
						ps.println(Identifier.USER_ERROR);
					}
				}
				
				//如果是用户登录
				else if(line.startsWith(Identifier.LOGIN)
							&& line.endsWith(Identifier.LOGIN))
				{
					String realMessage = getRealMessage(line, Identifier.LOGIN.length());
					//根据分隔符获得用户名和密码
					String[] mesArray = realMessage.split(Identifier.SPARATOR);
					String userName = mesArray[0];
					String passWord = mesArray[1];
					
					//判断用户是否登录成功
					User user = new User(userName, passWord);
					int i = userDAO.isLogin(user);
					if(i == 1)   //登录成功
					{
						//往HashMap中添加信息,对自定义异常进行捕获
						try
						{
							//加入在线用户列表
							Server.clients.put(userName, ps);
							//返回成功的服务器反馈
							ps.println(Identifier.SUCCESS);
							//设置反馈停顿，以便用户功能界面显示
							Thread.sleep(500);
							//返回离线私信和系统通知
							getOffLineMessage(userName);
						} catch (RuntimeException e)
						{
							//专门对于同一个用户多个线程登录
							ps.println(Identifier.HAS_LOGIN);
						}

					}//登录失败，密码错误
					else
					{
						ps.println(Identifier.PASS_ERROR);
					}
				}
				//发送私信
				else if(line.startsWith(Identifier.PRIVATE_MESSAGE)
							&& line.endsWith(Identifier.PRIVATE_MESSAGE)) 
				{
					String realMessage = getRealMessage(line, Identifier.PRIVATE_MESSAGE.length());
					//获得私聊对象与私聊信息
					String[] mesArray = realMessage.split(Identifier.SPARATOR);
					String recipient = mesArray[0];
					String content = mesArray[1];
					
					//发送私信
					sendOffLineMessage(recipient, content);
				}
				//发送好友申请
				else if(line.startsWith(Identifier.ADD_FRI) && 
						line.endsWith(Identifier.ADD_FRI))
				{
					//获得想要添加的好友用户名
					String realMessage = getRealMessage(line, Identifier.ADD_FRI.length());
					sendAdd(realMessage,Identifier.ADD_FRI);
				}
				//好友添加的反馈
				else if (line.startsWith(Identifier.RESPONE_FRI) 
						&& line.endsWith(Identifier.RESPONE_FRI)) 
				{
					String realMessage = getRealMessage(line, Identifier.RESPONE_FRI.length());
					//获得回复对象
					String[] mesArray = realMessage.split(Identifier.SPARATOR);
					String friend = mesArray[0];
					String respone = mesArray[1];
					//对好友申请反馈的响应
					respondApply(friend, respone);
				}
				//公共聊天室中向全体成员发送信息
				else 
				{
					String realMessage = getRealMessage(line, Identifier.ALL_MESSAGE.length());
					String send = Server.clients.getKeyByValue(ps);
					//保存到数据库
					messageDAO.saveMessage(new Message(send, "all", null, realMessage));
					//遍历输出
					pritlnToAllChatusers(send+"说："+realMessage);
				}
			}
		} 
		catch(InterruptedException i)
		{
			i.printStackTrace();
		}
		//发生异常，证明该客户端已经关闭，所以将它移除
		catch (IOException e)
		{
			//更新数据库资料
			userDAO.updateExitTime(Server.clients.getKeyByValue(ps), Identifier.ExitFromProcedure);
			//从HashMap表中移除数据
			Server.clients.removeByValue(ps);
		
		}finally 
		{
			//关闭物理资源
			serverThreadClose();
		}
	}
	
	/**
	 * 向用户发送私信的方法（会先判断接收用户是否存在,是否为黑名单）
	 * @param recipient 接收私信的用户
	 * @param content   私信内容
	 */
	private void sendOffLineMessage(String recipient, String content)
	{
		//判断用户是否存在
		int result = userDAO.isExist(recipient);
		//判断用户是否在黑名单中,禁言关系返回0
		int i = bansDAO.isBan(recipient, Server.clients.getKeyByValue(ps));
		if(result == 0)
		{
			ps.println("系统信息提示：该用户不存在！");
		} 
		else if(result == 1 && i == 1)
		{
		    	//获得发送信息的用户名
			String send = Server.clients.getKeyByValue(ps);
			//将私聊信息储存进数据库
			messageDAO.saveMessage(new Message(send, recipient, null, content));
			if(Server.clients.get(recipient) != null)
			{
				//向私聊对象发送信息
				Server.clients.get(recipient).println(
						send+" 悄悄对你说："+content);
			}
		}
	}
	
	/**
	 * 获得用户的离线信息列表，并发送到客户端
	 * @param model 模式：若为“all”则发送公共聊天室中的离线信息；否则返回私信离线信息
	 */
	private void getOffLineMessage(String model)
	{
		//获得离线信息表:包括公共聊天信息，和其他信息
		List<String> messages = null; 
		messages = messageDAO.offLineMessage(Server.clients.getKeyByValue(ps), model);
		for(String mes : messages)
		{
			ps.println(mes);
		}
	}
	
	/**
	 * 将用户加入用户的关系列表中 (会先判断用户是否存在，是否已经存在操作的关系)
	 * @param p_username 被操作的用户
	 * @param model 模式：若为ADD_FRI，操作好友列表；否则操作黑名单列表
	 */
	private void sendAdd(String p_username,String model)
	{
		//判断该用户是否存在
		int result = userDAO.isExist(p_username);
		if(result == 0)
		{
			ps.println("该用户不存在");
		}
		else  if(result==1 && model.equals(Identifier.ADD_FRI))
		{
			int i = friendDAO.isFriends(Server.clients.getKeyByValue(ps), p_username);
			//先判断双方是否为好友
			if(i== 0)
			{
				//两个人并非好友
				friendDAO.sendApply(Server.clients.getKeyByValue(ps), p_username);
				//如果该用户在线
				if(Server.clients.get(p_username) != null)
				{
					Server.clients.get(p_username).println(
							"系统消息提示：用户 "+Server.clients.getKeyByValue(ps)+" 请求添加您为好友！");
				}//用户不在线,保存到 messages 表中
				else
				{
					messageDAO.saveMessage(new Message("系统", p_username, null, "用户 "+Server.clients.getKeyByValue(ps)+" 请求添加您为好友！"));
				}
				
				
				ps.println("系统消息提示：已经向用户 "+p_username+" 发送好友申请");
			}
			else
			{
				ps.println("系统信息提示：您已经添加用户 "+p_username+" 为好友！");
			}
			
			
		}
		else if (result == 1 && model.equals(Identifier.ADD_BAN)) 
		{
			int b = bansDAO.isBan(Server.clients.getKeyByValue(ps),p_username);
			if(b==1)
			{
				//添加到数据库
				bansDAO.addBanUser(Server.clients.getKeyByValue(ps), p_username);
				//判断是否添加成功
				int i = bansDAO.isBan(Server.clients.getKeyByValue(ps), p_username);
				if(i != 1)
				{
					ps.println("系统信息提示：已经成功将用户 "+p_username+" 添加到黑名单");   //添加成功
				}
				else
				{
					ps.println("系统信息提示：未知错误，未能将用户 "+p_username+" 添加到黑名单");   //添加失败
				}
			
			}
			else 
			{
				ps.println("系统信息提示：用户 "+p_username+" 已被添加到黑名单!");
			}
			
		}
	}
	
	/**
	 * 对于好友申请的反馈
	 * @param applicant 发送好友申请的用户
	 * @param mes 好友申请的反馈:若为“add”同意好友申请；否则拒绝好友申请
	 */
	private void respondApply(String applicant, String mes)
	{
		//先判断是否存在该好友申请，不存在则无需理会
		int result = friendDAO.hasApply(applicant, Server.clients.getKeyByValue(ps));
		String message = null;
		
		if(result == 1)
		{
			if(mes.equals("add")) //同意
			{
				friendDAO.addFriend(applicant, Server.clients.getKeyByValue(ps));
				message = "用户 "+Server.clients.getKeyByValue(ps)+" 已经同意您的好友申请。";
			}
			else
			{
				message = "用户 "+Server.clients.getKeyByValue(ps)+" 已经拒绝您的好友申请。";
			}
			
			//判断对方是否在线
			if (Server.clients.get(applicant) != null)
			{
			    	//若对方在线直接发送反馈
				Server.clients.get(applicant).println("系统消息提示："+message);
				messageDAO.saveMessage(new Message("系统", applicant, null, message));
			} else
			{
			    	//若对方不在线则将信息存入数据库
				messageDAO.saveMessage(new Message("系统", applicant, null, message));
			}
		}
	}
	
	/**
	 * 关闭物理资源的方法
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
	 * 还原用户发送的数据
	 * @param line 客户端发送的信息
	 * @param len 信息两头标识符的长度
	 * @return 返回用户发送的信息
	 */
	private String getRealMessage(String line,int len)
	{
		return line.substring(len, line.length()-len);
	}
	
	/**
	 * 对公共聊天室用户集合遍历输出一条信息
	 * @param message 信息内容
	 */
	private void pritlnToAllChatusers(String message)
	{
		for(PrintStream p_ps : Server.chatClients.valueSet())
		{
			p_ps.println(message);
		}
	}
	
	/**
	 * 对本用户遍历输出一个消息的集合
	 * @param mesArray 消息的集合
	 */
	private void allPritln(List<String> mesArray)
	{
		for(String mes : mesArray)
		{
			ps.println(mes);
		}
	}


}
