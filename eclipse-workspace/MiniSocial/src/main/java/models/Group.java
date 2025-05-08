package models;

import javax.persistence.*;
import java.util.List;

public class Group {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int groupId;
	 
	    private String name;
	    private String description;
	    private boolean isOpen = true;

	    @ManyToOne
	    @JoinColumn(name = "creator_id")
	    private User creator;

	    @OneToMany(mappedBy = "group")
	    private List<GroupMember> members;
	    
	    

	    @OneToMany(mappedBy = "group")
	    private List<Post> posts;
	    
	    public void setCreator(User c) {
	    	this.creator = c;
	    }
	    
	    public boolean isOpen() {
	    	return isOpen;
	    }
	    
	    public String getName() {
	    	return name;
	    }
	    
	    public String getDescription() {
	    	return description;
	    }


}
