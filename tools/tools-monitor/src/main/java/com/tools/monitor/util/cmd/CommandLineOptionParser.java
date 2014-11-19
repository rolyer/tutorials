package com.tools.monitor.util.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14.
 */
public class CommandLineOptionParser {
    public static final String SOLE_INPUT = "SOLE_INPUT";

    // states
    private static int STARTED = 0;
    private static int NEW_OPTION = 1;
    private static int SUB_PARAM_OF_OPTION = 2;

    private Map<String, CommandLineOption> commandLineOptions;

    public CommandLineOptionParser(
            Map<String, CommandLineOption> commandLineOptions) {
        this.commandLineOptions = commandLineOptions;
    }

    public CommandLineOptionParser(String[] args) {
        this.commandLineOptions = this.parse(args);

    }

    /**
     * Return a list with <code>CommandLineOption</code> objects
     *
     * @param args
     * @return CommandLineOption List
     */
    private Map<String, CommandLineOption> parse(String[] args) {
        Map<String, CommandLineOption> commandLineOptions = new HashMap<String, CommandLineOption>();

        if (0 == args.length) {
            return commandLineOptions;
        }

        // State 0 means started
        // State 1 means earlier one was a new -option
        // State 2 means earlier one was a sub param of a -option

        int state = STARTED;
        ArrayList<String> optionBundle = null;
        String optionType = null;
        CommandLineOption commandLineOption;

        for (int i = 0; i < args.length; i++) {

            if (args[i].startsWith("-")) {
                if (STARTED == state) {
                    // fresh one
                    state = NEW_OPTION;
                    optionType = args[i];
                } else if (SUB_PARAM_OF_OPTION == state || NEW_OPTION == state) {
                    // new one but old one should be saved
                    commandLineOption = new CommandLineOption(optionType,
                            optionBundle);
                    commandLineOptions.put(commandLineOption.getOptionType(),
                            commandLineOption);
                    state = NEW_OPTION;
                    optionType = args[i];
                    optionBundle = null;

                }
            } else {
                if (STARTED == state) {
                    commandLineOption = new CommandLineOption(SOLE_INPUT, args);
                    commandLineOptions.put(commandLineOption.getOptionType(),
                            commandLineOption);
                    return commandLineOptions;

                } else if (NEW_OPTION == state) {
                    optionBundle = new ArrayList<String>();
                    optionBundle.add(args[i]);
                    state = SUB_PARAM_OF_OPTION;

                } else if (SUB_PARAM_OF_OPTION == state) {
                    optionBundle.add(args[i]);
                }

            }

        }

        commandLineOption = new CommandLineOption(optionType, optionBundle);
        commandLineOptions.put(commandLineOption.getOptionType(),
                commandLineOption);
        return commandLineOptions;
    }

    public Map<String, CommandLineOption> getAllOptions() {
        return this.commandLineOptions;
    }
}
