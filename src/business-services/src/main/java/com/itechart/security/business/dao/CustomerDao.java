package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.Customer;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface CustomerDao {

    List<Customer> loadAll();

    void update(Customer customer);
}
