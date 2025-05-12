package notification;




public class EventNotification {
	
	public enum notificationType{
		FRIEND_REQUEST,
		REQUEST_ACCEPTED,
		REQUEST_REJECTED,
		POST_LIKED,
		POST_COMMENTED,
		GROUP_REQUESTED,
		GROUP_REJECTED,
		GROUP_APPROVED,
		GROUP_JOINED,
		GROUP_KICKED,
		GROUP_PROMOTED
	}
	
	private int userId;
			

	private notificationType Type;
	
	private String message;
	
	public EventNotification() {}
	
	public EventNotification(int userId, notificationType eventType, String message) {
		
        this.userId = userId;
        
		this.Type= eventType;
        this.message = message;
        
	}
	
	  public notificationType getEventType() {
		  return Type; 
		  
	  }
	  
	
	    public String getMessage() {
	    	
	    	
	    return message; 
	    
	    }
	    
		public int getUserId() { 
			    	
		return userId; 
			    	
		}
	    
	    
	    public void setMessage(String message) { 
	    	
	    	this.message = message; 
	    	
	    }

	   
	    
	    public void setUserId(int userId) {
	    	
	    	this.userId = userId;
	    	
	    }
	    
		 public void setEventType(notificationType eventType) {
			    	
			    	this.Type = eventType; 
			    	
		 }

	   
}
