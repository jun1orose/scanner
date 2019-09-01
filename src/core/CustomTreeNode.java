package core;

import javax.swing.tree.DefaultMutableTreeNode;
import java.nio.file.Path;

class CustomTreeNode extends DefaultMutableTreeNode {

    private Path filePath;

    CustomTreeNode(Path filePath) {
        super(filePath.getFileName().toString());
    }

    Path getFilePath() {
        return filePath;
    }
}
