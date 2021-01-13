package com.jfrog.scheduler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class RetrieveFiles {
    public void Download(Properties pps) {
        String command = "jfrog rt dl";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String logfileName = df.format(new Date()) + ".log";
        LogFile logfile = new LogFile();
        logfile.setLogFile(new File(logfileName));


        String downloadType = pps.getProperty("DOWNLOAD_TYPE");
        String downloadTarget = pps.getProperty("DOWNLOAD_TARGET");
        String locationTarget = pps.getProperty("LOCATION_TARGET");
        String flat = pps.getProperty("FLAT");
        String thread_number = pps.getProperty("THREAD_NUMBER");
        flat = "--flat=" + flat;
        thread_number = "--threads=" + thread_number;

        if (downloadType.equals("downloadFolder")) {
            downloadTarget = downloadTarget.endsWith("/") ? downloadTarget : downloadTarget + "/";
            if (locationTarget == null || locationTarget.length() == 0) {
                System.out.println("LOCATION_TARGET is null ,download file to current path");
            } else {
                locationTarget = locationTarget.endsWith("/") ? locationTarget : locationTarget + "/";
            }
            System.out.println("Download Floder");
        } else if (downloadType.equals("downloadFile")) {
            System.out.println("DownloadFile");
        } else {
            System.out.println("Command or parameter is incorrect, please check again.");
            System.exit(-1);
        }
        command = command + " " + downloadTarget + " " + locationTarget + " " + flat + " " + thread_number;
        ProcessBuilderCMD pbcmd = new ProcessBuilderCMD();
        pbcmd.processBuilderCommand(command, logfile, pps);


        if (pps.getProperty("JFROG_CLI_LOG_LEVEL").equals("ERROR")) {
            try {
                String result = FileUtils.readFileToString(logfile.getLogFile(), System.getProperty("file.encoding"));
                JSONObject jsonobject = JSON.parseObject(result);
                String status = jsonobject.getString("status");

                if (status.equals("success")) {
                    System.out.println(result);
                    System.out.println("download completed");
                } else {

                    System.out.println(result);
                    System.out.println("download error");
                    System.exit(-1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                logfile.dropFile(logfile.getLogFile());
            }
        }
    }
}
