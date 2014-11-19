package com.tools.monitor.config;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;

import com.tools.monitor.Constants;
import com.tools.monitor.FileMonitor;
import com.tools.monitor.FolderMonitor;
import com.tools.monitor.Monitor;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午4:20.
 */
public class ConfigVisitor extends VisitorSupport {

    public static final String TAG_FOLDER_MONITOR = "FolderMonitor";
    public static final String TAG_PATH = "Path";
    public static final String TAG_INTERVAL = "Interval";
    public static final String TAG_FILE_MONITOR = "FileMonitor";
    public static final String TAG_NAME = "Name";
    public static final String TAG_FILE = "File";
    public static final String TAG_PROPERTY = "Property";

    private Config configuration;

    public ConfigVisitor() {
        configuration = new Config();
    }

    @SuppressWarnings("unchecked")
    public void read(Document document) throws ConfigException {
        Element elem = document.getRootElement();

        // jms config
//		Element nodeJMSConnection = elem.element(TAG_JMS_CONNECTION);
//		if (null != nodeJMSConnection) {
//			visitJMSConnection(nodeJMSConnection);
//		}

        // global properties
        List<Element> propertyElems = elem.elements(TAG_PROPERTY);
        for (Element element : propertyElems) {
            String propName = element.attributeValue("name");
            String propVal = element.attributeValue("value");
            configuration.getGlobalProperties().put(propName, propVal);
        }

        // read monitors by order
        List<Element> subElements = elem.elements();
        if (subElements != null) {
            for (Element sub : subElements) {
                String elemName = sub.getName();
                if (TAG_FILE_MONITOR.equals(elemName)) {

                    // file monitor
                    FileMonitor fileMonitor = visitFileMonitor(sub);
                    configuration.getMonitors().add(fileMonitor);

                } else if (TAG_FOLDER_MONITOR.equals(elemName)) {

                    // folder monitor
                    FolderMonitor folderMonitor = visitFolerMonitor(sub);
                    configuration.getMonitors().add(folderMonitor);

                } else {
                    //TODO: to do something here...
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private FolderMonitor visitFolerMonitor(Element element)
            throws ConfigException {
        File dir = null;
        String path = getText(element, TAG_PATH);
        if (null != path) {
            dir = new File(path);
        } else {
            String msg = expectedChildMsg(TAG_PATH, element.getName());
            throw new ConfigException(msg);
        }

        List<Element> fileMonitorList = element
                .elements(TAG_FILE_MONITOR);
        if (fileMonitorList == null || fileMonitorList.size() == 0) {
            String msg = expectedChildMsg(TAG_FILE_MONITOR,
                    element.getName());
            throw new ConfigException(msg);
        }

        String txtName = getText(element, TAG_NAME);

        FolderMonitor folderMonitor = new FolderMonitor(txtName, dir);
        folderMonitor.setProperties(new Properties());
        folderMonitor.setExecutor(configuration.getTaskExecutor());

        // visit all the log file monitor configuration in log folder monitor.
        for (Element e : fileMonitorList) {
            String key = getText(e, TAG_FILE);
            FileMonitor fileMonitor = visitFileMonitor(e);
            folderMonitor.getFileMonitorTemplates().put(key, fileMonitor);
        }

        String interval = getText(element, TAG_INTERVAL);
        if (null != interval) {
            long val = Long.parseLong(interval);
            //val = secondToMillis(val); // change into millisecond
            folderMonitor.getProperties().put(Constants.PROP_INTERVAL, val);
        }

       

        visitProperty(folderMonitor, element);

        return folderMonitor;
    }

    private FileMonitor visitFileMonitor(Element element)
            throws ConfigException {
        String filename = getText(element, TAG_FILE);
        if (null == filename) {
            String msg = expectedChildMsg(TAG_FILE, element.getName());
            throw new ConfigException(msg);
        }
		
        FileMonitor fileMonitor = new FileMonitor(null);
        fileMonitor.setProperties(new Properties());

        fileMonitor.setName(getText(element, TAG_NAME));

        if (!TAG_FOLDER_MONITOR.equals((element.getParent().getName()))) {
            fileMonitor.setFile(new File(filename));
        }

        Element interval = element.element(TAG_INTERVAL);
        if (null != interval) {
            long val = Long.parseLong(interval.getText());
            fileMonitor.getProperties().put(Constants.PROP_INTERVAL, val);
        }

        visitProperty(fileMonitor, element);

        fileMonitor.setExecutor(configuration.getTaskExecutor());

        return fileMonitor;
    }

    @SuppressWarnings("unchecked")
    private void visitProperty(Monitor monitor, Element element) {
        List<Element> propertys = element.elements(TAG_PROPERTY);
        if (propertys != null && propertys.size() > 0) {
            for (Element elem : propertys) {
                String propName = elem.attributeValue("name");
                String propVal = elem.attributeValue("value");
                monitor.getProperties().put(propName, propVal);
            }
        }
    }

    private String getText(Element parent, String tag) {
        Element child = parent.element(tag);
        if (null != child) {
            return child.getText();
        }

        return null;
    }

    public String expectedChildMsg(String child, String parent) {
        return "<" + child + "> is expected element of <" + parent + ">.";
    }

    public Config getConfiguration() {
        return configuration;
    }
}
