package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerCollection {
    private static Map<String, ServerReciveThread> map = new HashMap<String, ServerReciveThread>();

    public static void add(String username, ServerReciveThread serverReciveThread){
        map.put(username, serverReciveThread);
    }

    /**
     * 根据用户名返回服务器端与客户端通信的线程的方法
     * @param Name 用户名
     * @return 返回服务器端与客户端通信的线程
     */
    public static ServerReciveThread get(String Name){
        return map.get(Name);
    }

    /**
     * 根据用户名移除服务器端与客户端通信的线程的方法
     * 同时将其设置为没有登陆的状态
     * @param Name
     */
    public static void remove(String Name){
        map.remove(Name);
    }

    /**
     * 遍历集合返回在线用户
     * @return 带有在线用户的字符串
     */
    public static String GetOnline(){
        String Online = "";
        Iterator<String> it  = map.keySet().iterator();
        while(it.hasNext()){
            Online += it.next().toString()+" ";
        }
        return Online;
    }
}
