package gui;

import javax.swing.*;

class TextFrame extends JComponent {

    TextFrame() {
        super();
        this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(new ButtonsPanel());
    }
}
