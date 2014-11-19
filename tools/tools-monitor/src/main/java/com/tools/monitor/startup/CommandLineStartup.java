package com.tools.monitor.startup;

import com.tools.monitor.config.Config;
import com.tools.monitor.config.Configurator;
import com.tools.monitor.util.cmd.CommandLineOption;
import com.tools.monitor.util.cmd.CommandLineOptionParser;

import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14.
 */
public class CommandLineStartup {

    private static final Logger LOGGER = Logger.getLogger(CommandLineStartup.class);

    static void usage() {
        System.err.println("\nUsage: java com.monitor.startup.CommandLineStartup [options]");
        System.err.println("");
        System.err.println("options are:");
        System.err.println("");
        System.err.println("\t-config    <config file path>");
        System.exit(0);
    }

    private String[] args;

    @SuppressWarnings("unused")
	public void start() {

        try {
            CommandLineOptionParser optionParser = new CommandLineOptionParser(
                    args);
            Map<String, CommandLineOption> cmds = optionParser.getAllOptions();
            CommandLineOption configOption = cmds.get("config");

            if(configOption==null) {
                String[] values = {"/home/rolyer/dev/tools/tools-monitor/src/main/resources/MonitorConfig.xml"};
                configOption = new CommandLineOption("--config", values);
            }

            if (configOption != null) {

                String conf = configOption.getOptionValue();

                Config config = Configurator.getConfig(conf);

                Server server = new Server(config);
                server.init();
                server.start();

            } else {
                usage();
            }
        } catch (Exception e) {
            LOGGER.error(e);
            LOGGER.error("Failed to startup.");
        }
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
