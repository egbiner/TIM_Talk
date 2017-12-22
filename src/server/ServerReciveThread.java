package server;

import client.ClientReciveThread;
import dao.Userdao;
import model.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.SQLException;

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
                /**
                 * 实现群发
                 */
                if (message.getType().equals("group")){
                    ServerThread.sendmsgtoall(message);
                }else if (message.getType().equals("personal")){
                    ServerThread.sendmsgpersonal(message);
                }

                textArea.append(Userdao.getusernamebyaccount(message.getSender())+": to :"+message.getGetter()+":"+message.getContent()+"\n\r");

            } catch (IOException e) {
                textArea.append("客户端"+account+"已经断开连接\n\r");
                ServerCollection.remove(account);

                try {
                    ServerThread.setonlines(ServerCollection.GetOnline());
//                    ServerThread.sendonlines();
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
}
