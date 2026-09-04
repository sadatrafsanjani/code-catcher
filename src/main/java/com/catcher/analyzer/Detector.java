package com.catcher.analyzer;

import com.catcher.model.JavaClass;
import java.util.LinkedHashSet;
import java.util.Set;

public class Detector {

    public Set<JavaClass> detectControllers(Set<JavaClass> classes) {

        Set<JavaClass> list = new LinkedHashSet<>();

        for (JavaClass javaClass : classes) {

            if (javaClass.getAnnotations().contains("Controller") || javaClass.getAnnotations().contains("RestController")) {

                list.add(javaClass);
            }
        }

        return list;
    }

    public Set<JavaClass> detectServices(Set<JavaClass> classes) {

        Set<JavaClass> list = new LinkedHashSet<>();

        for (JavaClass javaClass : classes) {

            if (javaClass.getAnnotations().contains("Service")) {

                list.add(javaClass);
            }
        }

        return list;
    }

    public Set<JavaClass> detectRepositories(Set<JavaClass> classes) {

        Set<JavaClass> list = new LinkedHashSet<>();

        for (JavaClass javaClass : classes) {

            if (javaClass.getAnnotations().contains("Repository")) {

                list.add(javaClass);
            }
        }

        return list;
    }

    public Set<JavaClass> detectEntities(Set<JavaClass> classes) {

        Set<JavaClass> list = new LinkedHashSet<>();

        for (JavaClass javaClass : classes) {

            if (javaClass.getAnnotations().contains("Entity")) {

                list.add(javaClass);
            }
        }

        return list;
    }

    public Set<JavaClass> detectConfigurations(Set<JavaClass> classes) {

        Set<JavaClass> list = new LinkedHashSet<>();

        for (JavaClass javaClass : classes) {

            if (javaClass.getAnnotations().contains("Configuration")) {

                list.add(javaClass);
            }
        }

        return list;
    }

    public Set<JavaClass> detectComponents(Set<JavaClass> classes) {

        Set<JavaClass> list = new LinkedHashSet<>();

        for (JavaClass javaClass : classes) {

            if (javaClass.getAnnotations().contains("Component")) {

                list.add(javaClass);
            }
        }

        return list;
    }
}
