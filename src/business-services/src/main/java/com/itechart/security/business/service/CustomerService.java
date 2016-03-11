package com.itechart.security.business.service;

import com.itechart.security.business.filter.CustomerFilter;
import com.itechart.security.business.model.persistent.Customer;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface CustomerService {

    Long saveCustomer(Customer customer);

    List<Customer> getCustomers();

    void updateCustomer(Customer customer);

    void deleteById(Long id);

    List<Customer> findCustomers(CustomerFilter filter);

    int countCustomers(CustomerFilter filter);
}
