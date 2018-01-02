package client;

import dao.Userdao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws SQLException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(simpleDateFormat.format(date));
//        String content = ChatContentcollection.getContent("123456");
//        System.out.println(content);
        System.out.println((int)((Math.random()*9+1)*100000));
//        System.out.println(Userdao.Register("大哥大","asdasd","adsasd"));
    }
}
