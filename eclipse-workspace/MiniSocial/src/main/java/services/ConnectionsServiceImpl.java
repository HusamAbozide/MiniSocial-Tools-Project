package services;

import models.FriendRequest;
import models.FriendRequest.RequestStatus;
import models.User;
import notification.EventNotification;
import notification.EventNotification.notificationType;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.TextMessage;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.StringReader;
import java.util.List;
import javax.jms.Queue;




@Stateless
@RolesAllowed({"ADMIN", "USER"})
public class ConnectionsServiceImpl implements ConnectionsService {
	
	@Inject
	private JMSContext jmscontext;
	
	
	@Resource(lookup = "java:/jms/queue/NotificationQueue")
	private Queue notifications;

    @PersistenceContext(unitName = "hello")
    private EntityManager em;
    
    
    
    

    @Override
    public boolean sendFriendRequest(int senderID, int receiverID) {
        try {
            User sender = em.find(User.class, senderID);
            User receiver = em.find(User.class, receiverID);

            if (sender == null || receiver == null || sender.equals(receiver)) {
                return false;
            }

            List<FriendRequest> existingRequests = em.createQuery(
            		
                "select f from FriendRequest f where " +
                "((f.sender = :sender and f.reciever = :receiver) or (f.sender = :receiver and f.reciever = :sender)) " +
                "and f.status = :status", FriendRequest.class).setParameter("sender", sender)
                .setParameter("receiver", receiver)
                .setParameter("status", RequestStatus.PENDING)
                .getResultList();

            if (!existingRequests.isEmpty()) {
                return false;
            }

            FriendRequest request = new FriendRequest();
            request.setSender(sender);
            
            request.setReciever(receiver);
            
            request.setStatus(RequestStatus.PENDING);

            em.persist(request);
            
            notify(receiver.getUserId(), notificationType.FRIEND_REQUEST, sender.getUsername() + "send you have a friend request :)");
            return true;

        } catch (Exception e) {
            System.out.println("Exception in sendFriendRequest: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean acceptFriendRequest(int requestId) {
        try {
            FriendRequest request = em.find(FriendRequest.class, requestId);

            if (request == null || request.getStatus() != RequestStatus.PENDING) {
                return false;
            }

            request.setStatus(RequestStatus.ACCEPTED);
            em.merge(request);
            
            notify(request.getSender().getUserId(),notificationType.REQUEST_ACCEPTED,request.getReceiver().getUsername()+"accepted your friend request :)" );
            
            return true;

        } catch (Exception e) {
            System.out.println("Exception in acceptFriendRequest: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean rejectFriendRequest(int requestId) {
        try {
            FriendRequest request = em.find(FriendRequest.class, requestId);

            if (request == null || request.getStatus() != RequestStatus.PENDING) {
                return false;
            }

            request.setStatus(RequestStatus.REJECTED);
            em.merge(request);
            
            notify(request.getSender().getUserId(),notificationType.REQUEST_REJECTED,request.getReceiver().getUsername()+"rejected your friend request :(" );

            return true;

        } catch (Exception e) {
            System.out.println("Exception in rejectFriendRequest: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean removeFriend(int userId, int removedUserId) {
    	try {
            List<FriendRequest> results = em.createQuery(
                "select f FROM FriendRequest f where " +
                "((f.sender.userId = :user1 and f.reciever.userId = :user2) or " +
                "(f.sender.userId = :user2 and f.reciever.userId = :user1)) " +
                "and f.status = :status", FriendRequest.class).setParameter("user1", userId).setParameter("user2", removedUserId).setParameter("status", RequestStatus.ACCEPTED).getResultList();

            if (!results.isEmpty()) {
                em.remove(results.get(0));  
                return true;
            }

            return false;

        } catch (Exception e) {
            System.out.println("Exception in removeFriend: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<FriendRequest> ListPendingRequests(int userId) {
        return em.createQuery(
            "select f from FriendRequest f where f.reciever.userId = :id and f.status = :status", FriendRequest.class)
            .setParameter("id", userId)
            .setParameter("status", RequestStatus.PENDING)
            .getResultList();
    }
    
    

    @Override
    public List<User> ListAllFriends(int userId) {
        return em.createQuery(
            "select CASE " +
                "WHEN f.sender.userId = :id THEN f.reciever " +
                "ELSE f.sender " +
            "END " +
            "from FriendRequest f " +
            "where (f.sender.userId = :id or f.reciever.userId = :id) " +
            "and f.status = :status", User.class)
            .setParameter("id", userId)
            .setParameter("status", RequestStatus.ACCEPTED)
            .getResultList();
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
