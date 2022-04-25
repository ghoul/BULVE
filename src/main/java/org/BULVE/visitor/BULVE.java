package org.BULVE.visitor;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import java.util.*;

public class BULVE {
    public static void main(String[] args) {
        try {
            execute(CharStreams.fromFileName(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object execute(CharStream stream) {
        BULVELexer lexer = new BULVELexer(stream);
        BULVEParser parser = new BULVEParser(new CommonTokenStream(lexer));
        parser.setBuildParseTree(true);
        ParseTree tree = parser.program();

        BULVEVisitorImpl visitor = new BULVEVisitorImpl();
        return visitor.visit(tree);
    }
}
