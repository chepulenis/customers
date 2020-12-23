package com.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.customer.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public static final String SQL_CREATE_CUSTOMER = "insert into customers (created, full_name, email, phone) values "
            + "(extract(epoch from now()), :#{#customer.fullName}, :#{#customer.email}, :#{#customer.phone})";

    public static final String SQL_UPDATE_CUSTOMER = "update customers set updated = (extract(epoch from now())), full_name = :#{#customer.fullName},"
            + " phone = :#{#customer.phone} where id = :#{#id}";

    public static final String SQL_DELETE_CUSTOMER = "update customers set is_deleted = true where id = :#{#id}";

    public static final String SQL_FIND_ALL_CUSTOMERS = "select * from customers where is_deleted = false";
    
    public static final String SQL_FIND_CUSTOMER_BY_ID = "select * from customers where is_deleted = false and id = :#{#id}";

    @Transactional
    @Modifying
    @Query(value = SQL_CREATE_CUSTOMER, nativeQuery = true)
    public void createCustomer(@Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query(value = SQL_UPDATE_CUSTOMER, nativeQuery = true)
    public void updateCustomer(@Param("customer") Customer customer, @Param("id") long id);

    @Transactional
    @Modifying
    @Query(value = SQL_DELETE_CUSTOMER, nativeQuery = true)
    public void deleteCustomer(@Param("id") long id);

    @Query(value = SQL_FIND_ALL_CUSTOMERS, nativeQuery = true)
    public List<Customer> findAllCustomers();
    
    @Query(value = SQL_FIND_CUSTOMER_BY_ID, nativeQuery = true)
    public Customer findCustomerById(@Param("id") long id);

}
