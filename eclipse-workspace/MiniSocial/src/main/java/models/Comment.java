package models;


import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int commentId;
	
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User author; 
	
	@ManyToOne
	@JoinColumn(name = "postId")
	private Post post; 
	
	
	
	public User getUser() {
		
		return author;
	}
	
	public Post getPost() {
		return post;
	}
	
	public int getCommentId() {
		return commentId;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setUser(User u) {
		this.author = u;
	}
	
	public void setPost(Post p) {
		this.post = p;
	}
	
	public void setContent(String cont) {
		this.content = cont;
	}
	
	public void setCommentId(int id) {
		this.commentId = id;
		
	}
	
	public void setCommentContent(String cont) {
		this.content = cont;
		
	}
	
	

}
