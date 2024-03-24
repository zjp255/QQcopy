package com.zjp.QQCopyClient.ClientService;

import com.zjp.QQCopyClient.Utility.SentMessageUtility;
import com.zjp.QQCopyClient.Utility.TimeUtility;
import com.zjp.QQCopyCommon.Message;
import com.zjp.QQCopyCommon.MessageType;
import com.zjp.QQCopyCommon.User;

import java.io.*;
import java.net.Socket;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class ClientConnectServerThread extends Thread{
    private Socket socket = null;

    private User user = null;
    private ObjectInputStream ois = null;

    private ObjectOutputStream oos = null;

    private String myFilePath = null;

    private boolean isAlive = false;
    public ClientConnectServerThread(Socket socket,User user)
    {
        this.socket = socket;
        this.user = user;
        myFilePath = "out/production/QQCopyClient/getFile/";
    }

    @Override
    public void run() {

        while (isAlive)
        {
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                Message mes = (Message) ois.readObject();
                //根据收到消息的类型做出不同的反应
                switch (mes.getMessageType())
                {
                    case MessageType.MESSAGE_GET_ONLINE_USER -> {System.out.println(mes.getContent());}
                    //case MessageType.MESSAGE_LOGOUT -> {isAlive = false;}
                    case MessageType.NORMAL_CONTENT_MESSAGE_SIGNAL -> {getNormalMessage(mes);}
                    case MessageType.NORMAL_CONTENT_MESSAGE_MULTIPLE -> {getMultipleNormalMessage(mes);}
                    case MessageType.MESSAGE_FILE -> {getFile(mes);}
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(ois != null && oos != null)
        {
            try {
                ois.close();
                oos.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getFile(Message mes) {
        try {
            //BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            //byte[] bytes = bis.readAllBytes();
            byte[] bytes = mes.getFileInfo();
            System.out.println(mes.getSender() + " 向 " + mes.getGetter() + " 发送文件 " + mes.getContent() + " (" + mes.getSendTime() + ") ");
            myFilePath += mes.getContent();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myFilePath));
            bos.write(bytes);
            bos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean login()
    {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(user);
            //socket.shutdownOutput();

            ois = new ObjectInputStream(socket.getInputStream());
            Message mes = (Message) ois.readObject();

            if(mes.getMessageType().equals(MessageType.MESSAGE_LOGIN_SUCCEED))
            {
                isAlive = true;
            }else if(mes.getMessageType().equals(MessageType.MESSAGE_LOGIN_FAIL)){
                System.out.println("登录失败");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return isAlive;
    }

    public void getOnlineUser()
    {
        SentMessageUtility.sentMessage(socket,new Message(user.getUserId(), TimeUtility.getCurrentTime(),MessageType.MESSAGE_GET_ONLINE_USER));
    }

    public void logout()
    {
        SentMessageUtility.sentMessage(socket,new Message(user.getUserId(), TimeUtility.getCurrentTime(),MessageType.MESSAGE_LOGOUT));
        System.exit(0);
    }

    public void sentNormalMessage(String getter, String content)
    {
        SentMessageUtility.sentMessage(socket,new Message(user.getUserId(), getter, content, TimeUtility.getCurrentTime(), MessageType.NORMAL_CONTENT_MESSAGE_SIGNAL));
    }

    public void sentMultipleNormalMessage(String content)
    {
        SentMessageUtility.sentMessage(socket,new Message(user.getUserId(), "All", content, TimeUtility.getCurrentTime(), MessageType.NORMAL_CONTENT_MESSAGE_MULTIPLE));
    }

    public void sentFileMessage(String getter, String content)
    {
        String[] split = content.split("/");

        BufferedInputStream fileBis = null;
        try {
            fileBis = new BufferedInputStream(new FileInputStream(content));
            byte[] bytes = fileBis.readAllBytes();
            Message message = new Message(user.getUserId(), getter, split[split.length - 1], TimeUtility.getCurrentTime(), MessageType.MESSAGE_FILE);
            message.setFileInfo(bytes);
            SentMessageUtility.sentMessage(socket, message);

            //BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            //bos.write(bytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(fileBis != null)
                    fileBis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void getNormalMessage(Message message)
    {
        String content = message.getSender() + " (" + message.getSendTime() + ") :" + message.getContent();
        System.out.println(content);
    }

    public void getMultipleNormalMessage(Message message)
    {
        String content = message.getSender() + " (" + message.getSendTime() + ") :" + message.getContent();
        System.out.println(content);
    }
}
