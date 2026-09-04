package com.catcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DependencyEdge {

    private JavaClass source;
    private JavaClass target;
}
