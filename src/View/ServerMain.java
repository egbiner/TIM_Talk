package View;

import model.Message;
import server.ServerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerMain {
    private JPanel JPanle1;
    private JList list_users;
    private JButton Button_start;
    private JButton Button_stop;
    private JTextArea textArea1_msglist;
    private JTextField textField1_msgwrite;
    private JButton button_sendmsg;
    private JLabel Lable_usersnum;
    private JLabel Lable_serverisstart;
    private JTabbedPane tabbedPane1;
    private JTextArea textArea2_state;
    private JTextField textFiled_port;
    private Socket s;
    private ServerSocket serverSocket;

    public ServerMain() {
        Button_start.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    int port = Integer.parseInt(textFiled_port.getText());
                    serverSocket = new ServerSocket(port);
                    String account ="";
                    Thread t = new Thread(new ServerThread(serverSocket,Lable_usersnum,textArea1_msglist,list_users,Lable_usersnum,account,textArea2_state));
                    t.start();
                    Button_start.setEnabled(false);
                    textFiled_port.setEnabled(false);
                    Lable_serverisstart.setText("运行中");
                    textArea2_state.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"   服务器开启!\n\r");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        Button_stop.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ServerThread.closeserver();
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Lable_serverisstart.setText("已停止");
                Button_start.setEnabled(true);
            }
        });
        button_sendmsg.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Message message = new Message();
                message.setContent(textField1_msgwrite.getText());
                message.setSender("系统消息");
                message.setType("System");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                message.setTime(sdf.format(date));
                ServerThread.serversendmsg(message);
                textArea1_msglist.append(message.getContent()+"\n\r");
                textField1_msgwrite.setText("");
            }
        });
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        JFrame frame = new JFrame("ServerMain");
        frame.setContentPane(new ServerMain().JPanle1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000,600));
        frame.setLocation(800,250);
        frame.pack();
        frame.setVisible(true);
    }
}
