package com.catcher.analyzer;

import com.catcher.model.JavaClass;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class DependencyDetector {

    public Map<JavaClass, Set<JavaClass>> detectDependencies(Set<JavaClass> classes) {

        Map<JavaClass, Set<JavaClass>> dependencyGraph = new LinkedHashMap<>();

        for (JavaClass javaClass : classes) {

            Set<JavaClass> dependencies = new LinkedHashSet<>();

            for (String dependencyName : javaClass.getDependencies()) {

                for (JavaClass dependencyClass : classes) {

                    if (dependencyClass.getName().equals(dependencyName)) {

                        dependencies.add(dependencyClass);
                    }
                }
            }

            if (!dependencies.isEmpty()) {

                dependencyGraph.put(javaClass, dependencies);
            }
        }

        return dependencyGraph;
    }
}
