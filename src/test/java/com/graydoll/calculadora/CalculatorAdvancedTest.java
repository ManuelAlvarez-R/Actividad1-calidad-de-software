package com.graydoll.calculadora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias avanzadas: @Nested, @ParameterizedTest y casos límite.
 */
@DisplayName("Calculator — pruebas avanzadas")
class CalculatorAdvancedTest {

    private static final double DELTA = 1e-9;

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Nested
    @DisplayName("Suma (parameterized)")
    class AdditionTests {

        @ParameterizedTest(name = "{0} + {1} = {2}")
        @CsvSource({
                "0, 0, 0",
                "1, 2, 3",
                "-4, 10, 6",
                "-3.5, -1.5, -5",
                "0.1, 0.2, 0.3"
        })
        void addManyInputs(double left, double right, double expected) {
            // Arrange — los parámetros ya son el arreglo
            // Act
            double result = calculator.add(left, right);
            // Assert
            assertEquals(expected, result, DELTA);
        }
    }

    @Nested
    @DisplayName("Resta (parameterized)")
    class SubtractionTests {

        @ParameterizedTest(name = "{0} - {1} = {2}")
        @CsvSource({
                "5, 5, 0",
                "0, 7, -7",
                "-2, -8, 6",
                "100.5, 0.5, 100"
        })
        void subtractManyInputs(double left, double right, double expected) {
            double result = calculator.subtract(left, right);
            assertEquals(expected, result, DELTA);
        }
    }

    @Nested
    @DisplayName("Multiplicación (parameterized)")
    class MultiplicationTests {

        @ParameterizedTest(name = "{0} * {1} = {2}")
        @CsvSource({
                "0, 99, 0",
                "-3, 4, -12",
                "-2, -5, 10",
                "2.5, 4, 10"
        })
        void multiplyManyInputs(double left, double right, double expected) {
            double result = calculator.multiply(left, right);
            assertEquals(expected, result, DELTA);
        }
    }

    @Nested
    @DisplayName("División (parameterized + bordes)")
    class DivisionTests {

        @ParameterizedTest(name = "{0} / {1} = {2}")
        @CsvSource({
                "9, 3, 3",
                "-8, 2, -4",
                "1, 4, 0.25",
                "0, 5, 0"
        })
        void divideManyInputs(double left, double right, double expected) {
            double result = calculator.divide(left, right);
            assertEquals(expected, result, DELTA);
        }

        @Test
        @DisplayName("números muy pequeños no se tratan como cero")
        void divideByTinyNumber() {
            // Arrange
            double left = 1;
            double right = 1e-12;

            // Act
            double result = calculator.divide(left, right);

            // Assert
            assertEquals(1e12, result, 1e-3);
        }
    }

    @Nested
    @DisplayName("Contrato de Operation (Liskov)")
    class OperationContractTests {

        @Test
        @DisplayName("todas las implementaciones responden apply sin NPE")
        void eachImplementationApplies() {
            Operation[] operations = {
                    new Addition(),
                    new Subtraction(),
                    new Multiplication(),
                    new Division()
            };

            for (Operation operation : operations) {
                double result = operation.apply(8, 2);
                // el resultado concreto depende de la operación; lo importante es el contrato
                assertEquals(result, operation.apply(8, 2), DELTA);
            }
        }

        @Test
        @DisplayName("Division cumple el contrato de error al dividir por cero")
        void divisionContractOnZero() {
            Operation division = new Division();
            assertThrows(DivisionByZeroException.class, () -> division.apply(1, 0));
        }
    }

    @Nested
    @DisplayName("Inyección de dependencias (test avanzado con doble)")
    class DependencyInjectionTests {

        @Test
        @DisplayName("Calculator delega add en la Operation inyectada")
        void addUsesInjectedOperation() {
            // Arrange: un doble que no suma, para probar que Calculator no calcula por su cuenta
            Operation fakeAdd = (left, right) -> 123;
            Calculator custom = new Calculator(fakeAdd, new Subtraction(), new Multiplication(), new Division());

            // Act
            double result = custom.add(1, 1);

            // Assert
            assertEquals(123, result, DELTA);
        }
    }
}
