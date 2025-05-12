package services;

import models.User;

public interface UserService {
	
	public boolean addUser(User u); 
	
	public boolean Userlogin(User u);
	
	public boolean deleteUser(int targetId, int callerId, boolean isAdmin);
	
	public User getUser(String username);
	
	public User[] getAllUsers();
	
	public boolean updateProfile(String username, User updatedUser);

}
