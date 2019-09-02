package gui;

import core.Core;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;

class SearchPanel extends JPanel {

    private Core core;

    private JTextField searchFilePath;
    private JTextField searchFileExt;
    private JTextField searchPattern;

    private final String defaultExt = ".log";
    private final String defaultFilePath = FileSystemView.getFileSystemView().getHomeDirectory().toPath().toString();

    SearchPanel(Core core) {
        super();
        this.core = core;
        createFields();
        addEnterKeyListeners();
    }

    private void createFields() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        searchFilePath = new JTextField(defaultFilePath);
        searchFileExt = new JTextField(defaultExt);
        searchPattern = new JTextField();

        JLabel path = new JLabel("Search Path");
        JLabel ext = new JLabel("File Ext");
        JLabel pattern = new JLabel("Search Pattern");

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(path)
                        .addComponent(searchFilePath))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(ext)
                        .addComponent(searchFileExt)))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(pattern)
                    .addComponent(searchPattern)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(path)
                    .addComponent(ext))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(searchFilePath)
                    .addComponent(searchFileExt))
                .addComponent(pattern)
                .addComponent(searchPattern));

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateGaps(true);
    }

    private void addEnterKeyListeners() {
        KeyAdapter keyAdapter = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (searchPattern.getText().length() != 0) {
                        if (searchFileExt.getText().length() == 0) {
                            searchFileExt.setText(defaultExt);
                            if (searchFilePath.getText().length() == 0) {
                                searchFilePath.setText(defaultFilePath);
                            }
                        }

                        core.doSearch(searchPattern.getText().toCharArray(),
                                Paths.get(searchFilePath.getText()), searchFileExt.getText());
                    }
                }
            }
        };

        searchPattern.addKeyListener(keyAdapter);
        searchFilePath.addKeyListener(keyAdapter);
        searchFileExt.addKeyListener(keyAdapter);
    }
}
