package server;

import model.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReciveThread implements Runnable{
    private Socket s;
    private JTextArea textArea;
    private JLabel label;
    private String account;

    public ServerReciveThread(String account,Socket s,JLabel label,JTextArea textArea){
        this.account = account;
        this.label = label;
        this.textArea = textArea;
        this.s = s;
    }

    public Socket getSocket(){
        return s;
    }

    public void run() {
        while(true){
            try {
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message message = (Message) ois.readObject();

                textArea.append(message.getContent()+"\n\r");

            } catch (IOException e) {
                System.out.println("客户端断开就会自动调用here");
                textArea.append("客户端已经断开连接\n\r");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
