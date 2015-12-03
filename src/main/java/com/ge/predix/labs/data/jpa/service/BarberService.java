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

import com.ge.predix.labs.data.jpa.domain.Barber;

@Service
@SuppressWarnings("unchecked")
@Transactional
public class BarberService {

    static public final String BARBERS = "barbers";

    @PersistenceContext
    private EntityManager em;

    public Barber createBarber(String name, Date timeStamp) {
    	Barber barber = new Barber();
        barber.setName(name);
        barber.setTStamp(timeStamp);
        em.persist(barber);
        return barber;
    }

    public Collection<Barber> search(String name) {
        String sqlName = ("%" + name + "%").toLowerCase();
        String sql = "select c.* from barber c where (LOWER( c.name ) LIKE :name)";
        return em.createNativeQuery(sql, Barber.class)
                .setParameter("name", sqlName)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Barber> getAllBarbers() {
        return em.createQuery("FROM Barber").getResultList();
    }

    @Cacheable(BARBERS)
    @Transactional(readOnly = true)
    public Barber getBarberById(Integer id) {
        return em.find(Barber.class, id);
    }

    @CacheEvict(BARBERS)
    public void deleteBarber(Integer id) {
    	Barber barber = getBarberById(id);
        em.remove(barber);
    }

    @CachePut(value = BARBERS, key = "#id")
    public Barber updateBarber(Integer id, String name, Date date) {
    	Barber barber = getBarberById(id);
        barber.setTStamp(date);
        barber.setName(name);
        return em.merge(barber);
    }
}
