package com.catcher.model;

import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaClass {

    private String name;
    private String packageName;
    private String filePath;
    private boolean interfaceType;

    private final Set<JavaMethod> methods = new LinkedHashSet<>();
    private final Set<String> dependencies = new LinkedHashSet<>();
    private final Set<String> annotations = new LinkedHashSet<>();

    public void addMethod(JavaMethod method) {
        methods.add(method);
    }

    public void addDependency(String dependency) {
        dependencies.add(dependency);
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }
}
