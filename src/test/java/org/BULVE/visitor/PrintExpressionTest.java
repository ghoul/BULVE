/*
package org.BULVE.visitor;

import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrintExpressionTest {

    @Test
    void print_add_expression() {
        String program = """
                print(2+3)                
                """;

        String expected =
                """
                5
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void print_multiplication_expression() {
        String program = """
                print(2*3)        
                """;

        String expected =
                """
                6
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void print_complex1_expression() {
        String program = """
                print(1+2*3)
                """;

        String expected =
                """
                7
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void print_complex2_expression() {
        String program = """
                print(8/2*(2+2))
                """;

        String expected =
                """
                16
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }
}*/
