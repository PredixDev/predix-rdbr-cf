package com.ge.predix.labs.data.jpa.web;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ge.predix.labs.data.jpa.domain.Customer;
import com.ge.predix.labs.data.jpa.service.CustomerService;

@ComponentScan
@RestController
public class CustomerApiController {

	@Autowired  private CustomerService customerService;

    public static final String CUSTOMERS = "/customers";
    public static final String SEARCH_CUSTOMERS = "/search";
    public static final String GET_CUSTOMERS_BY_ID = CUSTOMERS + "/{id}";

    @RequestMapping(value = SEARCH_CUSTOMERS, method = RequestMethod.GET)
    public Collection<Customer> search(@RequestParam("q") String query) throws Exception {
        Collection<Customer> customers = customerService.search(query);
        return customers;
    }

    @RequestMapping(value = GET_CUSTOMERS_BY_ID, method = RequestMethod.GET)
    public Customer customerById(@PathVariable  Integer id) {
        return this.customerService.getCustomerById(id);
    }

    @RequestMapping(value = CUSTOMERS, method = RequestMethod.GET)
    public Collection<Customer> customers() throws Exception {
        Collection<Customer> customers = customerService.getAllCustomers();
        return customers;
    }

    @RequestMapping(value = CUSTOMERS, method = RequestMethod.POST)
    public Integer addCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer.getName(), customer.getPhone(),  new Date()).getId();
    }

    @RequestMapping(value = GET_CUSTOMERS_BY_ID, method = RequestMethod.PUT)
    public Integer updateCustomer(@PathVariable  Integer id, @RequestBody Customer customer) {
        customerService.updateCustomer(id, customer.getName(), customer.getPhone(), customer.getTStamp());
        return id;
    }
    
    @RequestMapping(value = GET_CUSTOMERS_BY_ID, method = RequestMethod.DELETE)
    public void deleteCustomer(@PathVariable  Integer id) {
    	customerService.deleteCustomer(id);
    }
}