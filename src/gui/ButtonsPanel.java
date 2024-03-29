package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class ButtonsPanel extends JPanel {

    private JTabbedPane tabs;

    ButtonsPanel(JTabbedPane tabs) {
        this.tabs = tabs;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        addNavigationButtons();
    }

    private void addNavigationButtons() {
        try {
            this.add(createUpButton());
            this.add(createDownButton());

            JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
            separator.setPreferredSize(new Dimension(2, 20));

            this.add(separator);
            this.add(createSelectAllButton());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JButton createUpButton() throws IOException {
        Image upImg = ImageIO.read(new File("resources/up.png"))
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        JButton upBtn = new JButton(new ImageIcon(upImg));
        upBtn.addActionListener(actionEvent -> {
            if (tabs.getSelectedComponent() != null) {
                ((TabTextArea) tabs.getSelectedComponent()).toPrevMatch();
            }
        });

        return upBtn;
    }

    private JButton createDownButton() throws IOException {
        Image downImg = ImageIO.read(new File("resources/down.png"))
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        JButton downBtn = new JButton(new ImageIcon(downImg));
        downBtn.addActionListener(actionEvent -> {
            if (tabs.getSelectedComponent() != null) {
                ((TabTextArea) tabs.getSelectedComponent()).toNextMatch();
            }
        });

        return downBtn;
    }

    private JButton createSelectAllButton() throws IOException {
        Image allImg = ImageIO.read(new File("resources/all.png"))
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        JButton selectAllBtn = new JButton(new ImageIcon(allImg));
        selectAllBtn.setEnabled(false);
        selectAllBtn.setToolTipText("Select all");

        return selectAllBtn;
    }
}
