package org.example.sendov.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ComplexTest {

    @Test
    void mult() {
        Complex a = new Complex(1, 2);
        Complex b = new Complex(2, 3);
//        assertEquals("", a);
        assertEquals(new Complex(-4, 7), Complex.mult(a, b));
        assertEquals(a, new Complex(a.re() + 1e-12, a.im() + 1e-12));
        Complex.setError(1);
        assertEquals(a, b);
        Complex.setError(1D/2);
//        assertEquals(a, b);
    }
}