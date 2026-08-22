package com.graydoll.calculadora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Exceptional Path: entradas inválidas o estados que deben fallar de forma controlada.
 */
@DisplayName("Calculator — Exceptional Path")
class CalculatorExceptionalPathTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("dividir por cero lanza DivisionByZeroException")
    void divideByZeroThrows() {
        // Arrange
        double left = 10;
        double right = 0;

        // Act + Assert
        DivisionByZeroException exception = assertThrows(
                DivisionByZeroException.class,
                () -> calculator.divide(left, right)
        );

        assertEquals("No se puede dividir por cero", exception.getMessage());
    }

    @Test
    @DisplayName("dividir por -0.0 también se trata como cero")
    void divideByNegativeZeroThrows() {
        // Arrange
        double left = 5;
        double right = -0.0;

        // Act + Assert
        assertThrows(DivisionByZeroException.class, () -> calculator.divide(left, right));
    }

    @Test
    @DisplayName("0 dividido por 0 también es inválido")
    void zeroDividedByZeroThrows() {
        // Arrange
        double left = 0;
        double right = 0;

        // Act + Assert
        assertThrows(DivisionByZeroException.class, () -> calculator.divide(left, right));
    }
}
