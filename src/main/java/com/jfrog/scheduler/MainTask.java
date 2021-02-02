package com.jfrog.scheduler;

import jnr.posix.POSIXFactory;
import sun.java2d.pipe.ShapeSpanIterator;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class MainTask {
    public static void main(String[] args) {

        DeleteItem delete = new DeleteItem();
        Properties pps = loadProperties(args);

        String locationTarget = pps.getProperty("LOCATION_TARGET");
        String downloadTarget = pps.getProperty("DOWNLOAD_TARGET");
        String zipFileName = pps.getProperty("ZIP_FILENAME");
        zipFileName = (zipFileName == null || zipFileName.equals("")) ? downloadTarget.split("/")[1] : zipFileName;

        if (locationTarget == null || locationTarget.equals("")) {
            delete.deleteDir(new File(downloadTarget));
        } else {
            delete.deleteDir(new File(locationTarget + zipFileName));
        }


        System.out.println("DOWNLOAD_TARGET: " + downloadTarget);
        System.out.println("ZIP_FILENAME: " + zipFileName);
        POSIXFactory.getPOSIX().chdir(locationTarget);
        //清理原有文件
        delete.deleteFile(zipFileName + ".success");
        delete.deleteFile(zipFileName + ".failure");
        delete.deleteDir(new File(zipFileName));


        // 下载文件
        RetrieveFiles rf = new RetrieveFiles();
        rf.Download(pps);
        // 压缩文件

        ZipFileUtil zfu = new ZipFileUtil();
        ArrayList<File> files = new ArrayList<>();

        zfu.compressFiles2Zip(zfu.getFiles(zipFileName, files), zipFileName);
        FinishStatus.success(zipFileName);
        delete.deleteDir(new File(zipFileName));


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
