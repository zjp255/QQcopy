package com.zjp.QQCopyClient.Utility;

import com.zjp.QQCopyCommon.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class SentMessageUtility {
    public static boolean sentMessage(Socket socket, Message message)
    {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            //socket.shutdownOutput();
            //oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static Message getMessage(Socket socket)
    {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message mes = (Message) ois.readObject();
            return mes;
            //socket.shutdownOutput();
            //oos.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
