package com.jfrog.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;

public class ProcessBuilderCMD {
    public void processBuilderCommand(String command, LogFile log, Properties pps) {
        BufferedReader br = null;
        try {
            System.out.println("execute command :" + command);
            String system = pps.getProperty("os.name");
            ProcessBuilder pb = new ProcessBuilder();
            System.out.println(system);
            if (system.contains("Windows")) {
                pb.command("cmd.exe", "/c", command).inheritIO();
            } else {
                pb.command("/bin/bash", "-c" ,command).inheritIO();
            }
            pb.environment().put("CI", pps.getProperty("CI"));
            pb.environment().put("JFROG_CLI_LOG_LEVEL", pps.getProperty("JFROG_CLI_LOG_LEVEL"));
            pb.redirectErrorStream(true);//这里是把控制台中的红字变成了黑字，用通常的方法其实获取不到，控制台的结果是pb.start()方法内部输出的。
            if (!pps.getProperty("JFROG_CLI_LOG_LEVEL").equals("ERROR")) {
                System.out.println("Do not collection result");
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            } else {
                pb.redirectOutput(log.getLogFile());
            }
            pb.start().waitFor();//等待语句执行完成，否则可能会读不到结果。
            System.out.println("Command execute completed");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}