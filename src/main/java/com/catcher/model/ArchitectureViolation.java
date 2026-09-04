package com.catcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ArchitectureViolation {

    private String sourceClass;
    private String targetClass;
    private String message;
}
