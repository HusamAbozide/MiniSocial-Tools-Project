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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.User;
import services.UserService;
import services.UserServiceImpl;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class UserController {
	
	@Inject
	private UserService userservice;
	//private UserService userservice = new UserServiceImpl();

	
	@POST
	@Path("/register")
    public Response addUser(User user) {
		
        boolean isAdded = userservice.addUser(user);
        if (isAdded) {
            return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
        }
        else {
            return Response.status(Response.Status.CONFLICT).entity("User registration failed").build();

        }
    }
	
	@POST
	@Path("/login")
	public Response loginUser(User user) {
		
		 boolean isLoggedIn = userservice.Userlogin(user);
	        if (isLoggedIn) {
	            return Response.ok("Login successful").build();
	        }
	        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid User Credentials").build();
	}
	
	@GET
	@Path("/getuser/{username}")
	public Response getUser (@PathParam("username") String username) {
		
		User user = userservice.getUser(username);
		if(user != null) {
				return Response.ok(user).build();
			}
		
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("User Not found!!").build();
		}
	  }
	
	@GET
	@Path("/getallusers")
	public Response getAllUsers() {
		
		User[] users = userservice.getAllUsers();
		return Response.ok(users).build();
	}
	
	@PUT
	@Path("/update/{username}")
	public Response updateProfile(@PathParam("usrname") String username, User updatedUser) {
		
		boolean isUpdated = userservice.updateProfile(username, updatedUser);
		if(isUpdated) {
			return Response.ok("Profile updated successfully :)").build();
		}
		else {
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to Update User").build();
		}
		
	}
	
	@DELETE
	@Path("/delete/{username}")
	public Response deleteUser(@PathParam("username") String username) {
		boolean isDeleted = userservice.deleteUser(username);
		
		if(isDeleted) {
			return Response.ok("Profile deleted successfully :)").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("User not found!!").build();
		}
	}
	
	
	
	
	
	
	

}
