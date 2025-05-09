package controllers;

import javax.ws.rs.core.*;

import models.Group;
import services.GroupService;

import javax.inject.Inject;
import javax.ws.rs.*;

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class GroupController {
	
	@Inject
	private GroupService groupservice;
	
	@POST
	@Path("/create")
	public Response createGroup(Group group, @QueryParam("creatorId") int creatorId) {
		
		boolean isCreated = groupservice.createGroup(group, creatorId);
		
		if(isCreated) {
			return Response.status(Response.Status.CREATED).build();
		}
		else {
			return Response.status(Response.Status.BAD_GATEWAY).entity("Error creating group!").build();
		}
	}
	
	 @POST
	 @Path("/join/{groupId}")
	 public Response requestJoin(@PathParam("groupId") int groupId, @QueryParam("userId") int userId) {
		 
	        boolean joined= groupservice.requestJoin(groupId, userId);
	        
	        if(joined) {
	        	
	        	return Response.ok("Group created successfuly :)").build();
	        }
	        else {
	        	return Response.status(Response.Status.BAD_REQUEST).entity("Error joining group!").build();
	        }
	 }
	 
	@POST
	@Path("/approve/{groupId}")
    public Response approveRequest(@PathParam("groupId") int groupId,@QueryParam("userId") int userId,@QueryParam("adminId") int adminId) {
		
        boolean approved = groupservice.approveRequest(groupId, userId, adminId);
        
        if(approved) {
        	
        	return Response.ok("Approved :)").build();
        }
        else {
        	return Response.status(Response.Status.BAD_REQUEST).entity("Error approving request!").build();
        }
      
    }
	
	@POST
	@Path("/reject/{groupId}")
    public Response rejectRequest(@PathParam("groupId") int groupId,@QueryParam("userId") int userId,@QueryParam("adminId") int adminId) {
		
        boolean rejected = groupservice.rejectRequest(groupId, userId, adminId);
        
        if(rejected) {
        	
        	return Response.ok("Rejected :)").build();
        }
        else {
        	return Response.status(Response.Status.BAD_REQUEST).entity("Error rejecting request!").build();
        }
      
    }
	
	@POST
	@Path("/promote/{groupId}")
	public Response promoteToAdmin(@PathParam("groupId") int groupId, @QueryParam("userId") int userId, @QueryParam("adminId") int adminId) {
		
		boolean promoted = groupservice.promoteToAdmin(groupId, userId, adminId);
		
		 if(promoted) {
	        	
	        	return Response.ok("User Promoted Successfuly :)").build();
	        }
	        else {
	        	return Response.status(Response.Status.BAD_REQUEST).entity("Error promoting user!").build();
	        }
	}
	
	@DELETE
	@Path("/leave/{groupId}")
	
	public Response leaveGroup(@PathParam("groupId") int groupId, @QueryParam("userId") int userId) {
		
		boolean left = groupservice.leaveGroup(groupId, userId);
		
		 if(left) {
	        	
	        	return Response.ok("Left Group").build();
	        }
	        else {
	        	return Response.status(Response.Status.BAD_REQUEST).entity("Unable to leave group!").build();
	        }
	}
	
	@DELETE
	@Path("/remove/{groupId}")
	
	public Response removeMember(@PathParam("groupId") int groupId, @QueryParam("userId") int userId, @QueryParam("adminId") int adminId) {
		
		boolean removed = groupservice.removeMember(groupId, userId, adminId);
		
		 if(removed) {
	        	
	        	return Response.ok("User removed from group successfuly").build();
	        }
	        else {
	        	return Response.status(Response.Status.BAD_REQUEST).entity("Unable to remove user!").build();
	        }
	}
	
	@DELETE
	@Path("/delete/{groupId}")
	
	public Response deleteGroup(@PathParam("groupId") int groupId, @QueryParam("adminId") int adminId) {
		
		boolean deleted = groupservice.deleteGroup(groupId, adminId);
		
		 if(deleted) {
	        	
	        	return Response.ok("Group deleted successfuly").build();
	        }
	        else {
	        	return Response.status(Response.Status.BAD_REQUEST).entity("Unable to delete group!").build();
	        }
	}
	
	
	

}
