package com.qg.fangrui.util;

/**
 * 
 * @author Administrator
 * <pre>
 * 这是一个标识符协议
 * </pre>
 */

public interface Identifier
{
	
	String REGISTER="@";   //用户注册
	String LOGIN = "#";      //用户登录
	String USER_ERROR = "==="; //存在该用户名
	String PASS_ERROR = "---"; //用户密码错误
	String SUCCESS = "+++";    //成功 或用户不存在
	String ALL_MESSAGE = "ALL";      //发送给全体信息
 	String PRIVATE_MESSAGE = "PRI";  //私聊信息
 	String SPARATOR = "&";         //分隔符
 	String USERNAME = "￥";         //对用户名进行检查
 	String HAS_LOGIN = "%";         //已经登录
 	String EXIT="||";                 //用户退出    
 	String ADD_CHAT = "add_chat";     //加入公共聊天
 	String EXIT_CHAT = "exit_chat";   //退出公共聊天
 	String ADD_FRI = "add_fri";       //发送好友申请
 	String RESPONE_FRI = "respone_fri";   //好友申请回复
 	String ALL_FRI = "all_friends";       //获得所有的好友
 	String ALL_PRI = "all_private";       //获得所有的私聊
 	String ALL_BAN = "all_ban";           //获得所有的黑名单
 	String ADD_BAN = "add_ban";           //添加黑名单
 	String DEL_BAN = "delete_ban";        //删除黑名单
 	String ALL_INCHAT = "all_inchat";               //在线用户
 	
 	String ExitFromChat = "exit_from_chat";    //从聊天室中退出
 	String ExitFromProcedure = "exit_from_procedure";   //从程序中退出
 	
 	
 	
}
