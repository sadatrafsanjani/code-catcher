package com.catcher.scanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ProjectScanner {

    public List<Path> scan(Path projectRoot) {

        if (!Files.exists(projectRoot)) {

            throw new IllegalArgumentException("Project directory does not exist: " + projectRoot);
        }

        if (!Files.isDirectory(projectRoot)) {

            throw new IllegalArgumentException("Path is not a directory: " + projectRoot);
        }

        try (Stream<Path> paths = Files.walk(projectRoot)) {

            return paths.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).toList();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
