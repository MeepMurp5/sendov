package org.example.sendov.model;

import java.util.ArrayList;
import java.util.List;

/**
 * mutable (for the sake of less copying)
 * unary operations will modify this (if necessary in the context of the operation), binary operations are static
 * (and creates new instance of a polynomial)
 */
public class Polynomial {
    private int degree;
    private List<Complex> coeffs;
    public Polynomial(String poly) {
        Polynomial res = stringToPoly(poly);
        this.degree = res.degree;
        this.coeffs = res.coeffs;
    }

    public Polynomial(List<Complex> coeffs) {
        this.coeffs = coeffs;
        truncate();
    }

    public Polynomial(Complex leadCoeff, int degree) {
        coeffs = new ArrayList<>(degree + 1);
        for (int i = 0; i < degree; i++) coeffs.add(Complex.ZERO);
        coeffs.add(leadCoeff);
        truncate();
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static double[] stringToTerm(String term) {
        double[] rtn = new double[2];
        if (!term.contains("x")) {
            rtn[0] = Double.parseDouble(term);
            return rtn;
        }
        int idx = term.indexOf("x");
        rtn[0] = Double.parseDouble(term.substring(0, idx));
        rtn[1] = Integer.parseInt(term.substring(idx + 2));
        return rtn;
    }

    public static Polynomial stringToPoly(String poly) {
        StringBuilder sb = new StringBuilder();
        if (poly.charAt(0) != '-') sb.append('+');
        for (char c : poly.toCharArray()) {
            if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == 'x' && c != '^') {
                sb.append('^');
                sb.append('1');
            }
            if (c == '-') sb.append('+');
            if (c == ' ') continue;
            if (c == 'x' && !isDigit(sb.charAt(sb.length() - 1))) sb.append('1');
            sb.append(c);
        }
        if (sb.charAt(sb.length() - 1) == 'x') {
            sb.append('^');
            sb.append('1');
        }
        String temp = sb.toString();
        String[] terms = temp.split("\\+");
//        System.out.println("temp: " + temp);
//        for (String term : terms) System.out.println("term: " + term);
        List<Complex> coeffs = new ArrayList<>();
        for (String term : terms) {
            if (term.equals("")) continue;
            double[] pair = stringToTerm(term);
            Complex coeff = new Complex(pair[0], 0);
            int degree = (int)pair[1];
            while (coeffs.size() < degree + 1) coeffs.add(Complex.ZERO);
            coeffs.set(degree, Complex.add(coeffs.get(degree), coeff));
        }
        return new Polynomial(coeffs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = coeffs.size() - 1; i >= 0; i--) {
            Complex curr = coeffs.get(i);
            if (curr.isReal()) {
                sb.append(" + " + curr.re() + "x^" + i);
            } else {
                sb.append(" + (" + curr + ")x^" + i);
            }

        }
        return sb.toString();
    }

    private void truncate() {
        while (coeffs.size() > 1 && coeffs.getLast().equals(Complex.ZERO)) coeffs.removeLast();
        if (coeffs.size() == 1 && coeffs.getLast().equals(Complex.ZERO)) degree = -1;
        else degree = coeffs.size() - 1;
    }

    public int degree() {return degree;}

    public boolean isZero() {return degree == -1;}

    public static Polynomial add(Polynomial a, Polynomial b) {
        int iters = Math.max(a.degree(), b.degree()) + 1;
        List<Complex> ncoeffs = new ArrayList<>(iters);
        for (int i = 0; i < iters; i++) ncoeffs.add(null);
        for (int i = 0; i < iters; i++) {
            ncoeffs.set(i, Complex.add(
                    (i <= a.degree()) ? a.coeffs.get(i) : Complex.ZERO,
                    (i <= b.degree()) ? b.coeffs.get(i) : Complex.ZERO
            ));
        }
        return new Polynomial(ncoeffs);
    }

    public void scale(Complex factor) {
        coeffs.replaceAll(a -> Complex.mult(a, factor));
        truncate();
    }

    public Polynomial copy() {
        return new Polynomial(new ArrayList<>(coeffs));
    }

    public static Polynomial subt(Polynomial a, Polynomial b) {
        Polynomial temp = b.copy();
        temp.scale(new Complex(-1, 0));
        return Polynomial.add(a, temp);
    }

    @Override public boolean equals(Object other) {
        if (!(other instanceof Polynomial)) throw new IllegalArgumentException("polynomials cannot be compared to non-polynomials");
        return Polynomial.subt(this, (Polynomial)other).isZero();
    }

    /*
    Although 0 can be shifted left math-wise to get another polynomial, I will treat 0 as any
    nonzero constant, which can't be shifted left
     */
    public boolean shift(int shiftBy) {
        if (shiftBy >= 0) {
            for (int i = 0; i < shiftBy; i++) {
                coeffs.addFirst(Complex.ZERO);
            }
        } else {
            int count = 0;
            shiftBy *= -1;
            while (coeffs.size() >= 2 && count < shiftBy && coeffs.get(count).isZero()) count++;
            if (count == shiftBy) {
                coeffs = coeffs.subList(shiftBy, coeffs.size());
            } else {
                return false;
            }
        }
        truncate();
        return true;
    }

    public static Polynomial mult(Polynomial a, Polynomial b) {
        Polynomial rtn = new Polynomial("0");
        for (int i = 0; i <= b.degree; i++) {
            Polynomial temp = a.copy();
            temp.scale(b.coeffs.get(i));
            temp.shift(i);
            rtn = Polynomial.add(rtn, temp);
        }
        return rtn;
    }

    /**
     * I will take 0^0 = 1
     * @param exponent
     */
    public void exp(int exponent) {
        if (exponent < 0) throw new IllegalArgumentException("exponent cannot be negative");
        List<Integer> bits = new ArrayList<>();
        while (exponent > 0) {
            bits.add(exponent & 1);
            exponent >>= 1;
        }
        bits = bits.reversed();
        Polynomial base = this;
        Polynomial ans = new Polynomial("1");
        for (int bit : bits) {
            ans = Polynomial.mult(ans, ans);
            if (bit == 1) ans = Polynomial.mult(ans, base);
        }
        coeffs = ans.coeffs;
        degree = ans.degree;
    }

    public static DivisionResult divide(Polynomial a, Polynomial b) {
        if (b.isZero()) throw new ZeroDivisionException("cannot divide by the zero polynomial");
        Polynomial curr = a.copy();

        // can be better
        Polynomial quotient = new Polynomial("0");

        int count = 0;
        while (curr.degree() >= b.degree() && count++ <= 10) {
            Polynomial temp = b.copy();
            temp.shift(curr.degree() - b.degree());
            Complex elim = Complex.divide(curr.coeffs.getLast(), b.coeffs.getLast());
            temp.scale(elim);
            quotient = Polynomial.add(quotient, new Polynomial(elim, curr.degree() - b.degree()));
            curr = Polynomial.subt(curr, temp);
        }
        return new DivisionResult(quotient, curr);
    }

    public Complex eval(Complex x) {
        Complex rtn = Complex.ZERO;
        Complex power = new Complex(1, 0);
        for (int i = 0; i <= degree(); i++) {
            rtn = Complex.add(rtn, Complex.mult(power, coeffs.get(i)));
            power = Complex.mult(power, x);
        }
        return rtn;
    }
}