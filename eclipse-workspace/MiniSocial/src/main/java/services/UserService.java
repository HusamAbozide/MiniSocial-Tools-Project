package services;

import models.User;

public interface UserService {
	
	public boolean addUser(User u); 
	
	public boolean Userlogin(User u);
	
	public boolean deleteUser(String username);
	
	public User getUser(String username);
	
	public User[] getAllUsers();
	
	public boolean updateProfile(String username, User updatedUser);

}
