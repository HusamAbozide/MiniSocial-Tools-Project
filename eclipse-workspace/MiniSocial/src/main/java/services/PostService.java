package services;

import java.util.List;

import models.Comment;
import models.Post;


public interface PostService {
	
	public boolean createPost(Post post, int userId, Integer groupId);
	
	List<Post> getFeed(int userId);
	
    boolean updatePost(int postId, Post updatedPost, int userId);
    
    boolean deletePost(int postId, int userId);
    
    boolean likePost(int postId, int userId);
    
    boolean commentPost(Comment comment);
}
