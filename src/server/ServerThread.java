package server;

import dao.Groupdao;
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
    private static JTextArea textArea;
    private String account;
    private static boolean isstart = true;
    private static JList list_users;
    private static JLabel Lable_usersnum;

    public ServerThread(ServerSocket serverSocket,JLabel label,JTextArea jtextArea,JList jList,JLabel jLabel,String account){
        this.serverSocket = serverSocket;
        this.label = label;
        textArea = jtextArea;
        this.account = account;
        list_users = jList;
        Lable_usersnum  = jLabel;

    }

    public void recivelogininfo(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message message = (Message)ois.readObject();
        account = message.getContent();
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
            if (s.equals("")){
                break;
            }
            listModel.addElement(Userdao.getusernamebyaccount(s));
        }
        Lable_usersnum.setText("在线人数:"+listModel.size());
        list_users.setModel(listModel);
    }
    /**
     * 群发消息
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
    public static void sendmsgtoall(Message message) throws IOException, SQLException {
//        String[] onlines = ServerCollection.GetOnline().split(" ");
//        for (String online:onlines) {
//            if (online.equals(message.getSender())){ //跳过发.送者
//                continue;
//            }
//            ServerReciveThread serverReciveThread = ServerCollection.get(online);
//            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
//            oos.writeObject(message);
//        }
        String[] users = Groupdao.getMembers(Groupdao.getgroupnumber(message.getGetter())).split(" ");
        for (String user:users) {
            if (user.equals(message.getSender())){
                continue;
            }
            ServerReciveThread serverReciveThread = ServerCollection.get(user);
            //当用户不在线的时候 不发送
            if (serverReciveThread==null){
                continue;
            }
            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
            oos.writeObject(message);
        }
    }
    /**
     * 服务器转发私人消息
     */
    public static void sendmsgpersonal(Message message) throws SQLException, IOException {
        ServerReciveThread serverReciveThread = ServerCollection.get(Userdao.getaccountbyusername(message.getGetter()));
        try {
            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
            oos.writeObject(message);
        }catch (Exception e){
            textArea.append(message.getGetter() + "未上线!\n\r");
        }
    }
}
