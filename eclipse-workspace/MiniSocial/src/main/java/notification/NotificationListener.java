package notification;

import models.Notifications;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.StringReader;

import com.fasterxml.jackson.databind.ObjectMapper;




@MessageDriven(
		activationConfig = {@ ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/NotificationQueue" ),
				@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
				
		}
		
		)
public class NotificationListener implements MessageListener {
	
	@PersistenceContext(unitName = "hello")
	private EntityManager em;
	
	@Override
	public void onMessage(Message message) {
		try {
			if(message instanceof TextMessage) {
				
	            String body = ((TextMessage) message).getText();

				ObjectMapper mapper = new ObjectMapper();
				
				EventNotification event = mapper.readValue(body, EventNotification.class);
				
				Notifications notification = new Notifications(event.getUserId(),event.getEventType(),event.getMessage());
				
								
				em.persist(notification);
				System.out.println("Notification saved :) --> "+event.getMessage());
                
			}
		}
		catch (Exception e) {
			System.out.println("Exception in onMessage(): " + e.getMessage());
		}
	}
	

}
