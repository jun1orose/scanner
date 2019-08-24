package gui;

import javax.swing.*;

class TextBox extends JComponent {

    TextBox() {
        super();
        this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(new ButtonsPanel());
    }
}
