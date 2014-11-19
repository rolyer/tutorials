package com.tools.monitor.util.cmd;

import java.util.ArrayList;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14.
 */
public class CommandLineOption {
    private String type;
    private ArrayList<String> optionValues;

    public CommandLineOption(String type, String[] values) {
        setOptionType(type);
        ArrayList<String> arrayList = new ArrayList<String>(values.length);
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                values[i] = values[i].trim();
            }
            arrayList.add(values[i]);
        }
        this.optionValues = arrayList;
    }

    private void setOptionType(String type) {
        if (type.startsWith("--")) { // the long options first
            type = type.replaceFirst("--", "");
        }
        if (type.startsWith("-")) {
            type = type.replaceFirst("-", "");
        }
        this.type = type;
    }

    public CommandLineOption(String type, ArrayList<String> values) {
        setOptionType(type);

        if (null != values) {
            this.optionValues = values;
        }
    }

    public String getOptionType() {
        return type;
    }

    public String getOptionValue() {
        if ((optionValues != null) && (optionValues.size() > 0)) {
            return (String) optionValues.get(0);
        } else {
            return null;
        }
    }

    public ArrayList<String> getOptionValues() {
        return optionValues;
    }

    @Override
    public String toString() {
        return optionValues == null ? type : optionValues.toString();
    }
}
