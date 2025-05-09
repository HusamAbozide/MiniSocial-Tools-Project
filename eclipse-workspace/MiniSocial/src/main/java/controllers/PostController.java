package controllers;


import javax.ws.rs.core.*;

import models.Comment;
import models.Group;
import models.Post;
import services.PostService;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.*;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class PostController {
	
	@Inject
	private PostService postservice;
	
	@POST
	public Response createPost(@QueryParam("userId") int userId, @QueryParam("groupId") Integer groupId, Post post) {
		
		boolean created = postservice.createPost(post, userId, groupId);
		
		if(created) {
			return Response.status(Response.Status.CREATED).entity(post).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create post!").build();
	}
	
	@GET
	@Path("/feed/{userId}")
	public Response getFeed(@PathParam("userId") int userId) {
		
		List<Post> feed = postservice.getFeed(userId);
		
		if(feed != null) {
			return Response.ok(feed).build();
		}
		
        return Response.status(Response.Status.BAD_REQUEST).entity("Could not retrieve feed!").build();
        
	}
	
	@PUT
	@Path("/update/{postId}")
	public Response updatePost(@PathParam("postId") int postId, @QueryParam("userId") int userId, Post newPost) {
		
		boolean isupdated = postservice.updatePost(postId, newPost ,userId);
		
		if(isupdated) {
			return Response.ok("Post updated successfuly :)").build();
			
		}
        return Response.status(Response.Status.BAD_REQUEST).entity("Error updating post!").build();
	}
	
	@DELETE
	@Path("/delete/{postId}")
	public Response deletePost(@PathParam("postId") int postId, @QueryParam("userId") int userId) {
		
		
		boolean isdeleted = postservice.deletePost(postId, userId);
	
		if(isdeleted) {
			return Response.ok("Post deleted successfuly :)").build();
			
		}
        return Response.status(Response.Status.BAD_REQUEST).entity("Error deleting post!").build();
	}
	
	@POST
	@Path("/like/{postId}")
	public Response likePost(@PathParam("postId") int postId, @QueryParam("userId") int userId) {	
			
		boolean like = postservice.likePost(postId ,userId);
		
		if(like) {
			return Response.ok("Post liked successfuly :)").build();
			
		}
        return Response.status(Response.Status.BAD_REQUEST).entity("Post like failed!").build();
		
	}
	
	@POST
	@Path("/comment")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response commentPost(Comment comment) {
		
		boolean commented = postservice.commentPost(comment);
		
		if(commented) {
			return Response.ok("Comment added successfuly :)").build();
			
		}
        return Response.status(Response.Status.BAD_REQUEST).entity("Error adding comment :(").build();
	}

	
	
	
	

}
