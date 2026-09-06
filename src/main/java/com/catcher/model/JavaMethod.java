package com.catcher.model;

import com.github.javaparser.ast.body.MethodDeclaration;
import lombok.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaMethod {

    private String name;
    private String returnType;
    private int complexity;
    private int statementCount;
    private String body;
    private MethodDeclaration declaration;
    private Set<String> parameters = new LinkedHashSet<>();
    private Set<String> annotations = new LinkedHashSet<>();
}
