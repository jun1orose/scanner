package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Scanner");
        this.setSize(800,800);
        this.setLayout(new GridLayout());
        this.add(new TextBox());
        this.setVisible(true);
    }
}
