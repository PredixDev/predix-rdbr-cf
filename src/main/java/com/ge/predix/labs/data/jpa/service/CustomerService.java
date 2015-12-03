package com.ge.predix.labs.data.jpa.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ge.predix.labs.data.jpa.domain.Customer;

@Service
@SuppressWarnings("unchecked")
@Transactional
public class CustomerService {

    static public final String CUSTOMERS = "customers";

    @PersistenceContext
    private EntityManager em;

    public Customer createCustomer(String name, String phone, Date timeStamp) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setTStamp(timeStamp);
        em.persist(customer);
        return customer;
    }

    public Collection<Customer> search(String name) {
        String sqlName = ("%" + name + "%").toLowerCase();
        String sql = "select c.* from customer c where (LOWER( c.name ) LIKE :fn)";
        return em.createNativeQuery(sql, Customer.class)
                .setParameter("fn", sqlName)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return em.createQuery("FROM Customer").getResultList();
    }

    @Cacheable(CUSTOMERS)
    @Transactional(readOnly = true)
    public Customer getCustomerById(Integer id) {
        return em.find(Customer.class, id);
    }

    @CacheEvict(CUSTOMERS)
    public void deleteCustomer(Integer id) {
        Customer customer = getCustomerById(id);
        em.remove(customer);
    }

    @CachePut(value = CUSTOMERS, key = "#id")
    public Customer updateCustomer(Integer id, String name, String phone, Date date) {
        Customer customer = getCustomerById(id);
        customer.setTStamp(date);
        customer.setName(name);
        customer.setPhone(phone);
        return em.merge(customer);
    }
}
