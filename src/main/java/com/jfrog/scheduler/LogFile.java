package com.jfrog.scheduler;

import java.io.File;
import java.io.IOException;

public class LogFile {

    private static File LogFile;


    public File getLogFile() {
        return LogFile;
    }

    public void setLogFile(File logFile) {
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogFile = logFile;
    }

    public void dropFile(File logFile) {
        if (logFile.exists()) {
            logFile.delete();
        }
    }
}
