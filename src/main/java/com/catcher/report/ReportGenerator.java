package com.catcher.report;

import com.catcher.analyzer.*;
import com.catcher.model.AnalysisReport;
import com.catcher.model.JavaClass;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReportGenerator {

//    public AnalysisReport generate(Set<JavaClass> classes) {
//
//        DependencyDetector dependencyDetector = new DependencyDetector();
//        Map<JavaClass, Set<JavaClass>> dependencyGraph = dependencyDetector.detectDependencies(classes);
//
//        CircularDependencyDetector circularDependencyDetector = new CircularDependencyDetector();
//        Set<List<JavaClass>> circularDependencies = circularDependencyDetector.detectCircularDependencies(dependencyGraph);
//
//        ArchitectureViolationDetector architectureViolationDetector = new ArchitectureViolationDetector();
//        Map<JavaClass, Set<JavaClass>> architectureViolations = architectureViolationDetector.detectArchitectureViolations(dependencyGraph);
//
//        CodeSmellDetector codeSmellDetector = new CodeSmellDetector();
//        Map<JavaClass, Set<String>> codeSmells = codeSmellDetector.detect(classes);
//
//        NPlusOneDetector nPlusOneDetector = new NPlusOneDetector();
//        Map<JavaClass, Set<String>> nPlusOneViolations = nPlusOneDetector.detect(classes);
//
//        return new AnalysisReport(classes, dependencyGraph, circularDependencies, architectureViolations, codeSmells, nPlusOneViolations);
//    }

    public AnalysisReport generate(Set<JavaClass> classes) {

        DependencyDetector dependencyDetector = new DependencyDetector();
        Map<JavaClass, Set<JavaClass>> dependencyGraph = dependencyDetector.detectDependencies(classes);

        int threadCount = Math.max(1, Math.min(4, Runtime.getRuntime().availableProcessors()));
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {

            Future<Set<List<JavaClass>>> circularDependenciesFuture = executor.submit(() -> {

                CircularDependencyDetector detector = new CircularDependencyDetector();

                return detector.detectCircularDependencies(dependencyGraph);
            });

            Future<Map<JavaClass, Set<JavaClass>>> architectureViolationsFuture = executor.submit(() -> {

                ArchitectureViolationDetector detector = new ArchitectureViolationDetector();

                return detector.detectArchitectureViolations(dependencyGraph);
            });

            Future<Map<JavaClass, Set<String>>> codeSmellsFuture = executor.submit(() -> {

                CodeSmellDetector detector = new CodeSmellDetector();

                return detector.detect(classes);
            });

            Future<Map<JavaClass, Set<String>>> nPlusOneViolationsFuture = executor.submit(() -> {

                NPlusOneDetector detector = new NPlusOneDetector();

                return detector.detect(classes);
            });

            Set<List<JavaClass>> circularDependencies = getResult(circularDependenciesFuture);
            Map<JavaClass, Set<JavaClass>> architectureViolations = getResult(architectureViolationsFuture);
            Map<JavaClass, Set<String>> codeSmells = getResult(codeSmellsFuture);
            Map<JavaClass, Set<String>> nPlusOneViolations = getResult(nPlusOneViolationsFuture);

            return new AnalysisReport(classes, dependencyGraph, circularDependencies, architectureViolations, codeSmells, nPlusOneViolations);

        }
        finally {

            executor.shutdown();
        }
    }

    private <T> T getResult(Future<T> future) {

        try {

            return future.get();

        }
        catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException("Analysis was interrupted.", e);
        }
        catch (ExecutionException e) {

            throw new RuntimeException("Analysis detector failed.", e.getCause());
        }
    }
}
