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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Initializer {

    private final String project;
    private final SourceParser sourceParser;
    private final CodeModelBuilder modelBuilder;
    private final ProjectScanner projectScanner;
    private final List<Path> javaFiles;
    private final JavaProject javaProject;
    private final ClassDetector classDetector;
    private final ReportGenerator reportGenerator;
    private final AnalysisReportPrinter analysisReportPrinter;

    public Initializer(String project){

        this.project = project;
        this.sourceParser = new SourceParser();
        this.modelBuilder = new CodeModelBuilder();
        this.projectScanner = new ProjectScanner();
        this.javaFiles = new ArrayList<>();
        this.javaProject = new JavaProject();
        this.classDetector = new ClassDetector();
        this.reportGenerator = new ReportGenerator();
        this.analysisReportPrinter = new AnalysisReportPrinter();
    }

    public void initialize(){

        List<Path> javaFiles = projectScanner.scan(Path.of(project));

        parseJavaFiles(javaFiles);

        printFileCounts(javaFiles.size());
        printFileDescriptions(javaProject.getClasses());

        printClasses("CONTROLLERS", classDetector.detectControllers(javaProject.getClasses()));
        printClasses("SERVICES", classDetector.detectServices(javaProject.getClasses()));
        printClasses("REPOSITORIES", classDetector.detectRepositories(javaProject.getClasses()));

        printAnalysisReport(javaProject.getClasses());
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
        System.out.println(title);
        System.out.println("------------------");

        for (JavaClass javaClass : classes) {

            System.out.println(javaClass.getPackageName() + "." + javaClass.getName());
        }
    }

    private void printAnalysisReport(Set<JavaClass> classes){

        analysisReportPrinter.print(reportGenerator.generate(classes));
    }

    private void parseJavaFiles(List<Path> javaFiles) {

        int threadCount = Math.max(1, Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {

            List<Future<JavaProject>> futures = new ArrayList<>(javaFiles.size());

            for (Path file : javaFiles) {

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
