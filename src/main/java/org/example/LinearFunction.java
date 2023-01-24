package org.example;

public class LinearFunction {
    private final double a;
    private final double b;
    LinearFunction (double fx1, double fx2, double x1, double x2){
        this.a = (fx1-fx2)/(x1-x2);
        this.b = fx1 - a*x1;
    }
    public double getDerivative() {
        return this.a;
    }

    public double getValue(double x) {
        return this.a*x + this.b;
    }
}
