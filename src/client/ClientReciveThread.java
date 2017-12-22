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
    private JTextArea textArea;
    private static JList jList_onlines;
    private static JList jList_groups;
    private static String account;

    public ClientReciveThread(Socket socket, JTextArea textArea,JList jList,JList jList2,String ac) throws SQLException {
        s = socket;
        this.textArea = textArea;
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
                textArea.append(Userdao.getusernamebyaccount(message.getSender()) + " : " + message.getContent() + "\n\r");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "与服务器连接断开，请重新登录", "连接失败", JOptionPane.PLAIN_MESSAGE);
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
