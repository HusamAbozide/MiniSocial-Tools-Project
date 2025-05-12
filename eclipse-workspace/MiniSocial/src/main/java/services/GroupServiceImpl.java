package services;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.Group;
import models.GroupMember;
import models.GroupMember.GroupRole;
import models.User;
import notification.EventNotification;
import notification.EventNotification.notificationType;


@RolesAllowed({"AMDIN", "USER"})
@Stateless
public class GroupServiceImpl implements GroupService {
	
	
	@Inject
	private JMSContext jmscontext;
	
	
	@Resource(lookup = "java:/jms/queue/NotificationQueue")
	private Queue notifications;

	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private JMSContext jmsContext;
	
//	@Resource(lookup = "java:/jms/queue/GroupNotifications")
//    private Queue groupQueue;
	
private void notify(int userId, notificationType type, String message) {
    	
    	try {
    		
    		EventNotification not = new EventNotification(userId, type, message);
    	    		
    		ObjectMapper mapper = new ObjectMapper();
    		
    		String body = mapper.writeValueAsString(not);
    		
    		TextMessage msg = jmscontext.createTextMessage(body);
    		
    		jmscontext.createProducer().send(notifications, msg);
    		

    	}
    	catch(Exception e) {
            System.out.println("Exception in notify(): " + e.getMessage());
    	}
    	
}
	
	private GroupMember findMembership(int groupId, int userId) {
        return em.createQuery("SELECT gm FROM GroupMember gm WHERE gm.group.id = :gid AND gm.user.id = :uid", GroupMember.class).setParameter("gid", groupId).setParameter("uid", userId)
        .getResultStream().findFirst().orElse(null);
        
    }
	
	private boolean validateAdmin(int groupId, int userId) {
        GroupMember gm = findMembership(groupId, userId);
        if (gm == null || gm.getRole() != GroupRole.ADMIN || !gm.getIsApproved()) {
            return false;
        }
        return true;
    }
	
//	 private void notify(String msg) {
//	        jmsContext.createProducer().send(groupQueue, msg);
//	    }
	
	@Override 
	public boolean createGroup(Group group, int creatorId) {
		try {
			
		
			User creator = em.find(User.class, creatorId);
			
			if(creator == null)
			{
			 return false;
			}
		
			group.setCreator(creator);
			em.persist(group);
			
			GroupMember gm = new GroupMember();
			gm.setGroup(group);
			gm.setUser(creator);
			gm.setRole(GroupRole.ADMIN);
			gm.setIsApproved(true);
			
			em.persist(gm);
			return true;
			
		}catch(Exception e) {
			System.out.println("Exception in createGroup() as" + e.getMessage());
            return false;
		}
			
		
	}
	
	@Override
    public boolean requestJoin(int groupId, int userId) {
        try {
		Group group = em.find(Group.class, groupId);
        User user = em.find(User.class, userId);
        
        if(group == null || user == null) {
        	return false;
        }
        
        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);
        member.setRole(GroupRole.MEMBER);
        member.setIsApproved(group.isOpen());
        
        if (group.isOpen()) {
            notify(user.getUserId(), notificationType.GROUP_JOINED, "You joined " +group.getName()+" :)" );
        }
        else {
        	notify(group.getCreator().getUserId(), notificationType.GROUP_REQUESTED, user.getUsername()+" requested to join your group");
        	
        }
       
        em.persist(member);
        return true;

        
       }catch(Exception e) {
    	   System.out.println("Exception in requestJoin() as" + e.getMessage());
           return false;
       }
    }
	
	@Override
	public boolean approveRequest(int groupId, int userId, int adminId) {
		try {
			
			if(!validateAdmin(groupId, adminId)) {
				return false;
			}
			
			GroupMember gm = findMembership(groupId, userId);
			
			if(gm != null && !gm.getIsApproved()) {
				gm.setIsApproved(true);
				
				em.merge(gm);
	            
				notify(userId, notificationType.GROUP_APPROVED, "Your request to join "+ gm.getGroup().getName()+" was APPROVED :)");
				return true;
			}
			return false;
			
		}catch(Exception e) {
			
			 System.out.println("Exception in approveRequest() as" + e.getMessage());
	           return false;
		}
	}
	
	@Override
	public boolean rejectRequest(int groupId, int userId, int adminId) {
		try {
			
			if(!validateAdmin(groupId, adminId)) {
				return false;
			}
			
			GroupMember gm = findMembership(groupId, userId);
			
			if(gm != null && !gm.getIsApproved()) {
				
				em.remove(gm);
				
				notify(userId, notificationType.GROUP_REJECTED, "Your request to join "+ gm.getGroup().getName()+" was REJECTED :(");
				return true;
			}
			return false;
			
			}catch(Exception e) {
			
			 System.out.println("Exception in rejectRequest() as" + e.getMessage());
	           return false;
		}
	}
	
	@Override 
	public boolean leaveGroup(int groupId, int userId) {
		try{
			GroupMember gm = findMembership(groupId,userId);
			if(gm == null) {
				return false;
			}
			em.remove(gm);
			
			return true;
		}catch(Exception e) {
		 System.out.println("Exception in leaveGroup() as" + e.getMessage());
		 return false;
			
		}
		
	}
	
	@Override
	public boolean promoteToAdmin(int groupId, int userId, int adminId) {
		try {
	
			if(!validateAdmin(groupId, adminId)) {
				return false;
			}
			
			GroupMember gm = findMembership(groupId, userId);
			if(gm != null && !gm.getIsApproved()) {
				gm.setRole(GroupRole.ADMIN);
				em.merge(gm);
				
				notify(userId, notificationType.GROUP_PROMOTED, "You were promoted to Admin by the creator "+gm.getGroup().getCreator().getUsername()+" :)");
			
				return true;
			}
			return false;
			
			}catch(Exception e) {
			
			 System.out.println("Exception in promoteToAdmin() as" + e.getMessage());
	           return false;
		}
	}
	
	@Override
	public boolean removeMember(int groupId, int userId, int adminId) {
		try {
	
			if(!validateAdmin(groupId, adminId)) {
				return false;
			}
			
			GroupMember gm = findMembership(groupId, userId);
			if(gm != null) {
			
				em.merge(gm);
				
				notify(userId, notificationType.GROUP_KICKED, "You were kicked out from the group by the admin "+gm.getGroup().getCreator().getUsername()+" :)");
				return true;
			}
			return false;
			
			}catch(Exception e) {
			
			 System.out.println("Exception in removeMember() as" + e.getMessage());
	           return false;
		}
	}
	
	@Override
	public boolean deleteGroup(int groupId, int adminId) {
		try {
	
			if(!validateAdmin(groupId, adminId)) {
				return false;
			}
			
			Group gp = em.find(Group.class, groupId);
			if(gp != null) {
			
				em.remove(gp);
				
				return true;
			}
			return false;
			
			}catch(Exception e) {
			
			 System.out.println("Exception in removeMember() as" + e.getMessage());
	           return false;
		}
	}
	
	
}
	
	
	
	
	
	


