package com.catcher;

import com.catcher.analyzer.ClassDetector;
import com.catcher.builder.CodeModelBuilder;
import com.catcher.model.AnalysisReport;
import com.catcher.model.JavaClass;
import com.catcher.model.JavaProject;
import com.catcher.parser.SourceParser;
import com.catcher.report.ReportGenerator;
import com.catcher.report.AnalysisReportPrinter;
import com.catcher.scanner.ProjectScanner;
import com.github.javaparser.ast.CompilationUnit;
import lombok.Data;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class Initializer {

    private String project;
    private SourceParser sourceParser;
    private CodeModelBuilder modelBuilder;
    private ProjectScanner projectScanner;
    private List<Path> javaFiles;
    private JavaProject javaProject;
    private ClassDetector classDetector;
    private ReportGenerator reportGenerator;
    private AnalysisReportPrinter analysisReportPrinter;

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

        for (Path file : javaFiles) {

            CompilationUnit compilationUnit = sourceParser.parse(file.toFile());
            JavaProject parsedProject = modelBuilder.build(compilationUnit, file);
            parsedProject.getClasses().forEach(javaProject::addClass);
        }

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
}
