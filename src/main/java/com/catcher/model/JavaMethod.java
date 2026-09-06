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
    private int complexity;
    private int statementCount;
    private String body;
    private Set<String> parameters = new LinkedHashSet<>();
    private Set<String> annotations = new LinkedHashSet<>();
}
