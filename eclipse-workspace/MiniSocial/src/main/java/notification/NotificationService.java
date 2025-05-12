package notification;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import models.Notifications;

@RolesAllowed({"AMDIN", "USER"})
@Stateless
public class NotificationService {
	
	  @PersistenceContext(unitName = "hello")
	    private EntityManager em;
	  
	  public List<Notifications> getUserNotification(int userId) {
		  try {
	            List<Notifications> notifications = em.createQuery("select n from Notifications n where n.userId = :id",Notifications.class).setParameter("id", userId)
	                    .getResultList();

	            if (notifications.isEmpty()|| notifications == null) {
	               
	            	return null;
	            }
	            
	            else {return notifications;}
	            
	            
		  }catch (Exception e) {
			 
			  System.out.println("Exception in createGroup() as" + e.getMessage());
			  return null;
		  }
		}

}
