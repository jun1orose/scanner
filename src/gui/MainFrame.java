package gui;

import core.Core;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private Core core;

    public MainFrame() {
        super("Scanner");

        this.setSize(800,800);
        this.setLayout(new GridLayout());

        Tabs tabs = new Tabs();
        this.core = new Core(tabs);
        this.add(tabs);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
