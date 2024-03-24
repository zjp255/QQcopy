package com.zjp.server;

import com.zjp.QQCopyCommon.Message;
import com.zjp.QQCopyCommon.MessageType;
import com.zjp.QQCopyCommon.User;
import com.zjp.Utility.DataUtility;
import com.zjp.Utility.SentMessageUtility;
import com.zjp.Utility.TimeUtility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class ServerThread extends Thread{
    private Socket socket = null;

    public Socket getSocket() {
        return socket;
    }

    private User user = null;
    private boolean isLogin = false;

    public ServerThread(Socket socket, User user)
    {
        this.socket = socket;
        this.user = user;
    }

    @Override
    public void run() {
        while(isLogin)
        {
            try {
                System.out.println("用户" + user.getUserId() + "已连接成功  正在监听");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message mes = (Message) ois.readObject();
                switch (mes.getMessageType())
                {
                    case MessageType.MESSAGE_GET_ONLINE_USER -> {SentOnlineUserMessage();}
                    case MessageType.MESSAGE_LOGOUT -> {isLogin = false;/*SentMessageUtility.sentMessage(socket,new Message(user.getUserId(), TimeUtility.getCurrentTime(),MessageType.MESSAGE_LOGOUT));*/}
                    case MessageType.NORMAL_CONTENT_MESSAGE_SIGNAL -> {privateChat(mes);}
                    case MessageType.NORMAL_CONTENT_MESSAGE_MULTIPLE -> {multipleChat(mes);}
                    case MessageType.MESSAGE_FILE -> {sendFile(mes);}
                }
            } catch (IOException | ClassNotFoundException e) {
                ManagerServerThread.deleteServerThread(user.getUserId());
                throw new RuntimeException(e);
            }
        }
        ManagerServerThread.deleteServerThread(user.getUserId());
        System.out.println("用户" + user.getUserId() + "已断开连接");
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendFile(Message message)
    {
        //BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        //byte[] bytes = bis.readAllBytes();

        ServerThread st = ManagerServerThread.getServerThread(message.getGetter());
        SentMessageUtility.sentMessage(st.socket,message);

        //BufferedOutputStream bos = new BufferedOutputStream(st.socket.getOutputStream());
        //bos.write(bytes);
    }

    public void multipleChat(Message message)
    {
        Collection<ServerThread> values = ManagerServerThread.getHm().values();
        for (ServerThread st : values)
        {
            SentMessageUtility.sentMessage(st.getSocket(),message);
        }
    }

    public void SentOnlineUserMessage(){
        SentMessageUtility.sentMessage(socket,new Message("system", user.getUserId(), ManagerServerThread.getAllOnlineUserId(),TimeUtility.getCurrentTime(),MessageType.MESSAGE_GET_ONLINE_USER));
    }
    public boolean userLogin()
    {
        if(DataUtility.findUser(user) && ManagerServerThread.getServerThread(user.getUserId()) == null)
        {
            //发送登录成功的消息
            SentMessageUtility.sentMessage(socket,new Message(user.getUserId(), TimeUtility.getCurrentTime(), MessageType.MESSAGE_LOGIN_SUCCEED));
            isLogin = true;
            return true;
        }
        else {
            //发送登录失败的消息
            SentMessageUtility.sentMessage(socket,new Message(user.getUserId(), TimeUtility.getCurrentTime(), MessageType.MESSAGE_LOGIN_FAIL));
            isLogin = false;
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public void privateChat(Message message)
    {
        ServerThread st = ManagerServerThread.getServerThread(message.getGetter());
        SentMessageUtility.sentMessage(st.getSocket(),message);
    }
}
