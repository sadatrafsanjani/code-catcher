package com.catcher.parser;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;

public class SourceParser {

    public SourceParser() {

        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_21);
        StaticJavaParser.setConfiguration(configuration);
    }

    public CompilationUnit parse(File file) {

        try {
            return StaticJavaParser.parse(file);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
