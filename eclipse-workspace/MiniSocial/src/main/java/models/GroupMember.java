package models;

import javax.persistence.*;


@Entity
public class GroupMember {
	public enum GroupRole{
		ADMIN,
		MEMBER
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int GroupMemberId;

    @ManyToOne
    private Group group;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private GroupRole role;

    private boolean isApproved;
    
    public void setGroup(Group g) {
    	this.group = g;
    }
    
    public void setUser(User u) {
    	this.user = u;
    }
    
    public void setRole(GroupRole r) {
    	this.role = r;
    }
    
    
    
    public void setGroupMemberId(int Id) {
    	this.GroupMemberId = Id;
    }
    
    public void setIsApproved(boolean flag) {
    	this.isApproved = flag;
    }
    
    public Group getGroup() {
    	return group;
    }
    
    public User getUser() {
    	return user;
    }
    
    public GroupRole getRole() {
    	return role;
    }
    
    public int getGroupMemberId() {
    	return GroupMemberId;
    }
    
    public boolean getIsApproved() {
    	return isApproved;
    }
    

}
