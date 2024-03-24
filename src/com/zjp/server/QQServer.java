package com.zjp.server;

import com.zjp.QQCopyCommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class QQServer {
    //private Socket socket = null;
    private ServerSocket serverSocket = null;
    public QQServer()
    {
        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clientService()
    {
        //重复监听


            try {
                while (true) {
                    System.out.println("正在监听");
                    Socket socket = serverSocket.accept();
                    //通过输入流获得User类
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    User user = (User) ois.readObject();

                    //接收到连接后创建serverThread线程进行回应
                    ServerThread serverThread = new ServerThread(socket, user);
                    if(serverThread.userLogin()) {
                        serverThread.start();
                        ManagerServerThread.addServerThread(user.getUserId(), serverThread);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

    }
}
