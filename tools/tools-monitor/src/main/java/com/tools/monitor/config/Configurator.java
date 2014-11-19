package com.tools.monitor.config;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.*;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午4:01.
 */
public class Configurator {

    private static final Logger LOGGER = Logger.getLogger(Configurator.class);

    private static Config populateConfiguration(InputStream inputStream)
            throws ConfigException {

        Config cfg = null;
        try {
            Document document = buildDocument(inputStream);
            ConfigVisitor visitor = new ConfigVisitor();
            visitor.read(document);

            cfg = visitor.getConfiguration();

        } catch (DocumentException e) {
            throw new ConfigException(e);
        }
        return cfg;
    }

    private static Document buildDocument(InputStream inputStream)
            throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        return document;
    }

    public synchronized static Config getConfig(String path)
            throws FileNotFoundException, ConfigException {

        Config config = null;
        if (path != null) {
            File configFile = new File(path);
            if (!configFile.exists()) {
                throw new ConfigException("Config file '" + path
                        + "' doesn't exist.");
            } else {
                path = configFile.getAbsolutePath();

                LOGGER.info("Load configuration from '" + path + "'");

                InputStream configStream = null;
                try {
                    if (path != null && !"".equals(path)) {
                        configStream = new FileInputStream(path);
                        config = populateConfiguration(configStream);

                        LOGGER.info("Configuration loaded");
                    }

                } finally {
                    if (configStream != null) {
                        try {
                            configStream.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
        return config;
    }

}
