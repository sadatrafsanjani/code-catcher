package com.catcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisReport {

    private Set<JavaClass> classes;
    private Map<JavaClass, Set<JavaClass>> dependencyGraph;
    private Set<List<JavaClass>> circularDependencies;
    private Map<JavaClass, Set<JavaClass>> architectureViolations;
    private Map<JavaClass, Set<String>> codeSmells;
    private Map<JavaClass, Set<String>> nPlusOneViolations;
}
