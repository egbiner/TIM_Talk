package client;

import javax.swing.*;
import java.awt.*;

public class MyCellRenderer extends JLabel implements ListCellRenderer {
    Icon[] icons;

    public MyCellRenderer() {
    }

    public MyCellRenderer(Icon[] icons) {
        // TODO Auto-generated constructor stub
        this.icons = icons;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String s = value.toString();
        setText(s);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));//加入宽度为5的空白边框
        if (isSelected) {
//            setBackground(list.getSelectionBackground());
//            setForeground(list.getSelectionForeground());
            setBackground(Color.ORANGE);
            setForeground(Color.WHITE);
        } else {
//            setBackground(list.getBackground());
//            setForeground(list.getForeground());
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }
        setIcon(icons[index]);//设置图片
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}