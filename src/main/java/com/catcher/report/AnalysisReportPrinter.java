package com.catcher.report;

import com.catcher.model.AnalysisReport;
import com.catcher.model.JavaClass;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnalysisReportPrinter {

    public void print(AnalysisReport report) {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("           FINAL ANALYSIS REPORT");
        System.out.println("==========================================");

        printSummary(report);

        printDependencyGraph(report.getDependencyGraph());
        printCircularDependencies(report.getCircularDependencies());
        printArchitectureViolations(report.getArchitectureViolations());
        printCodeSmells(report.getCodeSmells());
        printNPlusOneViolations(report.getNPlusOneViolations());

        System.out.println();
        System.out.println("==========================================");
        System.out.println("             ANALYSIS COMPLETE");
        System.out.println("==========================================");
    }

    private void printSummary(AnalysisReport report) {

        System.out.println();
        System.out.println("SUMMARY");
        System.out.println("------------------");
        System.out.println("Classes analyzed: " + report.getClasses().size());
        System.out.println("Dependencies found: " + countDependencies(report.getDependencyGraph()));
        System.out.println("Circular dependencies: " + report.getCircularDependencies().size());
        System.out.println("Architecture violations: " + countViolations(report.getArchitectureViolations()));
        System.out.println("Code smells: " + countViolations(report.getCodeSmells()));
        System.out.println("Potential N+1 issues: " + countViolations(report.getNPlusOneViolations()));
    }

    private void printDependencyGraph(Map<JavaClass, Set<JavaClass>> dependencyGraph) {

        System.out.println();
        System.out.println("DEPENDENCY GRAPH");
        System.out.println("------------------");

        if (dependencyGraph.isEmpty()) {

            System.out.println("No dependencies found.");

            return;
        }

        for (Map.Entry<JavaClass, Set<JavaClass>> entry : dependencyGraph.entrySet()) {

            JavaClass source = entry.getKey();
            System.out.println(source.getPackageName() + "." + source.getName());

            for (JavaClass target : entry.getValue()) {

                System.out.println("    -> " + target.getPackageName() + "." + target.getName());
            }
        }
    }

    private void printCircularDependencies(Set<List<JavaClass>> cycles) {

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
                System.out.print(javaClass.getPackageName() + "." + javaClass.getName());

                if (i < cycle.size() - 1) {
                    System.out.print(" -> ");
                }
            }

            System.out.println();
        }
    }

    private void printArchitectureViolations(Map<JavaClass, Set<JavaClass>> violations) {

        System.out.println();
        System.out.println("ARCHITECTURE VIOLATIONS");
        System.out.println("------------------");

        if (violations.isEmpty()) {

            System.out.println("No architecture violations found.");

            return;
        }

        for (Map.Entry<JavaClass, Set<JavaClass>> entry : violations.entrySet()) {

            JavaClass source = entry.getKey();
            System.out.println(source.getPackageName() + "." + source.getName());

            for (JavaClass target : entry.getValue()) {

                System.out.println("    -> " + target.getPackageName() + "." + target.getName());
            }
        }
    }

    private void printCodeSmells(Map<JavaClass, Set<String>> smells) {

        System.out.println();
        System.out.println("CODE SMELLS");
        System.out.println("------------------");

        if (smells.isEmpty()) {

            System.out.println("No code smells found.");

            return;
        }

        for (Map.Entry<JavaClass, Set<String>> entry : smells.entrySet()) {

            JavaClass javaClass = entry.getKey();

            System.out.println(javaClass.getPackageName() + "." + javaClass.getName());

            for (String smell : entry.getValue()) {

                System.out.println("    ⚠ " + smell);
            }
        }
    }

    private void printNPlusOneViolations(Map<JavaClass, Set<String>> violations) {

        System.out.println();
        System.out.println("POTENTIAL N+1 QUERIES");
        System.out.println("------------------");

        if (violations.isEmpty()) {

            System.out.println("No potential N+1 queries found.");

            return;
        }

        for (Map.Entry<JavaClass, Set<String>> entry : violations.entrySet()) {

            JavaClass javaClass = entry.getKey();
            System.out.println(javaClass.getPackageName() + "." + javaClass.getName());

            for (String violation : entry.getValue()) {

                System.out.println("    ⚠ " + violation);
            }
        }
    }

    private int countDependencies(Map<JavaClass, Set<JavaClass>> graph) {

        int count = 0;

        for (Set<JavaClass> dependencies : graph.values()) {

            count += dependencies.size();
        }

        return count;
    }

    private int countViolations(Map<JavaClass, ? extends Set<?>> violations) {

        int count = 0;

        for (Set<?> values : violations.values()) {
            count += values.size();
        }

        return count;
    }
}
