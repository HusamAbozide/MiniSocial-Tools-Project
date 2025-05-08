package services;

import java.util.List;

import models.Post;


public interface PostService {
	
	public boolean createPost(Post post, int userId, Integer groupId);
	
	List<Post> getFeed(int userId);
	
    boolean updatePost(int postId, String newContent, String newImageUrl, int userId);
    
    boolean deletePost(int postId, int userId);
    
    boolean likePost(int postId, int userId);
    
    boolean commentOnPost(int postId, int userId, String commentText);
}
