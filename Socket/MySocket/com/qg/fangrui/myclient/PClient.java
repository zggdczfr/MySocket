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
 * 客户端线程
 * </pre>
 */

public class PClient
{
	private static final int PORT = 30000;
	private Socket socket;
	private BufferedReader br;   //输入流:接收服务器端的响应
	private BufferedReader in;   //键盘输入
	private PrintStream ps;      //输出流:发送给服务器
	
	/**
	 * 客户端初始化方法
	 */
	public void init()
	{
		try
		{
			//包装键盘输入
			in = new BufferedReader(new InputStreamReader(System.in));
			//连接到服务器
			socket = new Socket("127.0.0.1", PORT);
			//获得Socket相应的输入流和输出流
			ps = new PrintStream(socket.getOutputStream());
			br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			String line = null;
			
			while(true)
			{
				System.out.println("--------------------");
				System.out.println("	请选择：		");
				System.out.println("	（1）登录		");
				System.out.println("	（2）注册		");
				System.out.println("	（3）退出		");
				System.out.println("--------------------");
				
				//读取键盘输入
				line = in.readLine();
				if(line.equals("1"))
				{
					login();  //跳转到登录方法
					System.out.println("登录成功，已进入聊天室！");
					break;
				}
				else if (line.equals("2"))
				{
					register();   //跳转到注册方法
					break;
				}
				else if (line.equals("3"))  //退出程序
				{
					System.out.println("谢谢使用！");
					System.exit(1);
				}
				else 
				{
					System.out.println("输入错误，请重新输入！");
				}
			}
			
			/*
			 * 开启专门用于接收的线程
			 */
			
			//客户端线程
			new ClientThread(br).start();
			
		}catch (IOException e)
		{
			System.out.println("网络通信异常！");
			clientClose();
			System.exit(1);
		}
	}
	
