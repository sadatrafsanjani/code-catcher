package com.catcher;

import com.catcher.analyzer.ArchitectureViolationDetector;
import com.catcher.analyzer.CircularDependencyDetector;
import com.catcher.analyzer.ClassDetector;
import com.catcher.builder.ArchitectureMapBuilder;
import com.catcher.builder.CodeModelBuilder;
import com.catcher.builder.DependencyGraphBuilder;
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

        ArchitectureMapBuilder architectureMapBuilder = new ArchitectureMapBuilder();
        ArchitectureMap architectureMap = architectureMapBuilder.build(controllers, services, repositories);

        System.out.println();
        System.out.println("ARCHITECTURE MAP");
        System.out.println("------------------");

        System.out.println();
        System.out.println("Controllers");

        for (JavaClass controller : architectureMap.getControllers()) {

            System.out.println("  " + controller.getPackageName() + "." + controller.getName());
        }

        System.out.println();
        System.out.println("Services");

        for (JavaClass service : architectureMap.getServices()) {

            System.out.println("  " + service.getPackageName() + "." + service.getName());
        }

        System.out.println();
        System.out.println("Repositories");

        for (JavaClass repository : architectureMap.getRepositories()) {

            System.out.println("  " + repository.getPackageName() + "." + repository.getName());
        }

        System.out.println();
        System.out.println("ARCHITECTURE VIOLATIONS");
        System.out.println("------------------");

        ArchitectureViolationDetector violationDetector = new ArchitectureViolationDetector();
        Set<ArchitectureViolation> violations = violationDetector.detect(architectureMap);

        if (violations.isEmpty()) {

            System.out.println("No architecture violations found.");

        }
        else {

            for (ArchitectureViolation violation : violations) {

                System.out.println(violation.getSourceClass() + " -> " + violation.getTargetClass());
                System.out.println("  " + violation.getMessage());
                System.out.println();
            }
        }

        DependencyGraphBuilder dependencyGraphBuilder = new DependencyGraphBuilder();
        DependencyGraph dependencyGraph = dependencyGraphBuilder.build(project.getClasses());


        System.out.println();
        System.out.println("DEPENDENCY GRAPH");
        System.out.println("------------------");

        Set<JavaClass> roots = new LinkedHashSet<>();

        for (DependencyEdge edge : dependencyGraph.getEdges()) {
            roots.add(edge.getSource());
        }

        Set<JavaClass> visited = new LinkedHashSet<>();

        for (JavaClass root : roots) {

            if (visited.contains(root)) {
                continue;
            }

            printDependencyTree(root, dependencyGraph, "", visited);

            System.out.println();
        }

    }

    private static void printClasses(String title, Set<JavaClass> classes) {

        System.out.println();
        System.out.println(title);
        System.out.println("------------------");

        for (JavaClass javaClass : classes) {

            System.out.println(javaClass.getPackageName() + "." + javaClass.getName());
        }
    }

    private static void printDependencyTree(JavaClass current, DependencyGraph graph, String prefix, Set<JavaClass> visited) {

        System.out.println(prefix + current.getName());

        if (!visited.add(current)) {
            return;
        }

        List<DependencyEdge> children = new ArrayList<>();

        for (DependencyEdge edge : graph.getEdges()) {

            if (edge.getSource() == current) {
                children.add(edge);
            }
        }

        for (int i = 0; i < children.size(); i++) {

            DependencyEdge edge = children.get(i);

            boolean last = i == children.size() - 1;

            String branch = last
                    ? "\\--> "
                    : "|--> ";

            String childPrefix = last
                    ? "     "
                    : "|    ";

            JavaClass target = edge.getTarget();

            System.out.print(prefix + branch);

            if (visited.contains(target)) {
                System.out.println(target.getName());
            }
            else {
                printDependencyTree(target, graph, prefix + childPrefix, visited);
            }
        }

        visited.remove(current);
    }
}