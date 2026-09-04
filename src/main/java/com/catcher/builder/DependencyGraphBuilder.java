package com.catcher.builder;

import com.catcher.model.DependencyEdge;
import com.catcher.model.DependencyGraph;
import com.catcher.model.JavaClass;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DependencyGraphBuilder {

    public DependencyGraph build(Set<JavaClass> classes) {

        DependencyGraph graph = new DependencyGraph();

        Map<String, JavaClass> classesByName = new HashMap<>();

        for (JavaClass javaClass : classes) {

            classesByName.put(javaClass.getName(), javaClass);
        }

        for (JavaClass source : classes) {

            for (String dependency : source.getDependencies()) {

                JavaClass target = classesByName.get(dependency);

                if (target == null || target == source) {
                    continue;
                }

                if (isModelClass(source) || isModelClass(target)) {
                    continue;
                }

                graph.getEdges().add(new DependencyEdge(source, target));
            }
        }

        return graph;
    }

    private boolean isModelClass(JavaClass javaClass) {

        return javaClass.getAnnotations().contains("Entity") || javaClass.getAnnotations().contains("Embeddable");
    }
}
