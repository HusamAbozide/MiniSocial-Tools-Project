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
}
