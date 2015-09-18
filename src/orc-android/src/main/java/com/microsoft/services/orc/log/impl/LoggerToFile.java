package com.microsoft.services.orc.log.impl;

import android.os.Environment;

import com.microsoft.services.orc.log.LogLevel;
import com.microsoft.services.orc.log.LoggerBase;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The type Logger to File.
 */
public class LoggerToFile extends LoggerBase {
    @Override
    public void print(String content, LogLevel logLevel) {
        PrintWriter pw;
        try {
            pw = new PrintWriter(
                    new FileWriter(Environment.getExternalStorageDirectory()+"/office365.log", true));

            pw.print(content);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
