package com.catcher;

import com.catcher.config.MessageProvider;
import com.catcher.model.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        MessageProvider messages = new MessageProvider();

        System.out.println(messages.get("app.title"));
        System.out.println();

        if (args.length == 0) {

            System.err.println(messages.get("error.project.path.required"));
            System.err.println(messages.get("usage"));
            System.exit(1);
        }

        if (args.length > 1) {
            System.err.println(messages.get("error.project.path.multiple"));
            System.err.println(messages.get("usage"));
            System.exit(1);
        }

        Path projectPath = Paths.get(args[0]);

        if (!Files.exists(projectPath)) {
            System.err.println(messages.get("error.project.path.not.exists", args[0]));
            System.exit(1);
        }

        if (!Files.isDirectory(projectPath)) {
            System.err.println(messages.get("error.project.path.not.directory", args[0]));
            System.exit(1);
        }

        Initializer initializer = new Initializer(projectPath.toAbsolutePath().normalize().toString());
        initializer.initialize();
    }
}