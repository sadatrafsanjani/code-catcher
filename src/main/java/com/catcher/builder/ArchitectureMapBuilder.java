package com.catcher.builder;

import com.catcher.model.ArchitectureMap;
import com.catcher.model.JavaClass;
import java.util.Set;

public class ArchitectureMapBuilder {

    public ArchitectureMap build(Set<JavaClass> controllers, Set<JavaClass> services, Set<JavaClass> repositories) {

        ArchitectureMap architectureMap = new ArchitectureMap();

        architectureMap.setControllers(controllers);
        architectureMap.setServices(services);
        architectureMap.setRepositories(repositories);

        return architectureMap;
    }
}
