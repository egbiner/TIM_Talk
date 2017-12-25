package client;

import java.util.HashMap;

public class ChatContentcollection2 {
    private static HashMap<String,String> hashMap = new HashMap<>();

    /**
     *通过群号保存用户的群聊天记录
     */
    public static void addContent(String account,String content){
        hashMap.put(account,content);
    }
    /**
     *
     */
    public static String getContent(String account){
        return hashMap.get(account
        );
    }
}
