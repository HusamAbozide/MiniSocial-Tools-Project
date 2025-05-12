package services;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.Post;
import models.User;
import notification.EventNotification;
import notification.EventNotification.notificationType;
import models.Group;
import models.Like;
import models.Comment;



@Stateless
@RolesAllowed({"USER", "ADMIN"})
public class PostServiceImpl implements PostService{
	
	@Inject
	private JMSContext jmscontext;
	
	
	@Resource(lookup = "java:/jms/queue/NotificationQueue")
	private Queue notifications;

	
	@PersistenceContext
    private EntityManager em;

    @Override
    public boolean createPost(Post post, int userId, Integer groupId) { // groupId might be null 
        try {
            User user = em.find(User.class, userId);
            if (user == null) return false;

            post.setAuthor(user);

            if (groupId != null) {
                Group group = em.find(Group.class, groupId);
                if (group == null) return false;
                post.setGroup(group);
            }

            em.persist(post);
            return true;
            
        } catch (Exception e) {
        	
            System.out.println("Exception in createPost: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Post> getFeed(int userId) {
        try {
            return em.createQuery(
                "select p FROM Post p where p.author.id = :userId or p.author.id IN (" +
                "select c.friend.id from Connection c WHERE c.user.id = :userId and c.status = 'ACCEPTED'" +
                ") order by p.postId DESC", Post.class)
                .setParameter("userId", userId)
                .getResultList();
        } catch (Exception e) {
            System.out.println("Exception in getFeed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updatePost(int postId, Post updatedPost, int userId) {
        try {
            Post storedPost = em.find(Post.class, postId);
            
            if (storedPost == null || storedPost.getAuthor().getUserId() != userId) {
            	return false;
            }

            storedPost.setContent(updatedPost.getContent());
            
            storedPost.setImageUrl(updatedPost.getImageUrl());
            
            em.merge(storedPost);
            return true;
            
            
        } catch (Exception e) {
            System.out.println("Exception in updatePost: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletePost(int postId, int userId) {
        try {
            Post post = em.find(Post.class, postId);
            if (post == null || post.getAuthor().getUserId() != userId) return false;

            em.remove(post);
            
            return true;
            
        } catch (Exception e) {
        	
            System.out.println("Exception in deletePost: " + e.getMessage());
            
            return false;
        }
    }

    @Override
    public boolean likePost(int postId, int userId) {
        try {
            Post post = em.find(Post.class, postId);
            User user = em.find(User.class, userId);

            if (post == null || user == null) return false;

            // Prevent duplicate likes
            TypedQuery<Like> query = em.createQuery(
                "SELECT l FROM Like l WHERE l.post.id = :postId AND l.user.id = :userId", Like.class);
            query.setParameter("postId", postId);
            query.setParameter("userId", userId);
            if (!query.getResultList().isEmpty()) return false;

            Like like = new Like();
            
            like.setPost(post);
            
            like.setUser(user);

            em.persist(like);
            
            notify(post.getAuthor().getUserId(), notificationType.POST_LIKED, user.getUsername()+ " liked you post :)");
            return true;
            
        } catch (Exception e) {
        	
            System.out.println("Exception in likePost: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean commentPost(Comment comment) {
        try {
            if (comment == null || comment.getUser() == null || comment.getPost() == null) {
                return false;
            }
            Post post = em.find(Post.class, comment.getPost().getPostId());
            
            User user = em.find(User.class, comment.getUser().getUserId());

            if (post == null || user == null) return false;

            comment.setPost(post);
            
            comment.setUser(user);

            em.persist(comment);
            
            notify(post.getAuthor().getUserId(), notificationType.POST_COMMENTED, user.getUsername()+" commented on your post :)");
            
            return true;
            
        } catch (Exception e) {
        	
            System.out.println("Exception in commentOnPost: " + e.getMessage());
            
            return false;
        }
    }
    
private void notify(int userId, notificationType type, String message) {
    	
    	try {
    		EventNotification not = new EventNotification(userId, type, message);
    		
//    		JsonReader jsonReader = Json.createReader(new StringReader(body));
    		
    		ObjectMapper mapper = new ObjectMapper();
    		
    		String body = mapper.writeValueAsString(not);
    		
    		TextMessage msg = jmscontext.createTextMessage(body);
    		
    		jmscontext.createProducer().send(notifications, msg);
    		

    	}catch(Exception e) {
            System.out.println("Exception in notify(): " + e.getMessage());
    	}
    	
    }


	
}
