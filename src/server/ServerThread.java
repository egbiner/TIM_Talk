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

                setonlines(ServerCollection.GetOnline());

                Thread t = new Thread(serverReciveThread);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                //setonlines
                e.printStackTrace();
            }

        }
    }

    public static void closeserver(){
        isstart = false;
    }

    public static void setonlines(String onlines) throws SQLException {
        String[] strings = onlines.split(" ");
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String s:strings) {
            listModel.addElement(Userdao.getusernamebyaccount(s));
        }
        Lable_usersnum.setText("在线人数:"+listModel.size());
        list_users.setModel(listModel);
    }
}
