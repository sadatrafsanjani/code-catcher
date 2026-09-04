package com.catcher.builder;

import com.catcher.model.JavaClass;
import com.catcher.model.JavaMethod;
import com.catcher.model.JavaProject;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import lombok.NoArgsConstructor;
import java.nio.file.Path;

@NoArgsConstructor
public class CodeModelBuilder {

    public JavaProject build(CompilationUnit compilationUnit, Path sourceFile) {

        JavaProject project = new JavaProject();

        String packageName = compilationUnit.getPackageDeclaration().map(NodeWithName::getNameAsString).orElse("");

        compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(classDeclaration -> {

                    JavaClass javaClass = new JavaClass(classDeclaration.getNameAsString(), packageName, sourceFile.toString(), classDeclaration.isInterface());

                    classDeclaration.getAnnotations().forEach(annotation -> javaClass.getAnnotations().add(annotation.getNameAsString()));

                    classDeclaration.getMethods().forEach(method -> javaClass.getMethods().add(buildMethod(method)));

                    classDeclaration.getFields().forEach(field -> field.getVariables().forEach(variable -> javaClass.getDependencies().add(variable.getType().asString())));

                    project.getClasses().add(javaClass);
                });

        return project;
    }

    private JavaMethod buildMethod(MethodDeclaration declaration) {

        JavaMethod method = new JavaMethod(declaration.getNameAsString(), declaration.getType().asString(), new java.util.LinkedHashSet<>(), new java.util.LinkedHashSet<>());

        declaration.getParameters().forEach(parameter -> method.getParameters().add(parameter.getNameAsString()));

        declaration.getAnnotations().forEach(annotation -> method.getAnnotations().add(annotation.getNameAsString()));

        return method;
    }
}
