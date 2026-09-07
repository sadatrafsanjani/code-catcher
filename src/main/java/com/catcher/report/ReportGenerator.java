package com.catcher.report;

import com.catcher.analyzer.*;
import com.catcher.model.AnalysisReport;
import com.catcher.model.JavaClass;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportGenerator {

    public AnalysisReport generate(Set<JavaClass> classes) {

        DependencyDetector dependencyDetector = new DependencyDetector();
        Map<JavaClass, Set<JavaClass>> dependencyGraph = dependencyDetector.detectDependencies(classes);

        CircularDependencyDetector circularDependencyDetector = new CircularDependencyDetector();
        Set<List<JavaClass>> circularDependencies = circularDependencyDetector.detectCircularDependencies(dependencyGraph);

        ArchitectureViolationDetector architectureViolationDetector = new ArchitectureViolationDetector();
        Map<JavaClass, Set<JavaClass>> architectureViolations = architectureViolationDetector.detectArchitectureViolations(dependencyGraph);

        CodeSmellDetector codeSmellDetector = new CodeSmellDetector();
        Map<JavaClass, Set<String>> codeSmells = codeSmellDetector.detect(classes);

        NPlusOneDetector nPlusOneDetector = new NPlusOneDetector();
        Map<JavaClass, Set<String>> nPlusOneViolations = nPlusOneDetector.detect(classes);

        return new AnalysisReport(classes, dependencyGraph, circularDependencies, architectureViolations, codeSmells, nPlusOneViolations);
    }
}
