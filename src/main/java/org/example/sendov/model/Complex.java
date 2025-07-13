package org.example.sendov.model;

/**
 * immutable
 */
public class Complex {
    private final double re;
    private final double im;
    private static int DECIMAL_MULT = 100;
    private static double ERROR = 1e-10;

    public final static Complex ZERO = new Complex(0, 0);

    private static void checkValidArgument(double arg, String name) {
        if (arg == Double.POSITIVE_INFINITY || arg == Double.NEGATIVE_INFINITY) {
            throw new InfiniteArgumentException(name + " cannot be infinite");
        }
    }

    public Complex(double re, double im) {
        checkValidArgument(re, "re");
        checkValidArgument(im, "im");
        this.re = re;
        this.im = im;
    }

    public double re() {return re;}

    public double im() {return im;}
    public static Complex mult(Complex a, Complex b) {
        double nre = a.re * b.re - a.im * b.im;
        double nim = a.re * b.im + a.im * b.re;
        checkValidArgument(nre, "re of product");
        checkValidArgument(nim, "im of product");
        return new Complex(nre, nim);
    }

    public static Complex divide(Complex a, Complex b) {
        return mult(a, b.recip());
    }

    public static Complex add(Complex a, Complex b) {
        double nre = a.re + b.re;
        double nim = a.im + b.im;
        checkValidArgument(nre, "re of sum");
        checkValidArgument(nim, "im of sum");
        return new Complex(nre, nim);
    }

    public static Complex subt(Complex a, Complex b) {
        double nre = a.re - b.re;
        double nim = a.im - b.im;
        checkValidArgument(nre, "re of diff");
        checkValidArgument(nim, "im of diff");
        return new Complex(nre, nim);
    }
    public double size() {
        return Math.sqrt(sizeSquared());
    }

    public double sizeSquared() {
        double rtn = re * re + im * im;
        checkValidArgument(rtn, "size squared");
        return rtn;
    }

    public Complex conj() {
        return new Complex(re, -im);
    }

    public Complex scale(double factor) {
        double nre = re * factor;
        double nim = im * factor;
        checkValidArgument(nre, "re of scaled");
        checkValidArgument(nim, "im of scaled");
        return new Complex(nre, nim);
    }

    public Complex recip() {
        if (re == 0 && im == 0) {
            throw new ZeroDivisionException("Cannot reciprocate 0");
        }
        return conj().scale(1 / sizeSquared());
    }

    public static boolean setDisplayDecimals(int decimals) {
        if (decimals < 0) return false;
        DECIMAL_MULT = 1;
        for (int i = 0; i < decimals; i++) DECIMAL_MULT *= 10;
        return true;
    }

    private double round(double toRound) {
        return (double)Math.round(DECIMAL_MULT * toRound) / DECIMAL_MULT;
    }

    @Override
    public String toString() {
        if (isReal()) return round(re) + "";
        else if (im > 0) return round(re) + " + i * " + round(im);
        else return round(re) + " - i * " + round(-im);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Complex)) throw new IllegalArgumentException("other should be a complex number");
        Complex diff = Complex.subt(this, (Complex)other);
        return Math.abs(diff.re()) <= ERROR && Math.abs(diff.im()) <= ERROR;
    }

    public static boolean setError(double error) {
        if (error < 0) return false;
        ERROR = error;
        return true;
    }

    public boolean isReal() {
        return Math.abs(im) <= ERROR;
    }

    public boolean isImag() {
        return Math.abs(re) <= ERROR;
    }

    public boolean isZero() {
        return isReal() && isImag();
    }
}
