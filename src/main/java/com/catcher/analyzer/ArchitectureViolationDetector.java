package com.catcher.analyzer;

import com.catcher.model.JavaClass;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ArchitectureViolationDetector {

    public Map<JavaClass, Set<JavaClass>> detectArchitectureViolations(Map<JavaClass, Set<JavaClass>> dependencyGraph) {

        Map<JavaClass, Set<JavaClass>> violations = new LinkedHashMap<>();

        for (Map.Entry<JavaClass, Set<JavaClass>> entry : dependencyGraph.entrySet()) {

            JavaClass source = entry.getKey();

            for (JavaClass target : entry.getValue()) {

                if (isViolation(source, target)) {

                    violations.computeIfAbsent(source, key -> new LinkedHashSet<>()).add(target);
                }
            }
        }

        return violations;
    }

    private boolean isViolation(JavaClass source, JavaClass target) {

        boolean sourceController = isController(source);
        boolean sourceService = isService(source);
        boolean sourceRepository = isRepository(source);

        boolean targetController = isController(target);
        boolean targetService = isService(target);
        boolean targetRepository = isRepository(target);

        if (sourceController && targetRepository) {
            return true;
        }

        if (sourceService && targetController) {
            return true;
        }

        if (sourceRepository && targetService) {
            return true;
        }

        if (sourceRepository && targetController) {
            return true;
        }

        return false;
    }

    private boolean isController(JavaClass javaClass) {

        return javaClass.getAnnotations().contains("Controller") || javaClass.getAnnotations().contains("RestController");
    }

    private boolean isService(JavaClass javaClass) {

        return javaClass.getAnnotations().contains("Service");
    }

    private boolean isRepository(JavaClass javaClass) {

        return javaClass.getAnnotations().contains("Repository");
    }
}
