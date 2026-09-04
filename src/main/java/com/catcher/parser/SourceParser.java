package com.catcher.parser;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.IOException;
import java.nio.file.Path;

public class SourceParser {

    public CompilationUnit parse(Path sourceFile) {

        try {
            return StaticJavaParser.parse(sourceFile);
        }
        catch (ParseProblemException | IOException e) {

            throw new RuntimeException(e);
        }
    }
}
