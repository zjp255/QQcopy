package com.zjp.QQCopyClient.ClientService;

import com.zjp.QQCopyCommon.Message;
import com.zjp.QQCopyCommon.MessageType;
import com.zjp.QQCopyCommon.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class UserClientService {
    private User user = null;
    private Socket socket = null;

    public boolean checkUser(String userId,String password)
    {
        boolean ans = false;
        user = new User(userId,password);

        //连接到客户端
        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            //new一个线程
            ClientConnectServerThread ccst = new ClientConnectServerThread(socket, user);
            ans = ccst.login();
            if(ans) {
                ccst.start();
                ManageClientConnectServerThread.addClientConnectServerThread(user.getUserId(), ccst);
            }
            else {
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ans;
    }
}
