package com.graydoll.calculadora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas unitarias simples — Happy Path.
 * Cada test sigue AAA: Arrange (preparar), Act (ejecutar), Assert (verificar).
 */
@DisplayName("Calculator — Happy Path (casos válidos)")
class CalculatorHappyPathTest {

    private static final double DELTA = 0.0001;

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("suma dos enteros positivos")
    void addPositiveIntegers() {
        // Arrange
        double left = 2;
        double right = 3;

        // Act
        double result = calculator.add(left, right);

        // Assert
        assertEquals(5, result, DELTA);
    }

    @Test
    @DisplayName("suma con decimales")
    void addDecimals() {
        // Arrange
        double left = 1.5;
        double right = 2.25;

        // Act
        double result = calculator.add(left, right);

        // Assert
        assertEquals(3.75, result, DELTA);
    }

    @Test
    @DisplayName("resta dos números")
    void subtractNumbers() {
        // Arrange
        double left = 10;
        double right = 4;

        // Act
        double result = calculator.subtract(left, right);

        // Assert
        assertEquals(6, result, DELTA);
    }

    @Test
    @DisplayName("resta que produce un negativo")
    void subtractResultingNegative() {
        // Arrange
        double left = 3;
        double right = 8;

        // Act
        double result = calculator.subtract(left, right);

        // Assert
        assertEquals(-5, result, DELTA);
    }

    @Test
    @DisplayName("multiplica dos números")
    void multiplyNumbers() {
        // Arrange
        double left = 6;
        double right = 7;

        // Act
        double result = calculator.multiply(left, right);

        // Assert
        assertEquals(42, result, DELTA);
    }

    @Test
    @DisplayName("multiplica por uno (identidad)")
    void multiplyByOne() {
        // Arrange
        double left = 9.5;
        double right = 1;

        // Act
        double result = calculator.multiply(left, right);

        // Assert
        assertEquals(9.5, result, DELTA);
    }

    @Test
    @DisplayName("divide dos números")
    void divideNumbers() {
        // Arrange
        double left = 20;
        double right = 4;

        // Act
        double result = calculator.divide(left, right);

        // Assert
        assertEquals(5, result, DELTA);
    }

    @Test
    @DisplayName("suma cero no cambia el valor (identidad aditiva)")
    void addZero() {
        // Arrange
        double left = 12;
        double right = 0;

        // Act
        double result = calculator.add(left, right);

        // Assert
        assertEquals(12, result, DELTA);
    }
}
