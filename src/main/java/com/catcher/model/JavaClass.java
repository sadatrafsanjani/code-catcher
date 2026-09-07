package com.catcher.model;

import lombok.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JavaClass {

    private String name;
    private String packageName;
    private String filePath;
    private boolean interfaceType;

    @Builder.Default
    private final Set<JavaMethod> methods = new LinkedHashSet<>();

    @Builder.Default
    private final Set<String> dependencies = new LinkedHashSet<>();

    @Builder.Default
    private final Set<String> annotations = new LinkedHashSet<>();
}
