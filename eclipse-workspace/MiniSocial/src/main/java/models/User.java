package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.List;
import java.util.Objects;


@Entity
public class User {
	
	public enum Role{
		USER,ADMIN
	}
		
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	
	@Column(unique = true, nullable = false)
	private String username;
	
    @Column(nullable = false)
    private String password;
	
    @Column(length=200)
    private String bio;
	
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @OneToMany(mappedBy = "sender")
    private List<FriendRequest> sentRequests;

    @OneToMany(mappedBy = "reciever")
    private List<FriendRequest> receivedRequests;
    
    @OneToMany(mappedBy= "author")
    private List<Post> posts;
    
    @OneToMany(mappedBy = "author")
    private List<Comment> comments;
    
    @OneToMany(mappedBy = "user")
    private List<Like> likes;
    
    @OneToMany(mappedBy = "creator")
    private List<Group> createdGroups;
    
    
    
	
	public Role getRole() {
		return role;
	}
	
	public int getUserId() {
	        return userId;
	    }
	    
	public String getUsername() {
	    	
	        return username;
	    }
	    
	public String getPassword() {
	    	
	        return password;
	    }
	    
	public String getEmail() {
	    	
	        return email;
	    }
	    
   public String getBio() {
    	
        return bio;
    }
   
   public void setRole(Role r) {
	   this.role = r;
   }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public void setUsername(String username) {
    	
        this.username = username;
    }
    
    public void setPassword(String password) 
    {	
    	this.password = password;
    }
    
    public void setEmail(String email) 
    {
    	
       this.email = email;
    }
    
    public void setBio(String bio) {
    
        this.bio = bio;
    }
    
    @Override
    public boolean equals(Object o) {
    	
        if (this == o) {
        	
        	return true;
        	}
        
        if (!(o instanceof User)) {
        	
        	return false;
        	
        }
        User user = (User) o;
        
        return Objects.equals(userId, user.userId); 
    }

    @Override
    public int hashCode() {
    	
        return Objects.hash(email);
    }
	

}
