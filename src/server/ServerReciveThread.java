package server;

import dao.Userdao;
import model.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerReciveThread implements Runnable{
    private Socket s;
    private JTextArea textArea;
    private JLabel label;
    private String account;
    private JTextArea textArea_state;
    private static boolean isrun = true;

    public ServerReciveThread(String account,Socket socket,JLabel label,JTextArea textArea,JTextArea textArea2_state){
        this.account = account;
        this.label = label;
        this.textArea = textArea;
        this.s = socket;
        this.textArea_state = textArea2_state;
    }

    public Socket getSocket(){
        return s;
    }

    public void run() {
        while(isrun){
            try {
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message message = (Message) ois.readObject();
                /**
                 * 根据消息类型实现不同的转发方式
                 */
                if (message.getType().equals("group")){
                    ServerThread.sendmsgtoall(message);
                }else if (message.getType().equals("personal")){
                    ServerThread.sendmsgpersonal(message);
                }else {
                    textArea.append("获取用户信息发送类型发生错误!\n\r");
                    textArea_state.append("Error：获取接收信息类型错误！\n\r");
                }

                textArea.append(Userdao.getusernamebyaccount(message.getSender())+": to :"+message.getGetter()+": "+message.getContent()+"\n\r");

            } catch (IOException e) {
                textArea_state.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"    用户:" +account+ "断开连接！\n\r");
                ServerCollection.remove(account);
                try {
                    Userdao.putlogin(account);
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

    public void closeThread() throws IOException {
        isrun = false;
        s.close();
    }
}
