package org.example.sendov.model;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    @Test
    void testStringToPoly() {
        Polynomial p1 = new Polynomial("-1+ 2x + 3x^4 - 2x");
        System.out.println(p1);
        Polynomial p2 = new Polynomial("-1-x+x+1");
        System.out.println(p2);
        Polynomial p3 = new Polynomial("0");
        System.out.println(p3);
        Polynomial p4 = new Polynomial("x^3 - 1");
        System.out.println(p4);
        Polynomial p5 = new Polynomial("-x");
        System.out.println(p5);
        Polynomial p6 = new Polynomial("-x+x");
        System.out.println(p6);
    }

    @Test
    void testTruncate() {
        Polynomial p1 = new Polynomial("-1+ 2x + 3x^4 - 2x");
        assertEquals(4, p1.degree());
        Polynomial p2 = new Polynomial("-1-x+x+1");
        assertEquals(-1, p2.degree());
        Polynomial p3 = new Polynomial("0");
        assertEquals(-1, p3.degree());
        Polynomial p4 = new Polynomial("x^3 - 1");
        assertEquals(3, p4.degree());
        Polynomial p5 = new Polynomial("-x");
        assertEquals(1, p5.degree());
        Polynomial p6 = new Polynomial("-x+x");
        assertEquals(-1, p6.degree());
        Polynomial p7 = new Polynomial("1 + x - x^3 + x^3");
        System.out.println("p7 in truncate: " + p7);
        assertEquals(1, p7.degree());
        Polynomial p8 = new Polynomial("0.0000000000000000000000001");
        assertTrue(p8.isZero());
    }

    @Test
    public void testAdd() {
        Polynomial p1 = new Polynomial("1 + x + x^2");
        Polynomial p2 = new Polynomial("-1-x-x^2");
        Polynomial p3 = Polynomial.add(p1, p2);
        assertTrue(p3.isZero());
        Polynomial p4 = new Polynomial("1 + x - x^2");
        System.out.println(Polynomial.add(p1, p4));
    }

    @Test
    public void testScale() {
        Polynomial p1 = new Polynomial("1 + x + x^2");
        Complex im = new Complex(0, 1);
        p1.scale(im);
        System.out.println(p1);
        Polynomial p2 = new Polynomial("1 + x");
        p2.scale(Complex.ZERO);
        assertTrue(p2.isZero());
    }

    @Test
    public void testSubt() {
        Polynomial p1 = new Polynomial("1 + x");
        Polynomial p2 = p1.copy();
        assertTrue(Polynomial.subt(p1, p2).isZero());
        Polynomial p3 = new Polynomial("1 + 2x");
        System.out.println(Polynomial.subt(p1, p3));
    }

    @Test
    public void testEquals() {
        Polynomial p1 = new Polynomial("0.0000000000000000000000000000000000001x");
        Polynomial p2 = new Polynomial("0");
        assertEquals(p1, p2);
        Polynomial p3 = new Polynomial("1 + x");
        Polynomial p4 = new Polynomial("-x^2 + 1 + 2x - x + x^2");
        assertEquals(p3, p4);
    }

    @Test
    public void testShift() {
        Polynomial p1 = new Polynomial("x^2 + x^3");
        p1.shift(-2);
        assertEquals(new Polynomial("1 + x"), p1);
        Polynomial p2 = new Polynomial("1 + x");
        p2.shift(0);
        assertEquals(new Polynomial("1 + x"), p2);
        Polynomial p3 = new Polynomial("1");
        p3.shift(3);
        assertEquals(new Polynomial("x^3"), p3);
        assertNotEquals(new Polynomial("x^2"), p3);
        Polynomial p4 = new Polynomial("x");
        assertFalse(p4.shift(-2));
        assertEquals(new Polynomial("x"), p4);
        p4.shift(-1);
        assertTrue(p4.shift(0));
        assertEquals(new Polynomial("1"), p4);
        Polynomial p5 = new Polynomial("0");
        assertFalse(p5.shift(-1));
    }

    @Test
    public void testCopy() {
        Polynomial p1 = new Polynomial("1 + x");
        Polynomial p2 = p1.copy();
        p2.shift(1);
        assertEquals(new Polynomial("1 + x"), p1);
    }

    @Test
    public void testMult() {
        Polynomial p1 = new Polynomial("1");
        Polynomial p2 = new Polynomial("0");
        assertEquals(new Polynomial("0"), Polynomial.mult(p1, p2));
        Polynomial p3 = new Polynomial("x + 1");
        assertEquals(new Polynomial("x^2 + 1 + 2x"), Polynomial.mult(p3, p3));
        Polynomial p4 = new Polynomial("x^2 - x + 1");
        Polynomial p5 = Polynomial.mult(p3, p4);
        assertEquals(new Polynomial("x^3 + 1"), p5);
        System.out.println(p5);
    }

    @Test
    public void testExp() {
        Polynomial p1 = new Polynomial("x + 1");
        p1.exp(0);
        assertEquals(new Polynomial("1"), p1);
        Polynomial p2 = new Polynomial("0");
        p2.exp(0);
        assertEquals(new Polynomial("1"), p2);
        Polynomial p3 = new Polynomial("x - 1");
        p3.exp(4);
        assertEquals(new Polynomial("x^4 - 4x^3 + 6x^2 - 4x + 1"), p3);
    }

    @Test
    public void testDivide() {
        Polynomial p1 = new Polynomial("x");
        Polynomial p2 = new Polynomial("0");
//        assertThrows(ZeroDivisionException.class, () -> Polynomial.divide(p1, p2), "expected ZeroDivisionException, but didn't");
        assertEquals(new Polynomial("1"), Polynomial.divide(p1, p1).quotient);
        Polynomial p3 = new Polynomial("x + 1");
        p3.exp(5);
        Polynomial p4 = new Polynomial("x + 1");
        p4.exp(3);
        System.out.println(Polynomial.divide(p3, p4));
        Polynomial p5 = new Polynomial("1");
        DivisionResult res1 = Polynomial.divide(p1, p5);
        assertEquals(new Polynomial("x"), res1.quotient);
        assertEquals(new Polynomial("0"), res1.remainder);
        DivisionResult res2 = Polynomial.divide(p5, p1);
        assertEquals(new Polynomial("0"), res2.quotient);
        assertEquals(new Polynomial("1"), res2.remainder);
        List<Complex> coeffs7 = new ArrayList<>();
        coeffs7.add(new Complex(0, 1));
        coeffs7.add(new Complex(1, 0));
        Polynomial p6 = new Polynomial("x^2 + 1");
        Polynomial p7 = new Polynomial(coeffs7);
        System.out.println(Polynomial.divide(p6, p7));
        Polynomial p8 = new Polynomial("x^3");
        Polynomial p9 = new Polynomial("x");
        assertEquals(new Polynomial("x^2"), Polynomial.divide(p8, p9).quotient);
    }

    @Test
    public void testEval() {
        Polynomial p1 = new Polynomial("x^4 - 1");
        assertEquals(Complex.ZERO, p1.eval(new Complex(0, 1)));
        assertEquals(Complex.ZERO, p1.eval(new Complex(0, -1)));
        assertEquals(Complex.ZERO, p1.eval(new Complex(1, 0)));
        assertEquals(Complex.ZERO, p1.eval(new Complex(-1, 0)));
        assertNotEquals(Complex.ZERO, p1.eval(new Complex(2, 0)));

        Polynomial p2 = new Polynomial("0");
        assertEquals(Complex.ZERO, p2.eval(new Complex(2, 0)));
    }
}