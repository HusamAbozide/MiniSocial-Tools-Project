package controllers;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.FriendRequest;
import models.User;
import services.ConnectionsService;
import services.ConnectionsServiceImpl;
import services.UserService;
import services.UserServiceImpl;

@Stateless
@Path("/connections")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class ConnectionController {
	
	@Inject
	private ConnectionsService con_service;
	//private ConnectionsService con_service = new ConnectionsServiceImpl();

	
	@POST
	@Path("/send")
	public Response sendFriendRequest(@QueryParam("senderId") int senderId, @QueryParam("recieverId") int recieverId) {
		
		boolean request = con_service.sendFriendRequest(senderId, recieverId);
		if(request) {
			return Response.ok("Friend request sent successfully :)").build();
		}
		else {
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to send Friend Request").build();
	    }
	}
	
	@POST
	@Path("/accept")
	public Response acceptFriendRequest(@QueryParam("requestId") int requestId) {
		
		boolean accepted = con_service.acceptFriendRequest(requestId);
		
		if(accepted)
		{
			return Response.ok("Friend request accepted :)").build();
		}
		else {
			return  Response.status(Response.Status.BAD_REQUEST).entity("Failed to accept Friend Request").build();
		}
		
	}
	
	@DELETE
	@Path("/delete")
	public Response rejectFriendRequest(@QueryParam("requestId") int requestId) {
		boolean rejected = con_service.rejectFriendRequest(requestId);
		
		if(rejected)
		{
			return Response.ok("Friend request rejected!").build();
		}
		else {
			return  Response.status(Response.Status.BAD_REQUEST).entity("Failed to reject Friend Request").build();
		}
	}
	
	@GET
	@Path("listpending")
	public Response listPendingRequest(@QueryParam("userId") int userId) {
		
		List<FriendRequest> pending_requests= con_service.ListPendingRequests(userId);
		return Response.ok(pending_requests).build();
	}
	
	@GET
	@Path("/friends")
	public Response listAllFriends(@QueryParam("userId") int userId) {
		
		List<User> friends = con_service.ListAllFriends(userId);
		return Response.ok(friends).build();
	}
	
	
	
	
	

}
