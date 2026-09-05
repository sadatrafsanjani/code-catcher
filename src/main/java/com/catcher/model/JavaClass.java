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
}
