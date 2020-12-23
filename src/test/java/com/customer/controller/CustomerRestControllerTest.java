package com.customer.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.customer.domain.Customer;
import com.customer.repository.CustomerRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase=ExecutionPhase.BEFORE_TEST_METHOD,scripts="classpath:schema.sql")
public class CustomerRestControllerTest {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    CustomerRepository repository;
    
    @After 
    public void resetDb() {
        repository.deleteAll();
    }
    
    @Test
    public void whenValidInputThenCreateCustomer() throws IOException, Exception {
        Customer customer = new Customer(1L, "Jennifer Watson", "jennifer@customers.com", "+32323232");
        mvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(toJson(customer)));

        List<Customer> found = repository.findAll();
        assertThat(found).extracting(Customer::getId).containsOnly(1L);
    }
    
    @Test
    public void givenCustomersWhenGetCustomersThenStatus200() throws Exception {
        Customer customer1 = new Customer(1L, "Megan Smith", "negan@customers.com", "+32323232");
        repository.saveAndFlush(customer1);
        Customer customer2 = new Customer(2L, "Jake Jyllenhaal", "jake@customers.com", "+6565665");
        repository.saveAndFlush(customer2);

        mvc.perform(get("/customers").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2)))).andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].fullName", is("Megan Smith"))).andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].fullName", is("Jake Jyllenhaal")));
    }

    
    private byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

}
