package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.User;

@Stateless
@Path("/users2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class UserController2 {
	
	@PersistenceContext(unitName="hello")
	private EntityManager em;
	
	ArrayList<User> logged_users = new ArrayList<User>();  
	
//	@GET
//	@Path("test")
//	public Response test() {
//	    return Response.ok("Test works").build();
//	}
	
	@POST
	@Path("register")
	public Response registerUser(User user) {
		
		User exists = em.find(User.class, user.getEmail());
		
		//String query = "select u from User u where u.userId = :id";
		
//		String simpleQuery="SELECT u.userId from User u";
//		Query query=em.createQuery(simpleQuery);
//		List<Integer> result = query.getResultList();
//		
		if(exists != null)
		{	
			
			return Response.status(Response.Status.CONFLICT).entity("User already registered!").build();
			
		}
		
		else{
			
			em.persist(user);
			return Response.status(Response.Status.OK).entity("User registered successfully").build();
			
		}
				
		
	}
	
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userLogin(User user) {
		
		User exists = em.find(User.class, user.getEmail());
		
		//int id = user.getUserId();
		
		//User exists = em.createQuery("select u from User u where u.email = :email",User.class).setParame ter("email", user.getEmail() ).getSingleResult();
		
		
		if(exists == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("You do not have an account to log in!").build();
		}
		
		
		else if(logged_users.contains(exists)) {
			
			return Response.status(Response.Status.BAD_REQUEST).entity("Already logged in!").build();

		}
		
		
		else {
			logged_users.add(exists);
			return Response.status(Response.Status.OK).entity("Logged in successfuly").build();
		}
	}
	
	
	
	@GET
	@Path("getuser/{username}")
	public Response getUser(@PathParam("username") String username) {
		
		User exists = em.createQuery("select u from User u where u.username = :username",User.class).setParameter("username", username).getSingleResult();
		
		if(exists == null) {
			
			return Response.status(Response.Status.NOT_FOUND).entity("There is no users with the given Username").build();
		}
		
		else {
			return Response.status(Response.Status.OK).entity(exists).build();
		}
		
		
	}

}
