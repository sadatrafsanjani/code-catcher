package com.catcher.model;

import lombok.Data;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class DependencyGraph {

    private Set<DependencyEdge> edges = new LinkedHashSet<>();
}
