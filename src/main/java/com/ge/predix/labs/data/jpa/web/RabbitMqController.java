package com.ge.predix.labs.data.jpa.web;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ge.predix.labs.data.jpa.domain.Barber;
import com.ge.predix.labs.data.jpa.domain.Customer;
import com.ge.predix.labs.data.jpa.service.BarberService;
import com.ge.predix.labs.data.jpa.service.CustomerService;
import com.ge.predix.labs.data.jpa.service.VisitService;

@ComponentScan
@RestController
public class RabbitMqController {

	private static final String QUEUE_NAME = "TestSV";
	
    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired  private BarberService barberService;
	@Autowired  private VisitService visitService;
	@Autowired  private CustomerService customerService;
	
    
	/**
	 * @param echo
	 *            - the string to echo back
	 * @return - round trip messages from a queue 
	 */
	
	@RequestMapping(value = "/testQueue", method = RequestMethod.GET)
	public String test(
			@RequestParam(value = "echo", defaultValue = "echo") String echo) {
		
		String result = "";
		
		String message = (new Date()) + ":Test Message:" + UUID.randomUUID();
		
		
		result += "-> " + message + "<br>";
		rabbitTemplate.convertAndSend(QUEUE_NAME, message);
		
		try {
		
			Thread.sleep(1200);
		} catch (InterruptedException e) {
		}
		
		result += "<- " +  (String) rabbitTemplate.receiveAndConvert(QUEUE_NAME);
		
		return result;
	}
	
	
	/***
	 * 
	 * in Edsger W. Dijkstra memory and his Sleeping Barber problem
	 * 
	 * Barber Shop 
	 * Customer arrives in waiting room aka a queue where barbers serve them. 
	 * When a line of customers is empty the barbers stop to work.
	 * It's standard producer (line of customers)/consumer (barbers) model.
	 * 
	 * @param echo
	 * @return log of events
	 * 
	 */
	
	@SuppressWarnings("nls")
	@RequestMapping("/visitsseries")
	public String barberShop(
			@RequestParam(value = "echo", defaultValue = "echo") String echo) {
		
		// create a barber shop with barbers pool
        Collection<Barber> barbers = barberService.getAllBarbers();
		ExecutorService executor = Executors.newFixedThreadPool(barbers.size());

		// start consumers in thread pool executor design pattern
		String result = new Date().getTime() + ": " + " Barber Shop starts to work" + "<br>";
		
		for (Barber barber:barbers){
			result += new Date().getTime() + ": " + barber.getName() + "start to work" + "<br>";
			executor.submit(new BarberShop(barber.getName(), barber.getHairCutPrice()));
		}

		/**
		 * Producer
		 * 
		 * Create new customers and put then in a queue
		 * 
		 */
        Collection<Customer> customers = customerService.getAllCustomers();
        for (Customer customer:customers){
			result += new Date().getTime() + ": " + customer.getName() + " is comming in a queue" + "<br>";     	
			rabbitTemplate.convertAndSend(QUEUE_NAME, customer);

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
        }
		return result;
	}
	/**
	 * 
	 * @author 212396313
	 * 
	 * Barber class - each barber works in a thread.
	 * Consumer
	 *
	 */

	class BarberShop extends Thread {
		private String name;
		private Double hairCutPrice;

		public BarberShop(String name, Double hairCutPrice) {
			this.name = name;
			this.hairCutPrice = hairCutPrice;
		}

		@Override
		public void run() {
			
			Customer message = (Customer) rabbitTemplate.receiveAndConvert(QUEUE_NAME);
			Date startDate;
			
// waiting when queue has a first message 
			
			while (message == null) {
				message = (Customer) rabbitTemplate.receiveAndConvert(QUEUE_NAME);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
			
			while (true) {
				if (message == null) {
					break;
				} else {
//	 start hair cut				
					startDate = new Date();
					
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
					}
				}
					
//	 end hair cut				
				visitService.createVisit(this.name, message.getName(), message.getPhone(), startDate, new Date(), this.hairCutPrice);
				message = (Customer) rabbitTemplate.receiveAndConvert(QUEUE_NAME);
			}
		}

	}
}