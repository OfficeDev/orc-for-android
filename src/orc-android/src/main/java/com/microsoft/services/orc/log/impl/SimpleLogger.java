package com.microsoft.services.orc.log.impl;


import com.microsoft.services.orc.log.LogLevel;
import com.microsoft.services.orc.log.LoggerBase;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleLogger extends LoggerBase {
    private static final String TAG = "Office365-SDK";
    private final static Logger logger = Logger.getLogger(LoggerImpl.class.getName());

    @Override
    public void print(String content, LogLevel logLevel) {
        logger.log(Level.ALL, content);
    }
}