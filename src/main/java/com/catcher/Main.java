package com.catcher;

import com.catcher.analyzer.*;
import com.catcher.builder.CodeModelBuilder;
import com.catcher.parser.SourceParser;
import com.catcher.report.AnalysisReportGenerator;
import com.catcher.report.AnalysisReportPrinter;
import com.catcher.scanner.ProjectScanner;
import com.github.javaparser.ast.CompilationUnit;
import java.nio.file.Path;
import com.catcher.model.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("=========== CODE CATCHER ===========\n");

        String projectPath = "E:\\sample";
        Initializer initializer = new Initializer(projectPath);
        initializer.initialize();

//        ClassDetector classDetector = new ClassDetector();
//        Set<JavaClass> controllers = classDetector.detectControllers(project.getClasses());
//        Set<JavaClass> services = classDetector.detectServices(project.getClasses());
//        Set<JavaClass> repositories = classDetector.detectRepositories(project.getClasses());

//        printClasses("CONTROLLERS", controllers);
//        printClasses("SERVICES", services);
//        printClasses("REPOSITORIES", repositories);

//        AnalysisReportGenerator reportGenerator = new AnalysisReportGenerator();
//        AnalysisReport report = reportGenerator.generate(project.getClasses());
//        AnalysisReportPrinter printer = new AnalysisReportPrinter();
//        printer.print(report);
    }
}