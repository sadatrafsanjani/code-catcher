package com.catcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaProject {

    private Set<JavaClass> classes = new LinkedHashSet<>();

    public void addClass(JavaClass javaClass) {
        classes.add(javaClass);
    }
}
