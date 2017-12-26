package client;

import dao.Groupdao;
import dao.Userdao;
import model.Message;
import server.ServerCollection;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * 此线程为接收通过服务器转发过来的消息线程
 */
public class ClientReciveThread implements Runnable{
    private static Socket s;
    private static JTextArea textArea;
    private static JList jList_onlines;
    private static JList jList_groups;
    private static String account;

    public ClientReciveThread(Socket socket, JTextArea jtextArea,JList jList,JList jList2,String ac) throws SQLException {
        s = socket;
        textArea = jtextArea;
        jList_onlines = jList;
        jList_groups = jList2;
        account = ac;

        initfriendgroup();
    }

    public static void initfriendgroup() throws SQLException {
        String[] friend = Userdao.getfriend(account).split(" ");
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String f:friend) {
            listModel.addElement(Userdao.getusernamebyaccount(f));
        }
        jList_onlines.setModel(listModel);

        String[] groups = Userdao.getgroup(account).split(" ");
        DefaultListModel<String> listModel2 = new DefaultListModel<String>();
        for (String g:groups) {
            listModel2.addElement(Groupdao.getgoupename(g));
        }
        jList_groups.setModel(listModel2);
    }

    public static void clientsendmsg(Message message) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.writeObject(message);
    }

    public void run() {
        while (true){
            try {
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message message = (Message)ois.readObject();
                if (message.getType().equals("setonline")){
                    setonlines(message.getContent());
                    continue;
                }
                /**
                 * 思路：接收服务器转发过来的消息
                 * 然后从集合中去除对应发送者账号的集合
                 * 将接收到的信息添加到集合里面
                 * 然后获取集合的内容展示
                 */
                if (message.getType().equals("personal")) {
                    String content = ChatContentcollection.getContent(message.getSender());
                    if (content == null) {
                        content = "";
                    }
                    content += Userdao.getusernamebyaccount(message.getSender()) + "  " + message.getTime() + " \n\r" + message.getContent() + "\n\r";
                    ChatContentcollection.addContent(message.getSender(), content);
                    textArea.setText(ChatContentcollection.getContent(message.getSender()));
                    //选中当前发送消息的人
                    jList_onlines.setSelectedValue(Userdao.getusernamebyaccount(message.getSender()),true);

                }else if (message.getType().equals("group")){
                    //TODO 群发
                    //此处不应该为getSender
                    //message.getGetter()为群号
                    String content = ChatContentcollection2.getContent(Groupdao.getgroupnumber(message.getGetter()));
                    if (content == null) {
                        content = "";
                    }
                    content += Userdao.getusernamebyaccount(message.getSender()) + "  " + message.getTime() + " \n\r " + message.getContent() + "\n\r";
                    ChatContentcollection2.addContent(Groupdao.getgroupnumber(message.getGetter()), content);

                    textArea.setText(ChatContentcollection2.getContent(Groupdao.getgroupnumber(message.getGetter())));
                    jList_groups.setSelectedValue(message.getGetter(),true);

                }else if (message.getType().equals("System")){
                    textArea.append(message.getSender()+"  " + message.getTime() + " \n\r " + message.getContent() + "\n\r");
                }

//                textArea.append(Userdao.getusernamebyaccount(message.getSender()) + "  " + message.getTime()+ " \n\r" + message.getContent() + "\n\r");

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "与服务器连接断开，请重新登录", "连接失败", JOptionPane.PLAIN_MESSAGE);
                textArea.append("与服务器断开连接！\n\r");
                try {
                    Userdao.putlogin(account);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setonlines(String onlines) throws SQLException {
        String[] strings = onlines.split(" ");
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String s:strings) {
            listModel.addElement(Userdao.getusernamebyaccount(s));
        }
        jList_onlines.setModel(listModel);
    }


}