	/**
	 * 客户端功能菜单的显示
	 */
	public void pattern()
	{
		try
		{
			while(true)
			{
				//让线程暂停半秒
				Thread.sleep(200);
				System.out.println("================");
				System.out.println("请选择功能：");
				System.out.println("1、公共聊天室");
				System.out.println("2、私聊");
				System.out.println("3、查看好友列表");
				System.out.println("4、查看私聊列表");
				System.out.println("5、查看黑名单列表");
				System.out.println("6、退出系统");
				System.out.println("================");
				
				String result;
				result = in.readLine();
				switch (result)
				{
				case "1":
					chat();  //进入公共聊天室
					break;
				case "2":
					privateChat();  //进入私聊模式
					break;
				case "3":
					ps.println(Identifier.ALL_FRI);   //发送查看用户所有好友的标识符
					break;
				case "4":
					ps.println(Identifier.ALL_PRI);   //发送查看用户所有私聊信息的标识符
					break;
				case "5":
					ps.println(Identifier.ALL_BAN);   //发送查看用户黑名单列表的标识符
					break;
				case "6":
					clientClose();  //关闭客户端资源并退出
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
			System.out.println("网络通信异常！");
			clientClose();
			System.exit(1);
		}
		
	}
	
	/**
	 * 专门用于关闭客户端资源的方法
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
	 * 用户进行注册的方法
	 */
	private synchronized void register()
	{
		try
		{
			/*
			 * 获取用户名模块
			 */
			String username = null;
			while(true)
			{
				System.out.print("请输入用户名：");
				username = in.readLine();
				
				//将用户名与协议符一起传送到服务器并读取服务器反馈
				ps.println(Identifier.USERNAME+username+Identifier.USERNAME);
				String result = br.readLine();
				
				if(result.equals(Identifier.USER_ERROR))
				{
					System.out.println("用户名重复，请重新注册！");
					continue;
				}
				
				
				/*
				 * 获取密码模块
				 */
				System.out.print("请输入密码：");
				String password = in.readLine();
				//将用户与密码与协议符一起传送到服务器
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
					System.out.println("注册失败，请重新注册！");
				}
				
			}
			
		} catch (IOException e)
		{
			System.out.println("网络通信异常！");
			clientClose();
			System.exit(1);
		}	
	}
	
	
	/**
	 * 用户登录的方法
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
				
				System.out.print("请输入用户名：");
				username = in.readLine();
				
				//将用户名与协议符一起传送到服务器并读取服务器反馈
				ps.println(Identifier.USERNAME+username+Identifier.USERNAME);
				result = br.readLine();
				System.out.println(result);
				if(result.equals(Identifier.SUCCESS))  //由于用户不存在会返回success
				{
					System.out.println("用户不存在!");
					continue;
				}
				
				System.out.print("请输入密码：");
				password = in.readLine();
					
				//将用户名和密码一起发送到服务器
				ps.println(Identifier.LOGIN+username
						+Identifier.SPARATOR+password+Identifier.LOGIN);
				
				result = br.readLine();
				if (result.equals(Identifier.HAS_LOGIN))
				{
					System.out.println("该用户已登录！");
					continue;
				}	
				if(result.equals(Identifier.PASS_ERROR))
				{
					System.out.println("密码错误，请重新登录！");
					continue;
				}
				if(result.equals(Identifier.SUCCESS))
				{
					break;
				}
			
			}
				
			
		} catch (IOException e)
		{
			System.out.println("网络通信异常！");
			clientClose();
			System.exit(1);
		}		
	}
	
	
	/**
	 * 用户进入公共聊天室的方法
	 */
	private void chat()
	{
		ps.println(Identifier.ADD_CHAT);
		String line = null;
		try
		{
			while((line = in.readLine()) != null)
			{
				//用户退出公共聊天室
				if (line.equals("bye"))
				{
					ps.println(Identifier.EXIT_CHAT);  //向服务器发送退出公共聊天室的标识符
					break;
				}
				//公共聊天室中全体在线成员
				else if (line.equals("AllInChat")) 
				{
					ps.println(Identifier.ALL_INCHAT);  //向服务器发送查看在线用户的标识符
				}
				//如果有带“TO”前缀
				else if(line.startsWith("TO")) 
				{
				    	//调用聊天室和私聊下的公用方法
					communal(line);
				}
				else
				{
				    	//若无特殊前缀，直接将用户信息当作群聊信息发送到服务器
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
	 * 用户开启私聊模式的方法
	 */
	private void privateChat()
	{
		String line = null;
		try
		{
			while((line = in.readLine()) != null)
			{
			    	//用户退出私聊模式
				if(line.equals("bye"))
				{
					break;
				}
				//用户发送带有标识符"TO"的信息
				if(line.startsWith("TO") )
				{
					communal(line);
				}
				else{
				    System.out.println("信息提示：输入格式有错！请重新输入！");
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 公用聊天室和私聊模块的公用方法
	 * 
	 * 添加黑名单
	 * 删除黑名单
	 * 好友申请
	 * 回复好友申请
	 * 私聊
	 */
	public void communal(String line)
	{
		//将用户添加到黑名单，格式 TO+用户名+BAN
		if(line.startsWith("TO") && line.endsWith("BAN")) 
		{
			//截掉头尾
			line = line.substring(2,line.length()-3);
			//往line头尾加标识符，并发送到服务器
			ps.println(Identifier.ADD_BAN + line + Identifier.ADD_BAN);
		}
		//删除黑名单，格式 TO+用户名+DEL
		else if(line.startsWith("TO") && line.endsWith("DEL"))
		{
			//截取头尾
			line = line.substring(2,line.length()-3);
			//往line头尾加标识符，并发送到服务器
			ps.println(Identifier.DEL_BAN+line
					+Identifier.DEL_BAN);
		}
		//好友申请，格式 TO+用户名+ADD
		else if(line.startsWith("TO") && line.endsWith("ADD"))
		{
			//截掉头尾
			line = line.substring(2, line.length()-3);
			//往line头尾加标识符，并发送到服务器
			ps.println(Identifier.ADD_FRI+line
					+Identifier.ADD_FRI);
		}
		//发送私聊信息,格式是TO+私聊对象+:+私聊信息+PRI、
		else if(line.startsWith("TO") && line.endsWith("PRI") && line.indexOf(":")>0)
		{
			//截掉头尾
			line = line.substring(2,line.length()-3);
			ps.println(Identifier.PRIVATE_MESSAGE+line.replaceFirst(":", Identifier.SPARATOR)
					+Identifier.PRIVATE_MESSAGE);
		}
		//对好友申请进行判断,将字符串“add”作为同意好友申请标志，格式 TO+申请的用户名+:+用户反馈信息+RES
		else if(line.startsWith("TO") &&  line.endsWith("RES") && line.indexOf(":")>0) 
		{
			//截掉头尾
			line = line.substring(2, line.length()-3);
			ps.println(Identifier.RESPONE_FRI+line.replace(":", Identifier.SPARATOR)
						+Identifier.RESPONE_FRI);
		}
	}
	
	/**
	 * 客户端主线程
	 * @param args
	 */
	public static void main(String[] args)
	{
		PClient pClient = new PClient();
		pClient.init();    //初始化
		pClient.pattern(); //功能列表
	}
	
}
