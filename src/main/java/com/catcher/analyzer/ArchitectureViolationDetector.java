package com.catcher.analyzer;

import com.catcher.model.ArchitectureMap;
import com.catcher.model.ArchitectureViolation;
import com.catcher.model.JavaClass;
import java.util.LinkedHashSet;
import java.util.Set;

public class ArchitectureViolationDetector {

    public Set<ArchitectureViolation> detect(ArchitectureMap architectureMap) {

        Set<ArchitectureViolation> violations = new LinkedHashSet<>();
        Set<JavaClass> controllers = architectureMap.getControllers();
        Set<JavaClass> services = architectureMap.getServices();
        Set<JavaClass> repositories = architectureMap.getRepositories();

        for (JavaClass controller : controllers) {

            for (JavaClass repository : repositories) {

                if (dependsOn(controller, repository)) {

                    violations.add(new ArchitectureViolation(getFullName(controller), getFullName(repository), "Controller directly depends on Repository"));
                }
            }
        }

        for (JavaClass repository : repositories) {

            for (JavaClass service : services) {

                if (dependsOn(repository, service)) {

                    violations.add(new ArchitectureViolation(getFullName(repository), getFullName(service), "Repository depends on Service"));
                }
            }
        }

        for (JavaClass repository : repositories) {

            for (JavaClass controller : controllers) {

                if (dependsOn(repository, controller)) {

                    violations.add(new ArchitectureViolation(getFullName(repository), getFullName(controller), "Repository depends on Controller"));
                }
            }
        }

        for (JavaClass controller : controllers) {

            for (JavaClass otherController : controllers) {

                if (controller == otherController) {
                    continue;
                }

                if (dependsOn(controller, otherController)) {

                    violations.add(new ArchitectureViolation(getFullName(controller), getFullName(otherController), "Controller depends on another Controller"));
                }
            }
        }

        return violations;
    }

    private boolean dependsOn(JavaClass source, JavaClass target) {

        return source.getDependencies().contains(target.getName());
    }

    private String getFullName(JavaClass javaClass) {

        return javaClass.getPackageName() + "." + javaClass.getName();
    }
}
