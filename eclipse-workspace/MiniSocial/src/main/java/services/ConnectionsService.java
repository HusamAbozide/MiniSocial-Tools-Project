package services;

import java.util.List;

import models.FriendRequest;
import models.User;

public interface ConnectionsService {
	
	public boolean sendFriendRequest(int senderID, int receiverID);
	
	public boolean acceptFriendRequest(int requestId);
	
	public boolean rejectFriendRequest(int requestId);
	public boolean removeFriend(int userId, int removedUserId);
	
	public List<FriendRequest> ListPendingRequests(int userId);
	
	public List<User> ListAllFriends(int userId);
	
	

}
