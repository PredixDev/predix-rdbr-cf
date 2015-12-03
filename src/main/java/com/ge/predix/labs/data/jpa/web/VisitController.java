package com.ge.predix.labs.data.jpa.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ge.predix.labs.data.jpa.domain.Visit;
import com.ge.predix.labs.data.jpa.service.VisitService;

@ComponentScan
@RestController
public class VisitController {

	@Autowired  private VisitService visitService;
	@Autowired  private CacheManager cm;
	@Autowired  private RedisTemplate<String, ?> redisTemplate;

    public static final String VISITS = "/visits";
    public static final String SEARCH_VISITS = "/visitsearch";
    public static final String GET_VISIT_BY_ID = VISITS + "/{id}";

    @RequestMapping(value = SEARCH_VISITS, method = RequestMethod.GET)
    public Collection<Visit> search(@RequestParam("q") String query) throws Exception {
        Collection<Visit> visits = visitService.search(query);
        return visits;
    }

    @RequestMapping(value = GET_VISIT_BY_ID, method = RequestMethod.GET)
    public Visit visitById(@PathVariable  Integer id) {
        return this.visitService.getVisitById(id);
    }

    @RequestMapping(value = VISITS, method = RequestMethod.GET)
    public Collection<Visit> visits() throws Exception {
        Collection<Visit> visits = visitService.getAllVisits();
        return visits;
    }
    
	@RequestMapping(value = VISITS, method = RequestMethod.POST)
	public Integer addBarber(@RequestBody Visit visit) {
		return visitService.createVisit(visit.getBarberName(),
				visit.getCustomerName(), visit.getCustomerPhone(),
				visit.getStartTimeVisit(), visit.getEndTimeVisit(),
				visit.getHairCutPrice()).getId();
	}

	@RequestMapping(value = GET_VISIT_BY_ID, method = RequestMethod.PUT)
	public Integer updateBarber(@PathVariable Integer id,
			@RequestBody Visit visit) {
		visitService.updateVisit(id, visit.getBarberName(),
				visit.getCustomerName(), visit.getCustomerPhone(),
				visit.getStartTimeVisit(), visit.getEndTimeVisit(),
				visit.getHairCutPrice());
		return id;
	}
}