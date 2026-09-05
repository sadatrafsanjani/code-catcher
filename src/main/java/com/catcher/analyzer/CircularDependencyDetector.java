package com.catcher.analyzer;

import com.catcher.model.JavaClass;

import java.util.*;

public class CircularDependencyDetector {

    public Set<List<JavaClass>> detectCircularDependencies(Map<JavaClass, Set<JavaClass>> dependencyGraph) {

        Set<List<JavaClass>> cycles = new LinkedHashSet<>();

        Set<JavaClass> visited = new HashSet<>();
        Set<JavaClass> currentPath = new LinkedHashSet<>();

        for (JavaClass javaClass : dependencyGraph.keySet()) {

            detectCycle(
                    javaClass,
                    dependencyGraph,
                    visited,
                    currentPath,
                    new ArrayList<>(),
                    cycles
            );
        }

        return cycles;
    }

    private void detectCycle(JavaClass current, Map<JavaClass, Set<JavaClass>> dependencyGraph, Set<JavaClass> visited, Set<JavaClass> currentPath, List<JavaClass> path, Set<List<JavaClass>> cycles) {

        if (currentPath.contains(current)) {

            int cycleStart = path.indexOf(current);

            if (cycleStart >= 0) {

                List<JavaClass> cycle = new ArrayList<>(path.subList(cycleStart, path.size()));
                cycle.add(current);
                cycles.add(cycle);
            }

            return;
        }

        if (visited.contains(current)) {
            return;
        }

        visited.add(current);
        currentPath.add(current);
        path.add(current);

        Set<JavaClass> dependencies =
                dependencyGraph.getOrDefault(current, Collections.emptySet());

        for (JavaClass dependency : dependencies) {

            detectCycle(
                    dependency,
                    dependencyGraph,
                    visited,
                    currentPath,
                    path,
                    cycles
            );
        }

        path.remove(path.size() - 1);
        currentPath.remove(current);
    }
}
