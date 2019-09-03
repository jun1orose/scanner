package core;

import gui.Tabs;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class Core {

    private ExecutorService service = Executors.newFixedThreadPool(5);

    private char[] currSearchPattern;
    private Path currSearchPath = FileSystemView.getFileSystemView().getHomeDirectory().toPath();
    private String currFileExt = ".log";

    private Tabs guiTabs;
    private JTree tree;

    private SyncTreeNode root;

    public Core(Tabs guiTabs, JTree tree) {
        this.guiTabs = guiTabs;
        this.tree = tree;
    }

    Future submitTask(Runnable task) {
        return service.submit(task);
    }

    private void testFile(Path filePath) {

        submitTask(() -> {
            SearchEngine searchEngine = SearchEngine.construct(this, filePath, currSearchPattern);

            if (searchEngine != null) {
                if (root == null) {
                    reloadModel(currSearchPath);
                }
                addFileToTree(filePath, searchEngine);
            }
        });
    }

    private void addFileToTree(Path filePath, SearchEngine searchEngine) {

        CustomTreeLeaf newFile = new CustomTreeLeaf(filePath, searchEngine);
        Path nextFilePath = filePath.subpath(currSearchPath.getNameCount(), filePath.getNameCount());

        recursiveTreeWalk(nextFilePath, root, newFile);
    }

    private void recursiveTreeWalk(Path filePath, SyncTreeNode parent, DefaultMutableTreeNode leaf) {

        if (leaf.toString().equals(filePath.toString())) {
            parent.add(leaf);
            return;
        }

        SyncTreeNode child = parent.getChildByPath(filePath);
        recursiveTreeWalk(filePath.subpath(1, filePath.getNameCount()), child, leaf);
    }

    public void addNewTab(CustomTreeLeaf file) {
        file.getSearchEngine().fullSearch();
        guiTabs.addNewTab(new FileInfo(this, file.getFilePath()), file.getSearchEngine());
    }

    public void doSearch(char[] newSearchPattern, Path newSearchPath, String newFileExt) {

        if (Files.exists(newSearchPath)) {

            clearTreeView();
            submitTask(() -> {

                currSearchPattern = newSearchPattern;
                currSearchPath = newSearchPath;
                currFileExt = newFileExt;

                try (Stream<Path> search = Files.walk(currSearchPath)) {

                    search.filter(Files::isRegularFile)
                            .filter(f -> f.getFileName().toString().endsWith(currFileExt))
                            .forEach(this::testFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void reloadModel(Path currSearchPath) {
        root = new SyncTreeNode(currSearchPath.toString());
        tree.setModel(new DefaultTreeModel(root));
    }

    private void clearTreeView() {
        root = null;
        tree.setModel(null);
    }
}
