package com.catcher.analyzer;

import com.catcher.model.JavaClass;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CodeSmellDetector {

    private static final int LARGE_CLASS_METHOD_THRESHOLD = 20;
    private static final int HIGH_DEPENDENCY_THRESHOLD = 5;

    public Map<JavaClass, Set<String>> detect(Set<JavaClass> classes) {

        Map<JavaClass, Set<String>> smells = new LinkedHashMap<>();

        for (JavaClass javaClass : classes) {

            Set<String> classSmells = new java.util.LinkedHashSet<>();

            if (javaClass.getMethods().size() > LARGE_CLASS_METHOD_THRESHOLD) {

                classSmells.add("Large Class (" + javaClass.getMethods().size() + " methods)");
            }

            if (javaClass.getDependencies().size() > HIGH_DEPENDENCY_THRESHOLD) {

                classSmells.add("High Dependency Count (" + javaClass.getDependencies().size() + " dependencies)");
            }

            if (!classSmells.isEmpty()) {

                smells.put(javaClass, classSmells);
            }
        }

        return smells;
    }
}
