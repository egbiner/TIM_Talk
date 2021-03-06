package View;

import client.ChatContentcollection;
import client.ChatContentcollection2;
import client.ClientReciveThread;
import client.MyCellRenderer;
import dao.Groupdao;
import dao.Userdao;
import model.Message;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private JLabel Lable_username;
    private JLabel Lable_icon;
    private JPanel tp1;
    private JPanel tp2;
    private static String account;
    private static Socket s;

    public Main() throws SQLException {
//        JPanel_chatwindow.setVisible(false);
        Icon icon1 = new ImageIcon("D:\\Java课程设计\\TIM_Talk\\img\\g2.jpg");
        Icon icon2 = new ImageIcon("D:\\Java课程设计\\TIM_Talk\\img\\g2.jpg");
        Icon icon3 = new ImageIcon("D:\\Java课程设计\\TIM_Talk\\img\\g2.jpg");
        Icon[] icons = { icon1, icon2, icon3};
        list1.setCellRenderer(new MyCellRenderer(icons));

        ImageIcon imageIcon = new ImageIcon("D:\\Java课程设计\\TIM_Talk\\img\\g2.jpg");
        Lable_icon.setIcon(imageIcon);
        Lable_username.setText(Userdao.getusernamebyaccount(account));
        Thread t = new Thread(new ClientReciveThread(s, textArea_msglist, list1, list2, account));
        t.start();

        button_send.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Message message = new Message();
                message.setContent(textField_msgsend.getText());
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                message.setTime(sdf.format(date));
                try {
                    if (list1.getSelectedValue() == null && list2.getSelectedValue() != null) {
                        message.setType("group");
                        message.setGetter(list2.getSelectedValue().toString());

                        String content = ChatContentcollection2.getContent(Groupdao.getgroupnumber(list2.getSelectedValue().toString()));
                        content += "\t\t\t\tMe:" + message.getContent() + "\n\r";
                        ChatContentcollection2.addContent(Groupdao.getgroupnumber(list2.getSelectedValue().toString()), content);
                    } else if (list1.getSelectedValue() != null && list2.getSelectedValue() == null) {
                        message.setType("personal");
                        message.setGetter(list1.getSelectedValue().toString());

                        String content = ChatContentcollection.getContent(Userdao.getaccountbyusername(list1.getSelectedValue().toString()));
                        content += "\t\t\t\tMe:" + message.getContent() + "\n\r";
                        ChatContentcollection.addContent(Userdao.getaccountbyusername(list1.getSelectedValue().toString()), content);
                    } else {
                        textArea_msglist.append("选择用户列表错误！\n\r");
                        return;
                    }
                    message.setSender(account);
                } catch (Exception e1) {
                    textArea_msglist.append("请选择发送对象\n\r");
                    return;
                }
                try {
                    ClientReciveThread.clientsendmsg(message);
                } catch (IOException e1) {
                    textArea_msglist.append("与服务器连接断开，请重新尝试连接服务器！\n\r");
                    return;
                }
                textArea_msglist.append("\t\t\t\tMe:" + message.getContent() + "\n\r");
                textField_msgsend.setText("");
            }
        });


        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (list1.getSelectedValue()==null) {
                        return;
                    }
                    Lable_name.setText(list1.getSelectedValue().toString());
                    if (!list2.isSelectionEmpty()) {
                        list2.clearSelection();
                    }
                    try {
                        String content = ChatContentcollection.getContent(Userdao.getaccountbyusername(list1.getSelectedValue().toString()));
                        if (content == null) {
                            content = "";
                        }
                        textArea_msglist.setText(content);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        list2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    if (list2.getSelectedValue()==null) {
                        return;
                    }
                    Lable_name.setText(list2.getSelectedValue().toString());
                    if (!list1.isSelectionEmpty()) {
                        list1.clearSelection();
                    }
                    try {
                        String content = ChatContentcollection2.getContent(Groupdao.getgroupnumber(list2.getSelectedValue().toString()));
                        if (content==null){
                            content = "";
                        }
                        textArea_msglist.setText(content);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
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
