package server;

import model.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable{
    private ServerSocket serverSocket;
    private JLabel label;
    private JTextArea textArea;
    private String account;

    public ServerThread(ServerSocket serverSocket,JLabel label,JTextArea textArea,String account){
        this.serverSocket = serverSocket;
        this.label = label;
        this.textArea = textArea;
        this.account = account;
    }

    public void recivelogininfo(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message message = (Message)ois.readObject();
        account = message.getContent();
        System.out.println("接收到"+account);
    }

    public static void serversendmsg(Message message){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ServerCollection.get("123456").getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void run() {
        while (true){
            try {
                Socket s = serverSocket.accept();
                System.out.println("有客户端连接上服务器了");
                recivelogininfo(s);
                ServerReciveThread serverReciveThread = new ServerReciveThread(account,s,label,textArea);
                ServerCollection.add(account,serverReciveThread);
                Thread t = new Thread(serverReciveThread);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
