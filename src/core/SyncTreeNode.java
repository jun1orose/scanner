package core;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.concurrent.locks.ReentrantLock;

public class SyncTreeNode extends DefaultMutableTreeNode {

    ReentrantLock lock = new ReentrantLock();

    SyncTreeNode(String fileName) {
        super(fileName);
    }

    @Override
    public void add(MutableTreeNode newChild) {
        try {
            lock.lock();
            super.add(newChild);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Enumeration<TreeNode> children() {
        try {
            lock.lock();
            return super.children();
        } finally {
            lock.unlock();
        }
    }
}
