package com.catcher.builder;

import com.catcher.model.JavaClass;
import com.catcher.model.JavaMethod;
import com.catcher.model.JavaProject;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.stmt.*;
import lombok.NoArgsConstructor;
import java.nio.file.Path;

@NoArgsConstructor
public class CodeModelBuilder {

    public JavaProject build(CompilationUnit compilationUnit, Path sourceFile) {

        JavaProject project = new JavaProject();

        String packageName = compilationUnit.getPackageDeclaration().map(NodeWithName::getNameAsString).orElse("");

        compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(classDeclaration -> {

            JavaClass javaClass = JavaClass.builder()
                    .name(classDeclaration.getNameAsString())
                    .packageName(packageName)
                    .filePath(sourceFile.toAbsolutePath().toString())
                    .interfaceType(classDeclaration.isInterface())
                    .build();

            classDeclaration.getAnnotations().forEach(annotation -> javaClass.getAnnotations().add(annotation.getNameAsString()));
            classDeclaration.getMethods().forEach(method -> javaClass.getMethods().add(buildMethod(method)));
            classDeclaration.getFields().forEach(field -> field.getVariables().forEach(variable -> javaClass.getDependencies().add(variable.getType().asString())));
            project.getClasses().add(javaClass);
        });

        return project;
    }

    private JavaMethod buildMethod(MethodDeclaration declaration) {

        JavaMethod method = new JavaMethod();

        method.setName(declaration.getNameAsString());
        method.setReturnType(declaration.getType().asString());
        declaration.getParameters().forEach(parameter -> method.getParameters().add(parameter.getNameAsString()));
        declaration.getAnnotations().forEach(annotation -> method.getAnnotations().add(annotation.getNameAsString()));

        method.setComplexity(calculateComplexity(declaration));
        method.setStatementCount(declaration.getBody().map(body -> body.findAll(Statement.class).size()).orElse(0));
        method.setBody(declaration.getBody().map(Object::toString).orElse(""));
        method.setDeclaration(declaration);

        return method;
    }

    private int calculateComplexity(MethodDeclaration declaration) {

        int complexity = 1;

        complexity += declaration.findAll(IfStmt.class).size();
        complexity += declaration.findAll(ForStmt.class).size();
        complexity += declaration.findAll(ForEachStmt.class).size();
        complexity += declaration.findAll(WhileStmt.class).size();
        complexity += declaration.findAll(DoStmt.class).size();
        complexity += declaration.findAll(CatchClause.class).size();
        complexity += declaration.findAll(SwitchEntry.class).size();

        complexity += declaration.findAll(BinaryExpr.class).stream().filter(
                expression -> expression.getOperator() == BinaryExpr.Operator.AND || expression.getOperator() == BinaryExpr.Operator.OR).count();

        return complexity;
    }
}
