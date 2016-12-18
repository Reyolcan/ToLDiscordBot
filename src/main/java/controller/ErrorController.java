package controller;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class ErrorController {
    public static void logError(String error) {
        System.out.println(error);
        try {
            FileUtils.writeStringToFile(new File("log.txt"), error + System.lineSeparator(), "utf-8", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logStackTrace(Exception exception) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace();
        exception.printStackTrace(pw);
        try {
            FileUtils.writeStringToFile(new File("errors/" + date + ".txt"), sw.toString() + System.lineSeparator() + "###" + System.lineSeparator(), "utf-8", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
