package com.tools.monitor.bootstrap;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14.
 */
public class Bootstrap {

    public static final String CONF_DIR = "/conf";
    //public static final String BIN_DIR = "/bin";
    public static final String LIB_DIR = "/lib";

    public static final String MONITOR_HOME = "MONITOR_HOME";
    public static final String DEBUG = "debug";

    private ClassLoader classLoader = null;
    private Object startupInstance;

    private void setMonitorHome() {
        if (System.getProperty(MONITOR_HOME) != null)
            return;
        System.setProperty(MONITOR_HOME, System.getProperty("user.dir"));
    }

    private void initClassLoader() throws MalformedURLException {

        List<URL> urls = new ArrayList<URL>();

        String home = getMonitorHome();
        String libdir = home + LIB_DIR;
        File lib = new File(libdir);
        if (lib != null && lib.exists()) {
            File[] jars = lib.listFiles();
            if (jars != null && jars.length > 0) {
                for (File jar : jars) {
                    if (jar.getName().endsWith(".jar")) {
                        urls.add(jar.toURI().toURL());
                    }
                }
            }
        }

        String confdir = home + CONF_DIR;
        File conf = new File(confdir);
        if (conf != null && conf.exists()) {
            urls.add(conf.toURI().toURL());
        }

        if ("true".equals(System.getProperty(DEBUG))) {
            System.out.println("Jars: " + urls.toString());
        }

        final URL[] urlArray = urls.toArray(new URL[urls.size()]);
        classLoader = AccessController
                .doPrivileged(new PrivilegedAction<ClassLoader>() {
                    @Override
                    public ClassLoader run() {
                        return new URLClassLoader(urlArray);
                    }
                });

        Thread.currentThread().setContextClassLoader(classLoader);
    }

    private void init() throws Exception {
        setMonitorHome();
        initClassLoader();
    }

    private void setArgs(String[] args) throws Exception {
        Class<?> startupClass = classLoader
                .loadClass("com.tools.monitor.startup.CommandLineStartup");
        startupInstance = startupClass.newInstance();

        String methodName = "setArgs";
        Class<?> paramTypes[] = new Class[1];
        paramTypes[0] = Class.forName("[Ljava.lang.String;");
        Object paramValues[] = new Object[1];
        paramValues[0] = args;
        Method method = startupInstance.getClass().getMethod(methodName,
                paramTypes);
        method.invoke(startupInstance, paramValues);
    }

    private void startup() throws Exception {
        Method method = startupInstance.getClass().getMethod("start",
                (Class[]) null);
        method.invoke(startupInstance, (Object[]) null);
    }

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.init();
        bootstrap.setArgs(args);
        bootstrap.startup();
    }

    public static String getMonitorHome() {
        return System.getProperty(MONITOR_HOME,
                System.getProperty("user.dir"));
    }
}
