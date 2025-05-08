package services;

import models.Group;

public interface GroupService {
	public boolean createGroup(Group group, int creatorId);
	public boolean requestJoin(int groupId, int  userId);
	public boolean approveRequest(int groupId, int userId, int adminId);
    public boolean rejectRequest(int groupId, int userId, int adminId);
    public boolean leaveGroup(int groupId, int userId);
    public boolean promoteToAdmin(int groupId, int userId, int requesterId);
    public boolean removeMember(int groupId, int userId, int requesterId);
    public boolean deleteGroup(int groupId, int requesterId);
	

}
