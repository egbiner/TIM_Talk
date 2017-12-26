package View;

import dao.Userdao;
import model.Message;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class Login {
    private JPanel JPanel1;
    private JButton LoginButton;
    private JTextField textFieldAccount;
    private JPasswordField passwordField;
    private static JFrame jFramemod;
    private String account;
    private String password;

    public Login() {
        LoginButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                account = textFieldAccount.getText();
                password = new String(passwordField.getPassword());
                User u = new User(account,password);
                try {
                    if(Userdao.login(u)==1&&Userdao.islogin(u.getAccount())==0){
                        //打开主窗口
                        Socket s = new Socket("127.0.0.1",2333);
                        //发送登录信息给服务器
                        sendlogininfo(s);
                        Main.RunMain(account,s);
                        Userdao.setlogin(u.getAccount());
                        jFramemod.dispose();
                    }else if (Userdao.islogin(u.getAccount())==1){
                        JOptionPane.showMessageDialog(null, "用户已经登录", "登陆失败", JOptionPane.PLAIN_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "用户名或密码错误", "登陆失败", JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "请检查服务器状态", "登陆失败", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
    }

    public void sendlogininfo(Socket s) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        Message message = new Message();
        message.setContent(account);
        oos.writeObject(message);
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        JFrame frame = new JFrame("Login");
        jFramemod = frame;
        frame.setContentPane(new Login().JPanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,320));
        frame.setLocation(250,350);
        frame.pack();
        frame.setVisible(true);
    }
}
