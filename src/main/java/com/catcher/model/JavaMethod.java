package com.catcher.model;

import lombok.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaMethod {

    private String name;
    private String returnType;

    private Set<String> parameters = new LinkedHashSet<>();
    private Set<String> annotations = new LinkedHashSet<>();

    public void addParameter(String parameter) {
        parameters.add(parameter);
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }
}
