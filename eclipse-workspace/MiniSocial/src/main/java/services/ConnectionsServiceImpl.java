package services;

import models.FriendRequest;
import models.FriendRequest.RequestStatus;
import models.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ConnectionsServiceImpl implements ConnectionsService {

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

            // Check if a pending request already exists
            List<FriendRequest> existingRequests = em.createQuery(
                "SELECT f FROM FriendRequest f WHERE " +
                "((f.sender = :sender AND f.reciever = :receiver) OR (f.sender = :receiver AND f.reciever = :sender)) " +
                "AND f.status = :status", FriendRequest.class)
         
                .setParameter("sender", sender)
                
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
                "SELECT f FROM FriendRequest f WHERE " +
                "((f.sender.userId = :user1 AND f.reciever.userId = :user2) OR " +
                "(f.sender.userId = :user2 AND f.reciever.userId = :user1)) " +
                "AND f.status = :status", FriendRequest.class)
                .setParameter("user1", userId)
                .setParameter("user2", removedUserId)
                .setParameter("status", RequestStatus.ACCEPTED)
                .getResultList();

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
            "SELECT f FROM FriendRequest f WHERE f.reciever.userId = :id AND f.status = :status", FriendRequest.class)
            .setParameter("id", userId)
            .setParameter("status", RequestStatus.PENDING)
            .getResultList();
    }
    
    

    @Override
    public List<User> ListAllFriends(int userId) {
        return em.createQuery(
            "SELECT CASE " +
                "WHEN f.sender.userId = :id THEN f.reciever " +
                "ELSE f.sender " +
            "END " +
            "FROM FriendRequest f " +
            "WHERE (f.sender.userId = :id OR f.reciever.userId = :id) " +
            "AND f.status = :status", User.class)
            .setParameter("id", userId)
            .setParameter("status", RequestStatus.ACCEPTED)
            .getResultList();
    }
}
