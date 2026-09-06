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

        Path projectPath = Path.of("E:\\sample");

        ProjectScanner scanner = new ProjectScanner();
        SourceParser parser = new SourceParser();
        CodeModelBuilder modelBuilder = new CodeModelBuilder();

        ClassDetector detector = new ClassDetector();
        List<Path> javaFiles = scanner.scan(projectPath);
        JavaProject project = new JavaProject();

        for (Path file : javaFiles) {

            CompilationUnit compilationUnit = parser.parse(file.toFile());
            JavaProject parsedProject = modelBuilder.build(compilationUnit, file);
            parsedProject.getClasses().forEach(project::addClass);
        }

        System.out.println("CODE CATCHER");
        System.out.println("------------------");
        System.out.println("Project: " + projectPath);
        System.out.println("Java files: " + javaFiles.size());
        System.out.println("Classes: " + project.getClasses().size());
        System.out.println();

        for (JavaClass javaClass : project.getClasses()) {

            System.out.println(javaClass.getPackageName() + "." + javaClass.getName());
            System.out.println("Methods: " + javaClass.getMethods().size());
            System.out.println("Annotations: " + javaClass.getAnnotations());
            System.out.println("Dependencies: " + javaClass.getDependencies());
            System.out.println();
        }

        Set<JavaClass> controllers = detector.detectControllers(project.getClasses());
        Set<JavaClass> services = detector.detectServices(project.getClasses());
        Set<JavaClass> repositories = detector.detectRepositories(project.getClasses());

        printClasses("CONTROLLERS", controllers);
        printClasses("SERVICES", services);
        printClasses("REPOSITORIES", repositories);

        AnalysisReportGenerator reportGenerator = new AnalysisReportGenerator();
        AnalysisReport report = reportGenerator.generate(project.getClasses());
        AnalysisReportPrinter printer = new AnalysisReportPrinter();
        printer.print(report);
    }

    private static void printClasses(String title, Set<JavaClass> classes) {

        System.out.println();
        System.out.println(title);
        System.out.println("------------------");

        for (JavaClass javaClass : classes) {

            System.out.println(javaClass.getPackageName() + "." + javaClass.getName());
        }
    }
}