package com.zjp.QQCopyClient.ClientService;

import java.util.HashMap;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class ManageClientConnectServerThread {
    private static HashMap<String,ClientConnectServerThread> hm = new HashMap<>();

    public static void addClientConnectServerThread(String userId ,ClientConnectServerThread thread)
    {
        hm.put(userId,thread);
    }

    public static ClientConnectServerThread getClientConnectServerThread(String userId)
    {
        return hm.get(userId);
    }
}
