package com.graydoll.calculadora;

public class Division implements Operation {

    @Override
    public double apply(double left, double right) {
        if (right == 0.0) {
            throw new DivisionByZeroException("No se puede dividir por cero");
        }
        return left / right;
    }
}
