package com.zjp.QQCopyClient.View;

import com.zjp.QQCopyClient.ClientService.ClientConnectServerThread;
import com.zjp.QQCopyClient.ClientService.ManageClientConnectServerThread;
import com.zjp.QQCopyClient.ClientService.UserClientService;
import com.zjp.QQCopyClient.Utility.UserInputUtility;
import com.zjp.QQCopyCommon.User;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class QQView {
    private boolean isViewLoginMenu = true;
    private String key = "";//接受控制台传入的数据

    private UserClientService userClientService = new UserClientService();

    private void showMainMenu(String userid)
    {
        boolean isRun = true;
        while (isRun){
            System.out.println("========主菜单(用户："+ userid +")========");
            System.out.println("\t\t1 显示在线用户列表");
            System.out.println("\t\t2 群发消息");
            System.out.println("\t\t3 私聊消息");
            System.out.println("\t\t4 发送文件");
            System.out.println("\t\t9 退出系统");
            System.out.println("请输入你的选择：");

            key = UserInputUtility.getInputKey(1);

            switch (key)
            {
                case "1":
                    System.out.println("====显示在线用户列表====");
                    ManageClientConnectServerThread.getClientConnectServerThread(userid).getOnlineUser();
                    System.out.println("====输入任意字符继续====");
                    UserInputUtility.getInputKey(1);
                    break;
                case "2":
                    System.out.println("====群发消息====");
                    System.out.println("请输入消息内容：");
                    String content1= UserInputUtility.getInputKey(50);
                    ManageClientConnectServerThread.getClientConnectServerThread(userid).sentMultipleNormalMessage(content1);
                    System.out.println("====输入任意字符继续====");
                    UserInputUtility.getInputKey(1);
                    break;
                case "3":
                    System.out.println("====私聊消息====");
                    System.out.println("请输入接收者：");
                    String getter = UserInputUtility.getInputKey(50);
                    System.out.println("请输入消息内容：");
                    String content= UserInputUtility.getInputKey(50);
                    ManageClientConnectServerThread.getClientConnectServerThread(userid).sentNormalMessage(getter,content);
                    System.out.println("====输入任意字符继续====");
                    UserInputUtility.getInputKey(1);
                    break;
                case "4":
                    System.out.println("====发送文件====");
                    System.out.println("请输入接收者：");
                    String fileGetter = UserInputUtility.getInputKey(50);
                    System.out.println("请输入文件路径（src/233.jpg）：");
                    String filePath = UserInputUtility.getInputKey(50);
                    ManageClientConnectServerThread.getClientConnectServerThread(userid).sentFileMessage(fileGetter,filePath);
                    System.out.println("====输入任意字符继续====");
                    UserInputUtility.getInputKey(1);
                    break;
                case "9":
                    isRun = false;
                    ManageClientConnectServerThread.getClientConnectServerThread(userid).logout();
                    break;
            }
        }
    }
    public void showLoginMenu()
    {
        while (isViewLoginMenu){
            System.out.println("========欢迎登录QQ========");
            System.out.println("\t\t1 登录系统");
            System.out.println("\t\t9 退出系统");
            System.out.println("请输入你的选择：");

            key = UserInputUtility.getInputKey(1);

            switch (key)
            {
                case "1":
                    System.out.println("请输入用户名：");
                    String userName = UserInputUtility.getInputKey(50);
                    System.out.println("请输入密  码：");
                    String password = UserInputUtility.getInputKey(50);

                    if(userClientService.checkUser(userName,password))
                    {
                        showMainMenu(userName);
                    }else{
                        System.out.println("密码错误，或用户不存在");
                    }
                    break;
                case "9":
                    isViewLoginMenu = false;
                    break;
            }
        }
    }
}
