package com.jfrog.scheduler;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class MainTask {
    public static void main(String[] args) {

        Properties pps = System.getProperties();
        try {
            pps.load(new FileReader(args[0]));

        } catch (IOException e) {
            e.printStackTrace();
        }
//        下载文件
        RetrieveFiles rf = new RetrieveFiles();
        rf.Download(pps);
//        压缩文件
        ZipFileUtil zfu = new ZipFileUtil();
        ArrayList<File> files = new ArrayList<>();
        String locationTarget = pps.getProperty("LOCATION_TARGET");
        String downloadTarget = pps.getProperty("DOWNLOAD_TARGET");
        if (locationTarget == null || locationTarget.equals("")) {
            downloadTarget = downloadTarget.split("/")[1];
        }
        zfu.compressFiles2Zip(zfu.getFiles(downloadTarget, files), pps.getProperty("ZIP_FILENAME"));
    }
}
