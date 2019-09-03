package gui;

import core.FileInfo;
import core.SearchEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.ArrayList;
import java.util.List;

public class Tabs extends JComponent {

    private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.LEFT);
    private List<String> openedTabs = new ArrayList<>();

    Tabs() {
        super();
        setBorder(BorderFactory.createRaisedSoftBevelBorder());
        setLayout(new BorderLayout());

        add(new ButtonsPanel(tabbedPane), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        setTabRemovingListener();
    }

    public void addNewTab(FileInfo fileInfo, SearchEngine searchEngine) {
        String fileName = fileInfo.getFileName();

        if (!openedTabs.contains(fileName)) {
            openedTabs.add(fileName);
            TabTextArea tabTextArea = new TabTextArea(fileInfo, searchEngine);

            tabbedPane.addTab(fileName, tabTextArea);
            tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(fileName),
                    new TabHeader(fileName, tabbedPane, this));
        }
    }

    void removeTab(String tab) {
        openedTabs.remove(tab);
    }

    void removeAllTabs() {
        openedTabs.clear();
        tabbedPane.removeAll();
    }

    private void setTabRemovingListener() {
        tabbedPane.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentRemoved(ContainerEvent e) {
                ((TabTextArea) e.getChild()).stopSearch();
            }
        });
    }
}
