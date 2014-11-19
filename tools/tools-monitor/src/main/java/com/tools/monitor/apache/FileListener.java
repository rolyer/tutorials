package com.tools.monitor.apache;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-17 上午11:51.
 */
public class FileListener extends FileAlterationListenerAdaptor {

    private static final Logger LOGGER = Logger.getLogger(FileListener.class);

    @Override
    public void onDirectoryCreate(File directory) {
        //TODO: to do something here...
    }

    @Override
    public void onDirectoryChange(File directory) {
        //TODO: to do something here...
    }

    @Override
    public void onDirectoryDelete(File directory) {
        //TODO: to do something here...
    }

    @Override
    public void onFileCreate(File file) {
        LOGGER.info("[File Create]:" + file.getAbsolutePath());

        FileProcess.transform(file);
    }
    @Override
    public void onFileChange(File file) {
        //TODO: to do something here...
    }

    @Override
    public void onFileDelete(File file) {
        //TODO: to do something here...
    }
}
