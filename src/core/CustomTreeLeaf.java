package core;

import javax.swing.tree.DefaultMutableTreeNode;
import java.nio.file.Path;

public class CustomTreeLeaf extends DefaultMutableTreeNode {

    private SearchEngine searchEngine;
    private Path filePath;

    CustomTreeLeaf(Path filePath, SearchEngine searchEngine) {
        super(filePath.getFileName());
        this.filePath = filePath;
        this.searchEngine = searchEngine;
    }

    SearchEngine getSearchEngine() {
        return searchEngine;
    }

    Path getFilePath() {
        return filePath;
    }
}
