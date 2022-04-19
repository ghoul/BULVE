# BULVE-visitor
Simple programing language created with ANTLR4 Visitor.

#### Tools

> Oracle OpenJDK 17.0.2

> Apache Maven 3.8.5

> IntelliJ IDEA Ultimate 2021.3.3
> - ANTLR v4 plugin

#### Build

Generate ANTLR visitor with:

    mvn clean antlr4:antlr4@generate-visitor

Build jar with:

    mvn clean package

Run jar with:

    java -jar target/glang-1.0.jar samples/test.glang
