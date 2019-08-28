package gui;

import javax.swing.*;
import java.awt.*;

class ContentBox extends JComponent {

    private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.LEFT);

    ContentBox() {
        super();
        this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.setLayout(new BorderLayout());

        this.add(new ButtonsPanel(), BorderLayout.NORTH);
        this.add(this.tabbedPane, BorderLayout.CENTER);

        this.tabbedPane.setTabPlacement(JTabbedPane.TOP);
        this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        addNewTab("/doc/tree/project");
        addNewTab("/add/swap/gen");
    }

    private void addNewTab(String tabName) {
        this.tabbedPane.addTab(tabName, new JPanel());
        this.tabbedPane.setTabComponentAt(this.tabbedPane.indexOfTab(tabName), new TabHeader(tabName, this.tabbedPane));
    }
}
