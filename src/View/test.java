package View;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class test {
    private JPanel Jpanel1;
    private JButton button1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        frame.setContentPane(new test().Jpanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public test() {
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String c = JOptionPane.showInputDialog(null,"请输入你的爱好：\n","title",JOptionPane.PLAIN_MESSAGE);
                System.out.println(c);
            }
        });
    }
}
