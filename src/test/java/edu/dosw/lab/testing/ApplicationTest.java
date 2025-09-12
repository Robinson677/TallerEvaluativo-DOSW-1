package edu.dosw.lab.testing;

import edu.dosw.lab.Application;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ApplicationTest {

    @Test
    void shouldInstantiateApplication() {
        assertDoesNotThrow(Application::new);
    }
}
