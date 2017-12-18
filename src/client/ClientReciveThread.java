package client;

import model.Message;
import server.ServerCollection;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 此线程为接收通过服务器转发过来的消息线程
 */
public class ClientReciveThread implements Runnable{
    private static Socket s;
    private JTextArea textArea;

    public ClientReciveThread(Socket s, JTextArea textArea){
        this.s = s;
        this.textArea = textArea;
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
                textArea.append(message.getSender() +" : "+message.getContent()+"\n\r");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "与服务器连接断开，请重新登录", "连接失败", JOptionPane.PLAIN_MESSAGE);
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
