package com.catcher;

import com.catcher.model.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("=========== CODE CATCHER ===========\n");

        String projectPath = "E:\\sample";
        Initializer initializer = new Initializer(projectPath);
        initializer.initialize();
    }
}