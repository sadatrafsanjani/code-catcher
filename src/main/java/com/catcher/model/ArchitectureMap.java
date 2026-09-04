package com.catcher.model;

import lombok.Data;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class ArchitectureMap {

    private Set<JavaClass> controllers = new LinkedHashSet<>();
    private Set<JavaClass> services = new LinkedHashSet<>();
    private Set<JavaClass> repositories = new LinkedHashSet<>();
}
