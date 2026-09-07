package com.catcher.scanner;

import com.catcher.config.MessageProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ProjectScanner {

    public List<Path> scan(Path projectRoot) {

        MessageProvider message = new MessageProvider();

        if (!Files.exists(projectRoot)) {

            throw new IllegalArgumentException("Project directory does not exist: " + projectRoot);
        }

        if (!Files.isDirectory(projectRoot)) {

            throw new IllegalArgumentException("Path is not a directory: " + projectRoot);
        }

        try (Stream<Path> paths = Files.walk(projectRoot)) {

            List<Path> javaFiles = paths.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).toList();

            if (javaFiles.isEmpty()) {

                throw new IllegalArgumentException(message.get("error.project.path.not.java"));
            }

            return javaFiles;
        }
        catch (IOException e) {

            throw new RuntimeException("Failed to scan project directory: " + projectRoot, e);
        }
    }
}
