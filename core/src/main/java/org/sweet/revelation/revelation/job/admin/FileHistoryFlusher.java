package org.sweet.revelation.revelation.job.admin;

import jline.console.history.FileHistory;

import java.io.IOException;

public class FileHistoryFlusher implements Runnable {

    private final FileHistory fileHistory;

    public FileHistoryFlusher(FileHistory fileHistory) {
        this.fileHistory = fileHistory;
    }

    public void run() {
        try {
            fileHistory.flush();
        } catch (IOException e) {
        }
    }
}
