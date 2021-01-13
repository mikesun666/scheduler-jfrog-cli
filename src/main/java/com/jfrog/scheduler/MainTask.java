package com.jfrog.scheduler;

import sun.java2d.pipe.ShapeSpanIterator;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class MainTask {
    public static void main(String[] args) {

        DeleteItem delete = new DeleteItem ();
        Properties pps = loadProperties(args);

        String locationTarget = pps.getProperty("LOCATION_TARGET");
        String downloadTarget = pps.getProperty("DOWNLOAD_TARGET");
        String zipFileName = pps.getProperty("ZIP_FILENAME");

        if (locationTarget == null || locationTarget.equals("")) {
            downloadTarget = downloadTarget.split("/")[1];
        } else {
            downloadTarget = locationTarget;
        }

        //清理原有文件
        delete.deleteFile(downloadTarget+".success");
        delete.deleteFile(downloadTarget+".failure");
        delete.deleteDir(new File(downloadTarget));

        if (null == zipFileName || zipFileName.length() <= 0) {
            zipFileName = downloadTarget + ".zip";
        } else {
            System.out.println("1");
            zipFileName += ".zip";
        }
        delete.deleteDir(new File(downloadTarget));


//        下载文件
        RetrieveFiles rf = new RetrieveFiles();
        rf.Download(pps);
//        压缩文件

        ZipFileUtil zfu = new ZipFileUtil();
        ArrayList<File> files = new ArrayList<>();

        zfu.compressFiles2Zip(zfu.getFiles(downloadTarget, files), zipFileName);
        FinishStatus.success(downloadTarget);
        delete.deleteDir(new File(downloadTarget));

    }


    public static Properties loadProperties(String args[]) {

        Properties pps = System.getProperties();
        File config = new File("./config.properties");
        try {
            if (config.exists()) {
                pps.load(new FileReader(config));
                System.out.println("read outside configfile");
            } else {
                InputStream in = MainTask.class.getClassLoader().getResourceAsStream("config.properties");
                pps.load(in);
                System.out.println("outside configfile does not exist,read insiede configfile");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String str : args) {
            String key = str.split("=")[0];
            String value = str.split("=")[1];
            pps.put(key, value);
        }
        return pps;
    }
}
