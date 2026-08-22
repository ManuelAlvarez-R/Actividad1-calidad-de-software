package com.graydoll.calculadora;

/**
 * Fachada de la calculadora. Orquesta operaciones; no imprime ni lee de consola.
 * Se puede inyectar cada Operation (útil para tests avanzados con dobles).
 */
public class Calculator {

    private final Operation addition;
    private final Operation subtraction;
    private final Operation multiplication;
    private final Operation division;

    public Calculator() {
        this(new Addition(), new Subtraction(), new Multiplication(), new Division());
    }

    public Calculator(Operation addition,
                      Operation subtraction,
                      Operation multiplication,
                      Operation division) {
        this.addition = addition;
        this.subtraction = subtraction;
        this.multiplication = multiplication;
        this.division = division;
    }

    public double add(double left, double right) {
        return addition.apply(left, right);
    }

    public double subtract(double left, double right) {
        return subtraction.apply(left, right);
    }

    public double multiply(double left, double right) {
        return multiplication.apply(left, right);
    }

    public double divide(double left, double right) {
        return division.apply(left, right);
    }
}
