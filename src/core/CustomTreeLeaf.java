package core;

import java.nio.file.Path;

public class CustomTreeLeaf extends CustomTreeNode {

    private SearchEngine searchEngine;

    CustomTreeLeaf(Path filePath, SearchEngine searchEngine) {
        super(filePath);
        this.searchEngine = searchEngine;
    }

    SearchEngine getSearchEngine() {
        return searchEngine;
    }
}
