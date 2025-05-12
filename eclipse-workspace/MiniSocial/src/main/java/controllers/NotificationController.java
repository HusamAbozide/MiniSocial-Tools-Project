package controllers;

import models.Notifications;
import notification.NotificationService;

import javax.inject.Inject;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationController{
	
	@PersistenceContext(unitName= "hello")
	private EntityManager em;
	
	@Inject
	NotificationService notificationService;
	
	@GET
	@Path("/{userId}")
	public Response getNotifications(@PathParam("userId") int userId) {
		
		List<Notifications> exists = notificationService.getUserNotification(userId);
		
		if(exists == null) {
			System.out.println("No notifications found, you are all caught up!");
            return Response.status(Response.Status.NO_CONTENT).entity("No notifications found, you are all caught up!").build();
		}
		else {
            return Response.ok(exists).build();
		}
	}
}
