package client;

import javax.swing.*;
import java.util.HashMap;

public class Chatwindowcollection {
    private static HashMap<String,JPanel> hashMap = new HashMap<>();

    /**
     * 添加JPanle及进集合中
     */
    public static void addJPanel(String account,JPanel jPanel){
        hashMap.put(account,jPanel);
    }
    /**
     * 根据账号返回JPanle
     */
    public static JPanel getJPanlebyaccount(String account){
        return hashMap.get(account
        );
    }
}
