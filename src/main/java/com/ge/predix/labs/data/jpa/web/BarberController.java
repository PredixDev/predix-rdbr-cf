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

import com.ge.predix.labs.data.jpa.domain.Barber;
import com.ge.predix.labs.data.jpa.service.BarberService;

@ComponentScan
@RestController
public class BarberController {

	@Autowired  private BarberService barberService;

    public static final String BARBERS = "/barbers";
    public static final String SEARCH_BARBERS = "/barbersearch";
    public static final String GET_BARBERS_BY_ID = BARBERS + "/{id}";

    @RequestMapping(value = SEARCH_BARBERS, method = RequestMethod.GET)
    public Collection<Barber> search(@RequestParam("q") String query) throws Exception {
        Collection<Barber> barbers = barberService.search(query);
        return barbers;
    }

    @RequestMapping(value = GET_BARBERS_BY_ID, method = RequestMethod.GET)
    public Barber customerById(@PathVariable  Integer id) {
        return this.barberService.getBarberById(id);
    }

    @RequestMapping(value = BARBERS, method = RequestMethod.GET)
    public Collection<Barber> barbers() throws Exception {
        Collection<Barber> barbers = barberService.getAllBarbers();
        return barbers;
    }

    @RequestMapping(value = BARBERS, method = RequestMethod.POST)
    public Integer addBarber(@RequestParam String name, @RequestParam String phone) {
        return barberService.createBarber(name, new Date()).getId();
    }

    @RequestMapping(value = GET_BARBERS_BY_ID, method = RequestMethod.PUT)
    public Integer updateBarber(@PathVariable  Integer id, @RequestBody Barber barber) {
        barberService.updateBarber(id, barber.getName(), barber.getTStamp());
        return id;
    }
}