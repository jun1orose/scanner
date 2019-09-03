package gui;

import core.FileInfo;
import core.SearchEngine;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.AdjustmentListener;

class TabTextArea extends JScrollPane {

    private final JTextArea textArea = new JTextArea();
    private AdjustmentListener listener;

    private final FileInfo fileInfo;
    private final SearchEngine searchEngine;

    TabTextArea(FileInfo fileInfo, SearchEngine searchEngine) {
        super();

        this.fileInfo = fileInfo;
        this.searchEngine = searchEngine;

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setDocument(this.fileInfo.getDoc());
        setViewportView(textArea);

        setLazyFileLoading();
        activateListener();
    }

    void toNextMatch() {
        disableListener();
        long next = searchEngine.getNextMatch();
        goToAndSelect(next);
    }

    void toPrevMatch() {
        disableListener();
        long next = searchEngine.getPrevMatch();
        goToAndSelect(next);
    }

    private void goToAndSelect(long match) {
        int actualPos = fileInfo.rollToMatchAndGetActualPos(match);

        try {
            textArea.requestFocusInWindow();
            Rectangle viewRect = textArea.modelToView(actualPos);
            textArea.scrollRectToVisible(viewRect);
            textArea.setCaretPosition(actualPos + searchEngine.getPatternLength());
            textArea.moveCaretPosition(actualPos);

        } catch (BadLocationException e) {
            e.printStackTrace();
        } finally {
            activateListener();
        }
    }

    private void disableListener() {
        getVerticalScrollBar().removeAdjustmentListener(listener);
    }

    private void activateListener() {
        SwingUtilities.invokeLater(() -> getVerticalScrollBar().addAdjustmentListener(listener));
    }

    private void setLazyFileLoading() {
        JScrollBar scroll = getVerticalScrollBar();
        BoundedRangeModel model = scroll.getModel();

        listener = (event) -> {

            if (!fileInfo.isLoading()) {

                int bound = model.getMaximum() / 10;

                if (model.getValue() - model.getMinimum() <= bound) {
                    fileInfo.loadUpperChunk();
                    return;
                }

                if (model.getMaximum() - (model.getValue() + model.getExtent()) <= bound) {
                    fileInfo.loadLowerChunk();
                }
            }
        };
    }

    void stopSearch() {
        searchEngine.stopSearch();
    }

    void startSearch() {
        searchEngine.fullSearch();
    }
}
