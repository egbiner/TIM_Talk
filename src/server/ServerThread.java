package server;

import dao.Userdao;
import model.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ServerThread implements Runnable{
    private ServerSocket serverSocket;
    private JLabel label;
    private JTextArea textArea;
    private String account;
    private static boolean isstart = true;
    private static JList list_users;
    private static JLabel Lable_usersnum;

    public ServerThread(ServerSocket serverSocket,JLabel label,JTextArea textArea,JList jList,JLabel jLabel,String account){
        this.serverSocket = serverSocket;
        this.label = label;
        this.textArea = textArea;
        this.account = account;
        list_users = jList;
        Lable_usersnum  = jLabel;

    }

    public void recivelogininfo(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message message = (Message)ois.readObject();
        account = message.getContent();
    }

    /**
     * 群发消息
     * @param message
     */
    public static void serversendmsg(Message message){
        try {
            String[] onlines = ServerCollection.GetOnline().split(" ");
            for (String online:onlines) {
                ObjectOutputStream oos = new ObjectOutputStream(ServerCollection.get(online).getSocket().getOutputStream());
                oos.writeObject(message);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void run() {
        while (isstart){
            try {
                Socket s = serverSocket.accept();
                recivelogininfo(s);
                ServerReciveThread serverReciveThread = new ServerReciveThread(account,s,label,textArea);
                ServerCollection.add(account,serverReciveThread);
                Thread t = new Thread(serverReciveThread);
                t.start();

                setonlines(ServerCollection.GetOnline());
//                sendonlines();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public static void closeserver(){
        isstart = false;
    }

    /**
     *服务器自己更新在线用户
     */
    public static void setonlines(String onlines) throws SQLException {
        String[] strings = onlines.split(" ");
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String s:strings) {
            listModel.addElement(Userdao.getusernamebyaccount(s));
        }
        Lable_usersnum.setText("在线人数:"+listModel.size());
        list_users.setModel(listModel);
    }

    /**
     * 向客户端发送更新在线用户
     */
    public static void sendonlines() throws IOException {
        Message message = new Message();
        message.setContent(ServerCollection.GetOnline());
        message.setType("setonline");
        String[] strings = ServerCollection.GetOnline().split(" ");
        for (String str:strings) {
            ServerReciveThread serverReciveThread = ServerCollection.get(str);
            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
            oos.writeObject(message);
        }
    }

    /**
     *服务器转发群消息 不包括发送消息的人
     */
    public static void sendmsgtoall(Message message) throws IOException {
        String[] onlines = ServerCollection.GetOnline().split(" ");
        for (String online:onlines) {
            if (online.equals(message.getSender())){ //跳过发送者
                continue;
            }
            ServerReciveThread serverReciveThread = ServerCollection.get(online);
            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
            oos.writeObject(message);
        }
    }
    /**
     * 服务器转发私人消息
     */
    public static void sendmsgpersonal(Message message) throws SQLException, IOException {
        ServerReciveThread serverReciveThread = ServerCollection.get(Userdao.getaccountbyusername(message.getGetter()));
        ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
        oos.writeObject(message);
    }
}
