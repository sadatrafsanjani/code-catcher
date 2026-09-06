package com.catcher.analyzer;

import com.catcher.model.JavaClass;
import com.catcher.model.JavaMethod;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ForEachStmt;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class NPlusOneDetector {

    public Map<JavaClass, Set<String>> detect(Set<JavaClass> classes) {

        Map<JavaClass, Set<String>> violations = new LinkedHashMap<>();

        for (JavaClass javaClass : classes) {

            for (JavaMethod javaMethod : javaClass.getMethods()) {

                MethodDeclaration declaration = javaMethod.getDeclaration();

                if (declaration == null) {

                    continue;
                }

                Set<String> findings = detectMethod(declaration);

                if (!findings.isEmpty()) {

                    violations.computeIfAbsent(javaClass, key -> new LinkedHashSet<>()).addAll(findings);
                }
            }
        }

        return violations;
    }

    private Set<String> detectMethod(MethodDeclaration declaration) {

        Set<String> findings = new LinkedHashSet<>();

        for (ForEachStmt loop : declaration.findAll(ForEachStmt.class)) {

            String loopVariable = loop.getVariable().getVariable(0).getNameAsString();
            String collectionVariable = loop.getIterable().toString();

            for (MethodCallExpr call : loop.getBody().findAll(MethodCallExpr.class)) {

                if (call.getScope().isEmpty()) {

                    continue;
                }

                String scope = call.getScope().get().toString();

                if (!scope.equals(loopVariable)) {

                    continue;
                }

                String getter = call.getNameAsString();

                if (!getter.startsWith("get") && !getter.startsWith("find") && !getter.startsWith("load")) {

                    continue;
                }

                findings.add("Potential N+1: collection '" + collectionVariable + "' is iterated and '" + loopVariable + "." + getter + "()' is accessed inside loop");
            }
        }

        return findings;
    }
}
