package models;


import javax.ejb.Stateless;
import javax.persistence.*;

@Stateless
@Entity
public class Like {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int likeId;
	
	@ManyToOne
	@JoinColumn(name= "userId")
	private User user;
	
	@ManyToOne
	@JoinColumn(name= "postId")
	private Post post;
	
	public User getUser() {
		return user;
	}
	
	public Post getPost() {
		return post;
	}
	

	public void setUser(User u) {
		this.user = u;
	}
	
	public void setPost(Post p) {
		this.post = p;
	}
}
