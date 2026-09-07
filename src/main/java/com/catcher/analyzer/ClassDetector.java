package com.catcher.analyzer;

import com.catcher.model.JavaClass;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ClassDetector {

    public Map<String, Set<JavaClass>> detect(Set<JavaClass> classes) {

        Map<String, Set<JavaClass>> detected = new LinkedHashMap<>();

        detected.put("CONTROLLERS", new LinkedHashSet<>());
        detected.put("SERVICES", new LinkedHashSet<>());
        detected.put("REPOSITORIES", new LinkedHashSet<>());
        detected.put("ENTITIES", new LinkedHashSet<>());
        detected.put("COMPONENTS", new LinkedHashSet<>());
        detected.put("CONFIGURATIONS", new LinkedHashSet<>());

        for (JavaClass javaClass : classes) {

            Set<String> annotations = javaClass.getAnnotations();

            if (annotations.contains("Controller") || annotations.contains("RestController")) {

                detected.get("CONTROLLERS").add(javaClass);
            }

            if (annotations.contains("Service")) {

                detected.get("SERVICES").add(javaClass);
            }

            if (annotations.contains("Repository")) {

                detected.get("REPOSITORIES").add(javaClass);
            }

            if (annotations.contains("Entity")) {

                detected.get("ENTITIES").add(javaClass);
            }

            if (annotations.contains("Component")) {

                detected.get("COMPONENTS").add(javaClass);
            }

            if (annotations.contains("Configuration")) {

                detected.get("CONFIGURATIONS").add(javaClass);
            }
        }

        return detected;
    }
}
