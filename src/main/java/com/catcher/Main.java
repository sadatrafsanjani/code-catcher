package com.catcher;

import com.catcher.analyzer.Detector;
import com.catcher.builder.CodeModelBuilder;
import com.catcher.model.JavaClass;
import com.catcher.model.JavaProject;
import com.catcher.parser.SourceParser;
import com.catcher.scanner.ProjectScanner;
import com.github.javaparser.ast.CompilationUnit;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Path projectPath = Path.of("E:\\sandbox\\E-commerce-project-springBoot");

        ProjectScanner scanner = new ProjectScanner();
        SourceParser parser = new SourceParser();
        CodeModelBuilder modelBuilder = new CodeModelBuilder();

        Detector detector = new Detector();

        List<Path> javaFiles = scanner.scan(projectPath);

        JavaProject project = new JavaProject();

        for (Path file : javaFiles) {

            try {

                CompilationUnit compilationUnit = parser.parse(file);
                JavaProject parsedProject = modelBuilder.build(compilationUnit, file);
                parsedProject.getClasses().forEach(project::addClass);
            }
            catch (Exception e) {
                System.out.println("Failed: " + file + " | " + e.getMessage());
            }
        }

        System.out.println("CODE CATCHER");
        System.out.println("------------------");
        System.out.println("Project: " + projectPath);
        System.out.println("Java files: " + javaFiles.size());
        System.out.println("Classes: " + project.getClasses().size());
        System.out.println();

        for (JavaClass javaClass : project.getClasses()) {

            System.out.println(javaClass.getPackageName() + "." + javaClass.getName());
            System.out.println("  Methods: " + javaClass.getMethods().size());
            System.out.println("  Annotations: " + javaClass.getAnnotations());
            System.out.println("  Dependencies: " + javaClass.getDependencies());
            System.out.println();
        }

        printClasses("CONTROLLERS", detector.detectControllers(project.getClasses()));
        printClasses("SERVICES", detector.detectServices(project.getClasses()));
        printClasses("REPOSITORIES", detector.detectRepositories(project.getClasses()));
        printClasses("ENTITIES", detector.detectEntities(project.getClasses()));
        printClasses("CONFIGURATION", detector.detectConfigurations(project.getClasses()));
        printClasses("COMPONENTS", detector.detectComponents(project.getClasses()));
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