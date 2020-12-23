package com.customer.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.customer.domain.Customer;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository repository;
    
    @After 
    public void resetDb() {
        repository.deleteAll();
    }
    
    @Test
    public void whenFindByIdThenReturnCustomer() {
        Customer expected = new Customer("Jenny Willson", "jenny@customers.com", "+52623232");
        entityManager.persistAndFlush(expected);

        Customer actual = repository.findById(1L).get();
        assertEquals(expected, actual);
    }

    @Test
    public void whenInvalidIdThenReturnNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> {
            repository.findById(0L).get();
        });
    }


}
