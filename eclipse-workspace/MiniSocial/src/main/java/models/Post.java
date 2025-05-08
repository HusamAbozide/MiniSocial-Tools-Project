package models;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
@Entity
public class Post {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int postId;
	
	private String content;
	
	private String imageUrl;
	
	@ManyToOne
	@JoinColumn(name = "authorId")
	
	private User author;
	
	@ManyToOne
	@JoinColumn(name = "groupId")
	
	private Group group;
	
	@OneToMany(mappedBy = "post")
	
	private List<Comment> comments;
	
	
	@OneToMany(mappedBy= "post")
	
	private List<Like> likes;
	
	
	public void setAuthor(User a) {
		this.author = a;
	}
	
	public void setGroup(Group gp) {
		this.group = gp;
	}
	

	public void setPostId(int postId) {
	    this.postId = postId;
	}

	
	public void setContent(String content) {
	    this.content = content;
	}

	

	public void setImageUrl(String imageUrl) {
	    this.imageUrl = imageUrl;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public String getImageUrl() {
	    return imageUrl;
	}

	
	public int getPostId() {
	    return postId;
	}
	
	public String getContent() {
	    return content;
	}

	

}
