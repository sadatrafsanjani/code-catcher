package com.catcher.analyzer;

import com.catcher.model.DependencyEdge;
import com.catcher.model.DependencyGraph;
import com.catcher.model.JavaClass;
import java.util.*;

public class CircularDependencyDetector {

    public Set<List<JavaClass>> detect(DependencyGraph graph) {

        Set<List<JavaClass>> cycles = new LinkedHashSet<>();
        Set<JavaClass> classes = new LinkedHashSet<>();

        for (DependencyEdge edge : graph.getEdges()) {

            classes.add(edge.getSource());
            classes.add(edge.getTarget());
        }

        for (JavaClass start : classes) {

            List<JavaClass> path = new ArrayList<>();
            Set<JavaClass> visited = new HashSet<>();
            findCycles(start, start, graph, path, visited, cycles);
        }

        return cycles;
    }

    private void findCycles(JavaClass current, JavaClass start, DependencyGraph graph, List<JavaClass> path, Set<JavaClass> visited, Set<List<JavaClass>> cycles) {

        path.add(current);
        visited.add(current);

        for (DependencyEdge edge : graph.getEdges()) {

            if (edge.getSource() != current) {
                continue;
            }

            JavaClass next = edge.getTarget();

            if (next == start && path.size() > 1) {

                List<JavaClass> cycle = new ArrayList<>(path);

                if (!containsEquivalentCycle(cycles, cycle)) {
                    cycles.add(cycle);
                }

                continue;
            }

            if (visited.contains(next)) {
                continue;
            }

            findCycles(next, start, graph, path, visited, cycles);
        }

        path.remove(path.size() - 1);
        visited.remove(current);
    }

    private boolean containsEquivalentCycle(Set<List<JavaClass>> cycles, List<JavaClass> candidate) {

        Set<JavaClass> candidateSet = new HashSet<>(candidate);

        for (List<JavaClass> cycle : cycles) {

            if (cycle.size() == candidate.size() && new HashSet<>(cycle).equals(candidateSet)) {

                return true;
            }
        }

        return false;
    }
}
