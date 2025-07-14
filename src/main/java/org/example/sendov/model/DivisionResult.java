package org.example.sendov.model;

public class DivisionResult {
    public final Polynomial quotient;
    public final Polynomial remainder;
    public DivisionResult(Polynomial quotient, Polynomial remainder) {
        this.quotient = quotient;
        this.remainder = remainder;
    }

    @Override
    public String toString() {
        return "q = " + quotient + ", " + "r = " + remainder;
    }
}
