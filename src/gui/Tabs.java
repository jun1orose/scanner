package gui;

import core.FileInfo;

import javax.swing.*;
import java.awt.*;

public class Tabs extends JComponent {

    private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.LEFT);

    Tabs() {
        super();
        this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.setLayout(new BorderLayout());

        this.add(new ButtonsPanel(), BorderLayout.NORTH);
        this.add(this.tabbedPane, BorderLayout.CENTER);

        this.tabbedPane.setTabPlacement(JTabbedPane.TOP);
        this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public void addNewTab(FileInfo fileInfo) {
        String fileName = fileInfo.getFileName();
        TabTextArea tabTextArea = new TabTextArea(fileInfo);

        this.tabbedPane.addTab(fileName, tabTextArea);
        this.tabbedPane.setTabComponentAt(this.tabbedPane.indexOfTab(fileName), new TabHeader(fileName, this.tabbedPane));
    }
}
