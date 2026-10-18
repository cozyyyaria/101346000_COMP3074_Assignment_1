package ca.gbc.comp3074.assignment1;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests validating the payroll and tax calculation logic
 * according to COMP3074 Assignment 1 specifications.
 */
public class PayrollCalculationTest {

    private static final double DELTA = 0.001;

    @Test
    public void testUnderFortyHours() {
        double hours = 35.0;
        double rate = 20.0;
        double taxRate = 0.13;

        // Formula: if hours <= 40 -> pay = hours * rate, overtime = 0
        double pay = hours * rate;
        double overtime = 0.0;
        double totalPay = pay + overtime;
        double tax = pay * taxRate;

        assertEquals(700.0, pay, DELTA);
        assertEquals(0.0, overtime, DELTA);
        assertEquals(700.0, totalPay, DELTA);
        assertEquals(91.0, tax, DELTA);
    }

    @Test
    public void testExactlyFortyHours() {
        double hours = 40.0;
        double rate = 25.0;
        double taxRate = 0.15;

        double pay = hours * rate;
        double overtime = 0.0;
        double totalPay = pay + overtime;
        double tax = pay * taxRate;

        assertEquals(1000.0, pay, DELTA);
        assertEquals(0.0, overtime, DELTA);
        assertEquals(1000.0, totalPay, DELTA);
        assertEquals(150.0, tax, DELTA);
    }

    @Test
    public void testOvertimeHours() {
        double hours = 50.0;
        double rate = 30.0;
        double taxRate = 0.20;

        // Formula: if hours > 40:
        // pay = 40 * rate
        // overtime = (hours - 40) * rate * 1.5
        // totalPay = pay + overtime
        // tax = pay * taxRate
        double pay = 40.0 * rate;
        double overtime = (hours - 40.0) * rate * 1.5;
        double totalPay = pay + overtime;
        double tax = pay * taxRate;

        assertEquals(1200.0, pay, DELTA);
        assertEquals(450.0, overtime, DELTA);
        assertEquals(1650.0, totalPay, DELTA);
        assertEquals(240.0, tax, DELTA);
    }
}

