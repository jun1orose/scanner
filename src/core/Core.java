package core;

import gui.Tabs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Core {

    private ExecutorService service = Executors.newFixedThreadPool(5);

    private String prevSearch;
    private Path prevSearchPath;
    private String prevFileExt;

    private Tabs guiTabs;

    public Core(Tabs guiTabs) {
        this.guiTabs = guiTabs;

        Path resPath = Paths.get("/home/kinddarose/dev/projects/scanner/resources/sampleFICT.txt");
        this.guiTabs.addNewTab(new FileInfo(this, resPath));
    }

    void submitTask(Runnable task) {
        this.service.submit(task);
    }


}
