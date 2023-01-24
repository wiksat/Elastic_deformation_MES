package org.example;

import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.linear.*;

import java.util.Arrays;


public class Elastic_Deformation_FEM {
    public double[] x_es;
    public double[] y_es;
    public static double domain;
    public Elastic_Deformation_FEM(double domain) {
        Elastic_Deformation_FEM.domain = domain;
    }
    public void compute(int n){
        RealMatrix B_uv = new Array2DRowRealMatrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                B_uv.setEntry(i, j, B(i, j, 0.0, n));
            }
        }

        RealVector L_v = new ArrayRealVector(n);
        for (int i = 0; i < n; i++) {
            L_v.setEntry(i, L(i, 0.0, n));
        }
        RealVector w = new QRDecomposition(B_uv).getSolver().solve(L_v);
        double[] wArray = w.toArray();

        y_es = new double[n+1];
        x_es = new double[n+1];
        double x = 0.0;
        int y = 0;
        while (x <= domain) {
            for (int i = 0; i < n; i++) {
                y_es[y] += e_i(i, x, n) * wArray[i];
            }
            x_es[y] = x;
            y++;
            x +=domain/n;
//            System.out.println(x);
        }
        for (int i = 0; i < n; i++) {
            y_es[n] += e_i(i, 2, n) * wArray[i];
        }
        x_es[n] = 2;
//        System.out.println(Arrays.toString(x_es));
//        System.out.println(Arrays.toString(y_es));
    }

    public double getIntegral(int i, int j, int n) {
        return new IterativeLegendreGaussIntegrator(
                30,
                1e-6,
                1e-6).integrate(
                Integer.MAX_VALUE,
                x -> E_value(x) * e_i_derivative(i, x, n) * e_i_derivative(j, x, n),
                0,
                2);
    }
    private double B(int i, int j, double x, int n) {
        if (Math.abs(j - i) <= 1) {
            return getIntegral(i, j, n) - E_value(x) * e_i(i, x, n) * e_i(j, x, n);
        }
        return - E_value(x) * e_i(i, x, n) * e_i(j, x, n);
    }
    private double L(int i, double x, int n) {
        return -10 * E_value(x) * e_i(i, x, n);
    }
    private double e_i(int i, double x, int n) {
        double l = domain / n;
        double middle = l * i;
        double left = l * (i - 1);
        double right = l * (i + 1);
        if (x < left|| x > right) {
            return 0.0;
        }
        if (x >= middle) {
            return new LinearFunction(1, 0, middle, right).getValue(x);
        } else {
            return new LinearFunction(0, 1, left, middle).getValue(x);
        }
    }

    private double e_i_derivative(int i, double x, int n) {
        double l = domain / n;
        double middle = l * i;
        double left = l * (i - 1);
        double right = l * (i + 1);
        if (x < left || x > right) {
            return 0.0;
        }
        if (x >= middle) {
            return new LinearFunction(1, 0, middle, right).getDerivative();
        } else {
            return new LinearFunction(0, 1, left, middle).getDerivative();
        }
    }
    private double E_value(double x) {
        if (x >= 0 && x <= 1.0) {
            return 2.0;
        } else if (x > 1 && x <= 2) {
            return 6.0;
        } else throw new IllegalArgumentException("X value out of domain");
    }
}
