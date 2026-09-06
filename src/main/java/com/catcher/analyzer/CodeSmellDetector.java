package com.catcher.analyzer;

import com.catcher.model.JavaClass;
import com.catcher.model.JavaMethod;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CodeSmellDetector {

    private static final int LARGE_CLASS_METHOD_THRESHOLD = 20;
    private static final int HIGH_DEPENDENCY_THRESHOLD = 5;

    private static final int GOD_CLASS_METHOD_THRESHOLD = 30;
    private static final int GOD_CLASS_DEPENDENCY_THRESHOLD = 10;

    private static final int COMPLEXITY_THRESHOLD = 10;
    private static final int LONG_METHOD_STATEMENT_THRESHOLD = 30;

    public Map<JavaClass, Set<String>> detect(Set<JavaClass> classes) {

        Map<JavaClass, Set<String>> smells = new LinkedHashMap<>();

        for (JavaClass javaClass : classes) {

            Set<String> classSmells = new LinkedHashSet<>();

            detectLargeClass(javaClass, classSmells);
            detectHighDependencyCount(javaClass, classSmells);
            detectGodClass(javaClass, classSmells);
            detectBadClassName(javaClass, classSmells);

            for (JavaMethod method : javaClass.getMethods()) {
                detectComplexity(method, classSmells);
                detectLongMethod(method, classSmells);
                detectBadMethodName(method, classSmells);
                detectMagicNumbers(method, classSmells);
            }

            detectDuplicateCode(javaClass, classSmells);

            if (!classSmells.isEmpty()) {
                smells.put(javaClass, classSmells);
            }
        }

        return smells;
    }

    private void detectLargeClass(JavaClass javaClass, Set<String> smells) {

        int methodCount = javaClass.getMethods().size();

        if (methodCount > LARGE_CLASS_METHOD_THRESHOLD) {

            smells.add("Large Class (" + methodCount + " methods)");
        }
    }

    private void detectHighDependencyCount(JavaClass javaClass, Set<String> smells) {

        int dependencyCount = javaClass.getDependencies().size();

        if (dependencyCount > HIGH_DEPENDENCY_THRESHOLD) {

            smells.add("High Dependency Count (" + dependencyCount + " dependencies)");
        }
    }

    private void detectGodClass(JavaClass javaClass, Set<String> smells) {

        int methodCount = javaClass.getMethods().size();
        int dependencyCount = javaClass.getDependencies().size();

        if (methodCount > GOD_CLASS_METHOD_THRESHOLD && dependencyCount > GOD_CLASS_DEPENDENCY_THRESHOLD) {

            smells.add("God Class (" + methodCount + " methods, " + dependencyCount + " dependencies)");
        }
    }

    private void detectComplexity(JavaMethod method, Set<String> smells) {

        if (method.getComplexity() > COMPLEXITY_THRESHOLD) {

            smells.add("High Complexity: " + method.getName() + " (" + method.getComplexity() + ")");
        }
    }

    private void detectLongMethod(JavaMethod method, Set<String> smells) {

        if (method.getStatementCount() > LONG_METHOD_STATEMENT_THRESHOLD) {

            smells.add("Long Method: " + method.getName() + " (" + method.getStatementCount() + " statements)");
        }
    }

    private void detectBadClassName(JavaClass javaClass, Set<String> smells) {

        if (!isPascalCase(javaClass.getName())) {

            smells.add("Bad Class Naming: " + javaClass.getName());
        }
    }

    private void detectBadMethodName(JavaMethod method, Set<String> smells) {

        if (!isCamelCase(method.getName())) {

            smells.add("Bad Method Naming: " + method.getName());
        }
    }

    private boolean isPascalCase(String name) {

        if (name == null || name.isEmpty()) {
            return false;
        }

        return name.matches("[A-Z][a-zA-Z0-9]*");
    }

    private boolean isCamelCase(String name) {

        if (name == null || name.isEmpty()) {
            return false;
        }

        return name.matches("[a-z][a-zA-Z0-9]*");
    }

    private void detectMagicNumbers(JavaMethod method, Set<String> smells) {

        if (method.getBody() == null || method.getBody().isEmpty()) {
            return;
        }

        String body = method.getBody();

        if (body.matches("(?s).*\\b(2|3|4|5|6|7|8|9|10|100|1000)\\b.*")) {

            smells.add("Magic Number: " + method.getName());
        }
    }

    private void detectDuplicateCode(JavaClass javaClass, Set<String> smells) {

        Set<JavaMethod> methods = javaClass.getMethods();

        for (JavaMethod method1 : methods) {

            if (method1.getBody() == null || method1.getBody().isEmpty()) {
                continue;
            }

            for (JavaMethod method2 : methods) {

                if (method1 == method2) {
                    continue;
                }

                if (method2.getBody() == null || method2.getBody().isEmpty()) {
                    continue;
                }

                String body1 = normalize(method1.getBody());
                String body2 = normalize(method2.getBody());

                if (body1.length() > 20 && body1.equals(body2)) {

                    smells.add("Duplicate Code: " + method1.getName() + " and " + method2.getName());

                    return;
                }
            }
        }
    }

    private String normalize(String body) {

        return body
                .replaceAll("\\s+", "")
                .replaceAll("//.*", "")
                .replaceAll("/\\*.*?\\*/", "");
    }
}
