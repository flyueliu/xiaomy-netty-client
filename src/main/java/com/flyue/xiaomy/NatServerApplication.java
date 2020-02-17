package com.flyue.xiaomy;

import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class NatServerApplication {

    // 使用命令 wsimport -keep -p com.pachira.boot.webservice.model -encoding utf-8 http://127.0.0.1:8020/HelloHBuilder/SaveQualityResults.xml
    public static void main(String[] args) {
        Options options = argumentOptions();
        CommandLineParser parser = new BasicParser();
        CommandLine command = null;
        try {
            command = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            System.exit(255);
            return;
        }

        if (command.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.setWidth(Integer.MAX_VALUE);
            formatter.printHelp("wrapper", options);
            return;
        }
        if (command.hasOption("version")) {
            System.out.println("wrapper version:v1.4");
            return;
        }
        List<String> list = new ArrayList<>();
        String cfg = command.getOptionValue("cfg");
        if (cfg != null && !cfg.isEmpty()) {
            list.add("--spring.config.location=" + cfg);
        }
        String[] arr = new String[list.size()];
        arr = list.toArray(arr);
        run(arr);
    }

    public static void run(String[] args) {
        SpringApplication.run(NatServerApplication.class, args);
    }

    private static Options argumentOptions() {
        Option help = OptionBuilder.create("h");
        help.setDescription("print help message");
        Option version = OptionBuilder.create("v");
        version.setDescription("print the version of application");
        Option confPath = OptionBuilder.create("cfg");
        Options options = new Options();
        options.addOption(help);
        options.addOption(version);
        options.addOption(confPath);
        return options;
    }

}
