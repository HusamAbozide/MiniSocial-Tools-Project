package services;

import java.util.List;

//import javax.annotation.security.PermitAll;
//import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import models.User;

@Stateless
//@RolesAllowed({"ADMIN", "USER"})
public class UserServiceImpl implements UserService{
	
	@PersistenceContext(unitName="hello")
	private EntityManager em;
	
	@Override
	//@PermitAll
    public boolean addUser(User u) {
		
        try {
        	User user = em.createQuery("select u from User u where u.username = :username",User.class).setParameter("username",u.getUsername()).getSingleResult();

           return false;
        }
            
           catch (NoResultException e) {
        	   
               try {
            	   System.out.println("Persisting user: role=" + u.getRole());

                   em.persist(u);
                   return true;
               } catch (Exception ex) {
                   System.out.println("Exception persisting user: " + ex.getMessage());
                   return false;
               }

           } catch (Exception e) {
               System.out.println("Unexpected exception in addUser(): " + e.getMessage());
               return false;
           }
    }

    @Override
    public boolean deleteUser(int targetId, int callerId, boolean isAdmin) {
    
        try {
        	
        	User user = em.find(User.class, targetId);

            if(user == null) {return false;}
        	
        	
        	if (isAdmin || targetId == callerId) 
        	{
            	em.remove(user);
            	return true;
            }
        	
            else {
            	
            	System.out.println("Unauthorized");
            	return false;
            	
            }
           
        } catch (Exception e) {
            System.out.println("Exception in deleteUser as" + e.getMessage());
            return false;
        }
    }

    
	@Override
    public User getUser(String username) {
    	
    	
        try {
        	
        	User user = em.createQuery("select u from User u where u.username = :username",User.class).setParameter("username",username).getSingleResult();

            return user;
            
        } catch (NoResultException e) {
        	 System.out.println("User not found with username: " + username);
             return null;        
        }
    }

    @Override
    public User[] getAllUsers() {
    	
        try {
        	
        	List<User> users = em.createQuery("select u from User u ",User.class).getResultList();
        	return users.toArray(new User[0]);
            
        } catch (Exception e) {
            System.out.println("Exception in getAllUsers as" + e.getMessage());
        }
        return null;
    }

    @Override
    //@PermitAll
    public boolean Userlogin(User u) {
        try {
        	
        	User storedUser = em.createQuery("select u from User u where u.username = :username",User.class)
        			.setParameter("username",u.getUsername()).getSingleResult();
            
            if (storedUser != null && storedUser.getPassword().equals(u.getPassword())) {
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            System.out.println("Exception in Userlogin: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateProfile(String username, User updatedUser) {
    	
    	try {
    		
    		User storedUser = em.createQuery("select u from User u where u.username = :username",User.class)
        			.setParameter("username",username).getSingleResult();
    		
    		storedUser.setUsername(updatedUser.getUsername());
    		
    		storedUser.setEmail(updatedUser.getEmail());
    		
    		storedUser.setBio(updatedUser.getBio());
    		
    		storedUser.setPassword(updatedUser.getPassword());
    		
    		em.merge(storedUser);
    		return true;
    		
    	}catch (Exception e) {
    		System.out.println("Exception in Userlogin: " + e.getMessage());
            return false;    	
            
    	}
    }
    
    
    
    
    
    
    
    
    
    

}
