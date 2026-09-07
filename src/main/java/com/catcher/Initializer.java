package com.catcher;

import com.catcher.analyzer.ClassDetector;
import com.catcher.builder.CodeModelBuilder;
import com.catcher.model.JavaClass;
import com.catcher.model.JavaProject;
import com.catcher.parser.SourceParser;
import com.catcher.report.ReportGenerator;
import com.catcher.report.AnalysisReportPrinter;
import com.catcher.scanner.ProjectScanner;
import com.github.javaparser.ast.CompilationUnit;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Initializer {

    private final String project;
    private final SourceParser sourceParser;
    private final CodeModelBuilder modelBuilder;
    private final ProjectScanner projectScanner;
    private final JavaProject javaProject;
    private final ClassDetector classDetector;
    private final ReportGenerator reportGenerator;
    private final AnalysisReportPrinter analysisReportPrinter;

    public Initializer(String project){

        this.project = project;
        this.sourceParser = new SourceParser();
        this.modelBuilder = new CodeModelBuilder();
        this.projectScanner = new ProjectScanner();
        this.javaProject = new JavaProject();
        this.classDetector = new ClassDetector();
        this.reportGenerator = new ReportGenerator();
        this.analysisReportPrinter = new AnalysisReportPrinter();
    }

    public void initialize(){

        long startTime = System.nanoTime();

        List<Path> scannedFiles = projectScanner.scan(Path.of(project));

        parseJavaFiles(scannedFiles);

        printFileCounts(scannedFiles.size());

        printFileDescriptions(javaProject.getClasses());

        Map<String, Set<JavaClass>> detected = classDetector.detect(javaProject.getClasses());
        printClasses("CONTROLLERS", detected.get("CONTROLLERS"));
        printClasses("SERVICES", detected.get("SERVICES"));
        printClasses("REPOSITORIES", detected.get("REPOSITORIES"));
        printClasses("ENTITIES", detected.get("ENTITIES"));
        printClasses("COMPONENTS", detected.get("COMPONENTS"));
        printClasses("CONFIGURATIONS", detected.get("CONFIGURATIONS"));

        printAnalysisReport(javaProject.getClasses());

        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

        System.out.printf("%nTotal inspection time: %.3f seconds%n", elapsedSeconds);
    }

    private void printFileCounts(long total) {

        long classCount = javaProject.getClasses().stream().filter(javaClass -> !javaClass.isInterfaceType()).count();
        long interfaceCount = javaProject.getClasses().stream().filter(JavaClass::isInterfaceType).count();
        long otherFiles = total - (classCount + interfaceCount);

        System.out.println("Project: " + project);
        System.out.println("Java files: " + total);
        System.out.println("Classes: " + classCount);
        System.out.println("Interfaces: " + interfaceCount);
        System.out.println("Others: " + otherFiles);
        System.out.println();
    }

    private void printFileDescriptions(Set<JavaClass> classes) {

        for (JavaClass javaClass : classes) {

            System.out.println("Package: " + javaClass.getPackageName() + "." + javaClass.getName());
            System.out.println("Methods: " + javaClass.getMethods().size());
            System.out.println("Annotations: " + javaClass.getAnnotations());
            System.out.println("Dependencies: " + javaClass.getDependencies());
            System.out.println();
        }
    }

    private void printClasses(String title, Set<JavaClass> classes) {

        System.out.println();
        System.out.println(title + "\n ------------------");

        Map<String, Set<JavaClass>> classesByPackage = new LinkedHashMap<>();

        for (JavaClass javaClass : classes) {

            classesByPackage.computeIfAbsent(javaClass.getPackageName(), key -> new LinkedHashSet<>()).add(javaClass);
        }

        for (Map.Entry<String, Set<JavaClass>> entry : classesByPackage.entrySet()) {

            System.out.println(entry.getKey());

            for (JavaClass javaClass : entry.getValue()) {

                System.out.println("--> " + javaClass.getName());
            }

            System.out.println();
        }
    }

    private void printAnalysisReport(Set<JavaClass> classes){

        analysisReportPrinter.print(reportGenerator.generate(classes));
    }

    private void parseJavaFiles(List<Path> scannedFiles) {

        int threadCount = Math.max(1, Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {

            List<Future<JavaProject>> futures = new ArrayList<>(scannedFiles.size());

            for (Path file : scannedFiles) {

                futures.add(executor.submit(() -> {

                        CompilationUnit compilationUnit = sourceParser.parse(file.toFile());

                        return modelBuilder.build(compilationUnit, file);
                    })
                );
            }

            for (Future<JavaProject> future : futures) {

                try {

                    JavaProject parsedProject = future.get();
                    parsedProject.getClasses().forEach(javaProject::addClass);

                }
                catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Java source processing was interrupted.", e);
                }
                catch (ExecutionException e) {

                    throw new RuntimeException("Failed to process Java source file.", e.getCause());
                }
            }

        }
        finally {

            executor.shutdown();
        }
    }
}
