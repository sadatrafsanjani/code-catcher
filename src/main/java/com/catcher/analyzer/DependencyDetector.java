package com.catcher.analyzer;

import com.catcher.model.JavaClass;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class DependencyDetector {

    public Map<JavaClass, Set<JavaClass>> detectDependencies(Set<JavaClass> classes) {

        Map<String, JavaClass> classIndex = new LinkedHashMap<>();
        Map<JavaClass, Set<JavaClass>> dependencyGraph = new LinkedHashMap<>();

        for (JavaClass javaClass : classes) {

            classIndex.put(javaClass.getName(), javaClass);
        }

        for (JavaClass javaClass : classes) {

            Set<JavaClass> dependencies = new LinkedHashSet<>();

            for (String dependencyName : javaClass.getDependencies()) {

                JavaClass dependencyClass = classIndex.get(dependencyName);

                if (dependencyClass != null) {

                    dependencies.add(dependencyClass);
                }
            }

            if (!dependencies.isEmpty()) {

                dependencyGraph.put(javaClass, dependencies);
            }
        }

        return dependencyGraph;
    }
}
