package client;

import java.util.HashMap;

public class ChatContentcollection {
    private static HashMap<String,String> hashMap = new HashMap<>();

    /**
     *
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
