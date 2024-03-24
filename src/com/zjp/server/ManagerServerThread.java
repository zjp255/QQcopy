package com.zjp.server;

import java.util.HashMap;
import java.util.Set;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class ManagerServerThread {
    private static HashMap<String,ServerThread> hm = new HashMap<>();

    public static HashMap<String, ServerThread> getHm() {
        return hm;
    }

    public static void addServerThread(String userid, ServerThread serverThread)
    {
        hm.put(userid,serverThread);
    }

    public static ServerThread getServerThread(String userid)
    {
        return hm.get(userid);
    }

    public static boolean deleteServerThread(String userid)
    {
        return hm.remove(userid) != null;
    }


    public static String getAllOnlineUserId()
    {
        String ans = "";
        Set<String> strings = hm.keySet();
        for(String key : strings)
        {
            ans += "用户：" + key + "\n";
        }
        return ans;
    }
}
