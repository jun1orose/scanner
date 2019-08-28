package gui;

import javax.swing.*;
import javax.swing.text.PlainDocument;

public class TabTextArea extends JScrollPane {

    private final JTextArea textArea = new JTextArea();
    private final PlainDocument doc = new PlainDocument();

    TabTextArea() {
        super();

        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.textArea.setDocument(doc);


        this.setViewportView(this.textArea);
    }
}
