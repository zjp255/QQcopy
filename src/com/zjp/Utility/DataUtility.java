package com.zjp.Utility;

import com.zjp.QQCopyCommon.User;

import java.io.*;
import java.util.HashMap;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class DataUtility {
    private static String dataFilePath = "src\\userData.dat";

    private static HashMap<String ,User> hm = new HashMap<>();
    //查找user
    public static boolean findUser(User user)
    {
        User ans = selectUser(user.getUserId());
        if (ans != null) {
            if (ans.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    //查找user
    public static User selectUser(String userid)
    {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFilePath));
             hm = (HashMap<String ,User>)ois.readObject();
            ois.close();
        } catch (Exception e) {
            return null;
        }
        return hm.get(userid);
    }

    //将user保存到数据库中
    public static boolean saveUser(User user)
    {
        //将user写入文件末尾
       if(hm.get(user.getUserId()) == null) {
            hm.put(user.getUserId(), user);
           ObjectOutputStream oos = null;
           try {
               oos = new ObjectOutputStream(new FileOutputStream(dataFilePath));
               oos.writeObject(hm);
           } catch (IOException e) {
               throw new RuntimeException(e);
           } finally {
               try {
                   oos.close();
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           }
           return true;
       }else {
           return false;
       }
    }

    public static void main(String[] args) {
        saveUser(new User("123","123456"));
        saveUser(new User("200","123456"));
        saveUser(new User("300","123456"));
        saveUser(new User("孙悟空","123456"));
        saveUser(new User("唐僧","123456"));
        saveUser(new User("玉帝","123456"));
    }
}
