package com.tools.monitor.apache;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-17 下午1:43.
 */
public class FileProcess {
    private static final Logger LOGGER = Logger.getLogger(FileListener.class);

    public static void transform(File file){
        LOGGER.info("Start transform " + file.getName() + ".");
        //TODO: invoke excel-tools to process file and write logs.
    }

}
