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

	    @ManyToMany
	    @JoinTable(name = "group_members", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	    private List<User> members;
	    
	    

	    @OneToMany(mappedBy = "group")
	    private List<Post> posts;

}
