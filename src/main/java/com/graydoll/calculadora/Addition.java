package com.graydoll.calculadora;

public class Addition implements Operation {

    @Override
    public double apply(double left, double right) {
        return left + right;
    }
}
