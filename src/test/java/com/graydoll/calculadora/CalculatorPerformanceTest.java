package com.graydoll.calculadora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTimeout;

/**
 * Performance Unit Test: el cálculo debe terminar dentro de un tiempo acotado.
 * No sustituye un benchmark (JMH); valida que no hay regresiones groseras de tiempo.
 */
@DisplayName("Calculator — Performance Unit Test")
class CalculatorPerformanceTest {

    private static final int ITERATIONS = 10_000;

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    @DisplayName("10_000 operaciones mixtas terminan en menos de 100 ms")
    void mixedOperationsCompleteUnderTimeout() {
        // Arrange — calculator ya creado en setUp

        // Act
        for (int i = 1; i <= ITERATIONS; i++) {
            calculator.add(i, i);
            calculator.subtract(i, 1);
            calculator.multiply(i, 2);
            calculator.divide(i, 2);
        }

        // Assert — si supera 100 ms, JUnit falla por @Timeout
    }

    @Test
    @DisplayName("un millón de sumas no supera 200 ms")
    void oneMillionAdditionsStayFast() {
        // Arrange
        int n = 1_000_000;

        // Act + Assert
        assertTimeout(Duration.ofMillis(200), () -> {
            double acc = 0;
            for (int i = 0; i < n; i++) {
                acc = calculator.add(acc, 1);
            }
        });
    }
}
