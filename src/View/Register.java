package View;

import dao.Userdao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class Register {
    private JPanel JPanel1;
    private JTextField textField_username;
    private JTextField textField_email;
    private JTextField textField_pwd1;
    private JTextField textField_pwd2;
    private JButton Button;

    public Register() {
        Button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String username = textField_username.getText();
                String pwd1 = new String(textField_pwd1.getText());
                String pwd2 = new String(textField_pwd2.getText());
                String email = textField_email.getText();
                if (username.length()<5){
                    return;
                }
                if (!pwd1.equals(pwd2)){
                    return;
                }
                //TODO
                try {
                    String status = Userdao.Register(username,pwd1,email);
                    if(status.equals("error")){
                        JOptionPane.showMessageDialog(null,"用户名:"+username+"+已被注册!");
                    }else {
                        JOptionPane.showMessageDialog(null,"注册成功\n\r账号:"+status+"\n\r密码:"+pwd1+"\n\r邮箱:"+email+"");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    public static void RunRegister(){
        JFrame frame = new JFrame("注册");
        frame.setContentPane(new Register().JPanel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550,450));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("R egister");
        frame.setContentPane(new Register().JPanel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550,450));
        frame.pack();
        frame.setVisible(true);
    }
}
