package com.ge.predix.labs.data.jpa.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ge.predix.labs.data.jpa.domain.Visit;

@Service
@SuppressWarnings("unchecked")
@Transactional
public class VisitService {

    static public final String VISITS = "visits";

    @PersistenceContext
    private EntityManager em;

	public Visit createVisit(String barberName, String customerName,
			String customerPhone, Date startTimeVisit, Date endTimeVisit,
			Double hairCutPrice) {
		Visit barber = new Visit();
		barber.setBarberName(barberName);

		barber.setCustomerName(customerName);
		barber.setCustomerPhone(customerPhone);
		barber.setStartTimeVisit(startTimeVisit);

		barber.setEndTimeVisit(endTimeVisit);
		barber.setHairCutPrice(hairCutPrice);
		em.persist(barber);
		return barber;
	}

    public Collection<Visit> search(String name) {
        String sqlName = ("%" + name + "%").toLowerCase();
        String sql = "select c.* from visit c where (LOWER( c.barbername ) LIKE :name)";
        return em.createNativeQuery(sql, Visit.class)
                .setParameter("name", sqlName)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Visit> getAllVisits() {
        return em.createQuery("FROM Visit").getResultList();
    }

    @Cacheable(VISITS)
    @Transactional(readOnly = true)
    public Visit getVisitById(Integer id) {
        return em.find(Visit.class, id);
    }

	@CachePut(value = VISITS, key = "#id")
	public Visit updateVisit(Integer id, String barberName, String customerName,
			String customerPhone, Date startTimeVisit, Date endTimeVisit,
			Double hairCutPrice) {
		Visit visit = getVisitById(id);
		visit.setBarberName(barberName);
		visit.setCustomerName(customerName);
		visit.setCustomerPhone(customerPhone);
		visit.setStartTimeVisit(startTimeVisit);
		visit.setEndTimeVisit(endTimeVisit);
		visit.setHairCutPrice(hairCutPrice);
		return em.merge(visit);
	}
}
