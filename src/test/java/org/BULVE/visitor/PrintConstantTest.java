package org.BULVE.visitor;

import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrintConstantTest {

    @Test
    void print_integer_constant() {
        String program = """
                print(5)                
                """;

        String expected =
                """
                5
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void print_multiple_integers_constant() {
        String program = """
                print(5)
                print(13)             
                """;

        String expected =
                """
                5
                13
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void print_decimal_constant() {
        String program = """
                print(5.5)
                """;

        String expected =
                """
                5.5
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void print_boolean_constant() {
        String program = """
                print(true)
                """;

        String expected =
                """
                true
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void print_string_constant() {
        String program = """
                print("5")
                """;

        String expected =
                """
                5
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }
}