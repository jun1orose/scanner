package gui;

import core.Core;
import core.CustomTreeLeaf;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class FileTree extends JScrollPane {

    private Core core;
    private JTree fileTree;

    FileTree(Core core, JTree fileTree) {
        super();

        this.core = core;
        this.fileTree = fileTree;
        addLoadingFileByDoubleClick();

        setViewportView(fileTree);
        setOpaque(true);
    }

    private void addLoadingFileByDoubleClick() {
        fileTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                int selectedRow = fileTree.getRowForLocation(e.getX(), e.getY());
                TreePath selectedPath = fileTree.getPathForLocation(e.getX(), e.getY());

                if (selectedRow != -1) {
                    DefaultMutableTreeNode node = null;

                    if (selectedPath != null) {
                        node = (DefaultMutableTreeNode)selectedPath.getLastPathComponent();

                        if (node.isLeaf()) {
                            if (e.getClickCount() == 2) {
                                core.addNewTab((CustomTreeLeaf)node);
                            }
                        }
                    }
                }
            }
        });
    }

}
