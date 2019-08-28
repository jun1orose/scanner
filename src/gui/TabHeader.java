package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

class Tab extends JPanel {

    private String tabName;
    private JTabbedPane parentPane;

    private static final int SIZE = 20;
    private static final Font FONT = new Font("Times New Roman", Font.PLAIN, Tab.SIZE * 2 / 3);


    Tab(String tabName, JTabbedPane parentPane) {
        super(new BorderLayout());

        this.tabName = tabName;
        this.parentPane = parentPane;

        setOpaque(false);
        setPreferredSize(new Dimension(Tab.SIZE * 6, Tab.SIZE));

        addTitle();
        addCloseButton();
    }

    private void addTitle() {

        JLabel label = new JLabel(this.tabName);

        label.setFont(Tab.FONT);
        label.setBorder(new EmptyBorder(0, 0, 0, Tab.SIZE));
        label.setPreferredSize(new Dimension(Tab.SIZE * 5, Tab.SIZE));
        label.setToolTipText(this.tabName);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Tab.this.parentPane.
                        dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, Tab.this.parentPane));
            }
        });

        this.add(label, BorderLayout.LINE_START);
    }

    private void addCloseButton() {
        try {
            Image closeImg = ImageIO.read(new File("resources/close.png"))
                    .getScaledInstance(Tab.SIZE, Tab.SIZE, Image.SCALE_SMOOTH);

            JButton closeBtn = new JButton(new ImageIcon(closeImg));

            closeBtn.setPreferredSize(new Dimension(Tab.SIZE, Tab.SIZE));
            closeBtn.addActionListener(new CloseButtonAction());
            closeBtn.setContentAreaFilled(false);
            closeBtn.setBorderPainted(false);

            this.add(closeBtn, BorderLayout.LINE_END);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final private class CloseButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int index = Tab.this.parentPane.indexOfTabComponent(Tab.this);
            if (index >= 0) {
                Tab.this.parentPane.removeTabAt(index);
            }
        }
    }
}
