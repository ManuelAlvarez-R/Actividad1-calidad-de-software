package com.graydoll.calculadora;

/**
 * Contrato de una operación binaria. El Calculator depende de esta abstracción
 * (Dependency Inversion) y no de una clase concreta.
 */
@FunctionalInterface
public interface Operation {

    double apply(double left, double right);
}
