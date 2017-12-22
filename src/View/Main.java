package View;

import client.Chatwindowcollection;
import client.ClientReciveThread;
import dao.Userdao;
import model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class Main {
    private JPanel JPanel1;
    private JList list1;
    private JTextArea textArea_msglist;
    private JTextField textField_msgsend;
    private JButton button_send;
    private JLabel Lable_name;
    private JLabel Lable_usersnum;
    private JPanel JPanel_chatwindow;
    private JTabbedPane tabbedPane1;
    private JList list2;
    private static String account;
    private static Socket s;

    public Main() throws SQLException {
//        JPanel_chatwindow.setVisible(false);
        Thread t = new Thread(new ClientReciveThread(s,textArea_msglist,list1,list2,account));
        t.start();

        button_send.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Message message = new Message();
                message.setContent(textField_msgsend.getText());
                message.setType("personal");
                message.setSender(account);
                try {
                    message.setGetter(list1.getSelectedValue().toString());
                }catch (Exception e1){
                    textArea_msglist.append("请选择发送对象\n\r");
                    return;
                }

                try {
                    ClientReciveThread.clientsendmsg(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                textArea_msglist.append(textField_msgsend.getText()+"\n\r");
                textField_msgsend.setText("");
            }
        });

        list1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
//                System.out.println(list1.getAnchorSelectionIndex());
//                System.out.println(list1.getSelectedValue());
//                JPanel_chatwindow.setVisible(true);
                Lable_name.setText(list1.getSelectedValue().toString());
                Chatwindowcollection.addJPanel(account,JPanel_chatwindow);
            }
        });
        list2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Lable_name.setText(list2.getSelectedValue().toString());
            }
        });
    }

    public static void RunMain(String account1, Socket socket) throws SQLException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("TIM_Talk    "+new Userdao().getusernamebyaccount(account1));
        account = account1;
        s = socket;
        frame.setContentPane(new Main().JPanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000,600));
        frame.setLocation(200,200);
        frame.pack();
        frame.setVisible(true);

    }

}
