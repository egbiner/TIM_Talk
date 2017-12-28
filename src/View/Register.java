package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Register {
    private JPanel JPanel1;
    private JTextField textField_username;
    private JTextField textField_email;
    private JTextField textField_pwd1;
    private JTextField textField_pwd2;
    private JButton Button;

    public Register() {
        Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }
    public static void RunRegister(){
        JFrame frame = new JFrame("注册");
        frame.setContentPane(new Register().JPanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550,450));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("R egister");
        frame.setContentPane(new Register().JPanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550,450));
        frame.pack();
        frame.setVisible(true);
    }
}
