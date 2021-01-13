package com.jfrog.scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FinishStatus {
    public static void success(String projectName){
        File success = new File(projectName + ".success");
        File projectdir = new File(projectName);

        try {
           success.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void failure(String projectName){
        File failure = new File(projectName + ".failure");
        try {
            failure.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
