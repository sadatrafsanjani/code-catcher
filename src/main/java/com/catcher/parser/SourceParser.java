package com.catcher.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;

public class SourceParser {

    private final ParserConfiguration parserConfiguration;

    public SourceParser() {

        parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_21);
    }

    public CompilationUnit parse(File file) {

        JavaParser parser = new JavaParser(parserConfiguration);

        try {

            return parser.parse(file).getResult().orElseThrow(() -> new RuntimeException("Could not parse file: " + file));
        }
        catch (Exception e) {

            throw new RuntimeException("Failed to parse file: " + file, e);
        }
    }
}
