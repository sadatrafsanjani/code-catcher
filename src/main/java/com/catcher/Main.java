package com.catcher;

import com.catcher.analyzer.CircularDependencyDetector;
import com.catcher.analyzer.ClassDetector;
import com.catcher.analyzer.DependencyDetector;
import com.catcher.builder.CodeModelBuilder;
import com.catcher.model.*;
import com.catcher.parser.SourceParser;
import com.catcher.scanner.ProjectScanner;
import com.github.javaparser.ast.CompilationUnit;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Path projectPath = Path.of("E:\\sandbox\\E-commerce-project-springBoot");

        ProjectScanner scanner = new ProjectScanner();
        SourceParser parser = new SourceParser();
        CodeModelBuilder modelBuilder = new CodeModelBuilder();

        ClassDetector detector = new ClassDetector();

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

        Set<JavaClass> controllers = detector.detectControllers(project.getClasses());
        Set<JavaClass> services = detector.detectServices(project.getClasses());
        Set<JavaClass> repositories = detector.detectRepositories(project.getClasses());

        printClasses("CONTROLLERS", controllers);
        printClasses("SERVICES", services);
        printClasses("REPOSITORIES", repositories);

        DependencyDetector dependencyDetector = new DependencyDetector();
        Map<JavaClass, Set<JavaClass>> dependencyGraph = dependencyDetector.detectDependencies(project.getClasses());
        printDependencyGraph(dependencyGraph);

        CircularDependencyDetector circularDependencyDetector = new  CircularDependencyDetector();
        Set<List<JavaClass>> cycles = circularDependencyDetector.detectCircularDependencies(dependencyGraph);
        printCircularDependencies(cycles);
    }

    private static void printClasses(String title, Set<JavaClass> classes) {

        System.out.println();
        System.out.println(title);
        System.out.println("------------------");

        for (JavaClass javaClass : classes) {

            System.out.println(javaClass.getPackageName() + "." + javaClass.getName());
        }
    }

    private static void printDependencyGraph(Map<JavaClass, Set<JavaClass>> dependencyGraph) {

        System.out.println();
        System.out.println("DEPENDENCY GRAPH");
        System.out.println("------------------");

        for (Map.Entry<JavaClass, Set<JavaClass>> entry : dependencyGraph.entrySet()) {

            JavaClass source = entry.getKey();

            System.out.println(source.getPackageName() + "." + source.getName());

            for (JavaClass dependency : entry.getValue()) {

                System.out.println("    -> " + dependency.getPackageName() + "." + dependency.getName()
                );
            }
        }
    }

    private static void printCircularDependencies(Set<List<JavaClass>> cycles) {

        System.out.println();
        System.out.println("CIRCULAR DEPENDENCIES");
        System.out.println("------------------");

        if (cycles.isEmpty()) {

            System.out.println("No circular dependencies found.");
            return;
        }

        for (List<JavaClass> cycle : cycles) {

            for (int i = 0; i < cycle.size(); i++) {

                JavaClass javaClass = cycle.get(i);

                System.out.print(
                        javaClass.getPackageName() + "." + javaClass.getName()
                );

                if (i < cycle.size() - 1) {
                    System.out.print(" -> ");
                }
            }

            System.out.println();
        }
    }
}