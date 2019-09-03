package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

class TabHeader extends JPanel {

    private String tabName;
    private JTabbedPane parentPane;
    private Tabs tabs;

    private static final int SIZE = 20;
    private static final Font FONT = new Font("Times New Roman", Font.PLAIN, TabHeader.SIZE * 2 / 3);


    TabHeader(String tabName, JTabbedPane parentPane, Tabs tabs) {
        super(new BorderLayout());

        this.tabName = tabName;
        this.parentPane = parentPane;
        this.tabs = tabs;

        setOpaque(false);
        setPreferredSize(new Dimension(TabHeader.SIZE * 6, TabHeader.SIZE));

        addTitle();
        addCloseButton();
        parentPane.addChangeListener(new ChangeTabListener());
    }

    private void addTitle() {

        JLabel label = new JLabel(this.tabName);

        label.setFont(TabHeader.FONT);
        label.setBorder(new EmptyBorder(0, 0, 0, TabHeader.SIZE));
        label.setPreferredSize(new Dimension(TabHeader.SIZE * 5, TabHeader.SIZE));
        label.setToolTipText(this.tabName);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                TabHeader.this.parentPane.
                        dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, TabHeader.this.parentPane));
            }
        });

        this.add(label, BorderLayout.LINE_START);
    }

    private void addCloseButton() {
        try {
            Image closeImg = ImageIO.read(new File("resources/close.png"))
                    .getScaledInstance(TabHeader.SIZE, TabHeader.SIZE, Image.SCALE_SMOOTH);

            JButton closeBtn = new JButton(new ImageIcon(closeImg));

            closeBtn.setPreferredSize(new Dimension(TabHeader.SIZE, TabHeader.SIZE));
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
            int index = parentPane.indexOfTabComponent(TabHeader.this);
            if (index >= 0) {
                parentPane.removeTabAt(index);
                tabs.removeTab(tabName);
            }
        }
    }

    final private class ChangeTabListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            int newTab = parentPane.getSelectedIndex();
            int thisTab = parentPane.indexOfTabComponent(TabHeader.this);

            if (newTab != -1 && thisTab != -1 && newTab != thisTab) {
                ((TabTextArea) parentPane.getComponentAt(thisTab)).stopSearch();
                ((TabTextArea) parentPane.getComponentAt(newTab)).startSearch();
            }
        }
    }
}