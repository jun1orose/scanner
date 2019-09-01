package core;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

public class SyncTreeNode extends DefaultMutableTreeNode {

    private ReentrantLock lock = new ReentrantLock();

    SyncTreeNode(String fileName) {
        super(fileName);
    }

    public SyncTreeNode getChildByPath(Path filePath) {
        try {
            lock.lock();

            Iterator<TreeNode> childes = this.children().asIterator();
            SyncTreeNode child;

            while (childes.hasNext()) {
                child = (SyncTreeNode) childes.next();

                if (child.toString().equals(filePath.getName(0).toString())) {
                    return child;
                }
            }

            child = new SyncTreeNode(filePath.getName(0).toString());
            add(child);
            return child;

        } finally {
            lock.unlock();
        }
    }
}
