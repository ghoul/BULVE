package org.BULVE.visitor;

import org.antlr.v4.runtime.CharStreams;
import org.BULVE.visitor.exception.BULVEVariableNotDeclaredException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScopeTest {

    @Test
    void no_exception() {
        String program = """
                var a = 5
                print(a)             
                """;

        String expected =
                """
                5
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void variable_not_declared_throws_exception() {
        String program = """
                var a = 5
                print(b)             
                """;

        BULVEVariableNotDeclaredException thrown = assertThrows(
                BULVEVariableNotDeclaredException.class,
                () -> BULVE.execute(CharStreams.fromString(program))
        );

        assertEquals("Variable 'b' is not declared.", thrown.getMessage());
    }

    @Test
    void value_changed_no_exception() {
        String program = """
                var a = 5
                if (true) {
                    a = 6
                } else { }
                print(a)
                """;

        String expected =
                """
                6
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void variable_out_of_scope_throws_exception() {
        String program = """
                var a = 5
                if (true) {
                    var b = 6
                } else { }
                print(b)
                """;

        BULVEVariableNotDeclaredException thrown = assertThrows(
                BULVEVariableNotDeclaredException.class,
                () -> BULVE.execute(CharStreams.fromString(program))
        );

        assertEquals("Variable 'b' is not declared.", thrown.getMessage());
    }

    @Test
    void variable_changes_in_another_block() {
        String program = """
                var a = 5
                if (true) {
                    a = 6
                    if (true) {
                        print(a)
                    } else { }
                } else { }
                """;

        String expected =
                """
                6
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void variable_declared_in_another_block() {
        String program = """
                if (true) {
                    var a = 5
                    if (true) {
                        print(a)
                    } else { }
                } else { }
                """;

        String expected =
                """
                5
                """;

        String actual = (String) BULVE.execute(CharStreams.fromString(program));

        assertEquals(expected, actual);
    }

    @Test
    void variable_declared_in_another_block_throws_exception() {
        String program = """
                if (true) {
                    if (true) {
                        var a = 5
                    } else { }
                    print(a)
                } else { }
                """;

        BULVEVariableNotDeclaredException thrown = assertThrows(
                BULVEVariableNotDeclaredException.class,
                () -> BULVE.execute(CharStreams.fromString(program))
        );

        assertEquals("Variable 'a' is not declared.", thrown.getMessage());
    }
}