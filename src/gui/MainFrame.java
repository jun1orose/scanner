package gui;

import core.Core;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Scanner");

        this.setSize(800,800);

        addPanels();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void addPanels() {

        Tabs tabs = new Tabs();
        JTree tree = new JTree();
        Core core = new Core(tabs, tree);

        tree.setModel(null);
        JPanel leftPanel = new JPanel(new BorderLayout());
        SearchPanel searchPanel = new SearchPanel(core, tabs);
        FileTree fileTree = new FileTree(core, tree);

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(fileTree);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;

        gbc.weightx = 0.15f;
        gbc.weighty = 1f;

        add(leftPanel, gbc);

        gbc.weightx = 1f;

        add(tabs, gbc);
    }
}