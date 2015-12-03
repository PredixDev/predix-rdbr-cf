package com.ge.predix.labs.data.jpa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Visit implements Serializable {
	/**
	 * Barber Shop Visit 
	 */

	private static final long serialVersionUID = 261192499055400094L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @NotNull
    private String customerName;
    
    @NotNull
    private String customerPhone;

	@NotNull
    private String barberName;
    
    @NotNull
    private Date startTimeVisit;
    
    @NotNull
    private Date endTimeVisit;
    
    @NotNull
    private Double hairCutPrice;
    
    public String getCustomerName() {
    	return customerName;
    }
    
    public void setCustomerName(String customerName) {
    	this.customerName = customerName;
    }
    
    public String getCustomerPhone() {
    	return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
    	this.customerPhone = customerPhone;
    }
    
    public String getBarberName() {
    	return barberName;
    }
    
    public void setBarberName(String barberName) {
    	this.barberName = barberName;
    }

	public Date getStartTimeVisit() {
		return startTimeVisit;
	}

	public void setStartTimeVisit(Date startTimeVisit) {
		this.startTimeVisit = startTimeVisit;
	}

	public Date getEndTimeVisit() {
		return endTimeVisit;
	}

	public void setEndTimeVisit(Date endTimeVisit) {
		this.endTimeVisit = endTimeVisit;
	}

    public Double getHairCutPrice() {
		return hairCutPrice;
	}

	public void setHairCutPrice(Double hairCutPrice) {
		this.hairCutPrice = hairCutPrice;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
