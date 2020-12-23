package com.customer.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.customer.domain.Customer;

public class CustomerValidationTest {
    
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void shouldHaveNoViolations() throws Exception {
        Customer customer = new Customer(1L, "Jim Beam", "jim@customers.com", "+323232");
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldDetectInvalidFullName() throws Exception {
        Customer customer = new Customer(1L, "b", "jim@customers.com", "+323232");
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertEquals(violations.size(), 1);
        ConstraintViolation<Customer> violation = violations.iterator().next();
        assertEquals("2..50 chars including whitespaces", violation.getMessage());
        assertEquals("fullName", violation.getPropertyPath().toString());
        assertEquals("b", violation.getInvalidValue());
    }
    
    @Test
    public void shouldDetectInvalidEmail() throws Exception {
        Customer customer = new Customer(1L, "Robert Bobson", "robert@custo@mers.com", "+4233232");
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertEquals(violations.size(), 1);
        ConstraintViolation<Customer> violation = violations.iterator().next();
        assertEquals("should include exactly one @", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("robert@custo@mers.com", violation.getInvalidValue());
    }
    
    @Test
    public void shouldDetectInvalidPhone() throws Exception {
        Customer customer = new Customer(1L, "William Shakespeare", "william@customers.com", "3423232");
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertEquals(violations.size(), 1);
        ConstraintViolation<Customer> violation = violations.iterator().next();
        assertEquals("only digits, should start from +", violation.getMessage());
        assertEquals("phone", violation.getPropertyPath().toString());
        assertEquals("3423232", violation.getInvalidValue());
    }


}
