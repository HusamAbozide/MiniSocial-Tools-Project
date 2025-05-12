package models;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Stateless
@Entity
public class FriendRequest {
	
	public enum RequestStatus{
		
		ACCEPTED, REJECTED, PENDING
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int requestId;
	
	@ManyToOne
	@JoinColumn(name="sender_id", nullable = false)
	private User sender;
	
	@ManyToOne
	@JoinColumn(name="reciever_id", nullable = false)
	private User reciever;
	
	@Enumerated(EnumType.STRING)
	private RequestStatus status;
	
	
	public void setRequestId(int id) {
		
		this.requestId = id;
	}
	
	public void setSender(User s) {
		
		this.sender = s;
	}
	
	public void setReciever(User r) {
		
		this.reciever = r;
	}
	
	public void setStatus(RequestStatus rs) {
		this.status = rs;
	}
	
	
	
	
	public int getId() {
		
		return requestId;
	}
	
	public User getSender() {
		
		return sender;
	}
	
	public User getReceiver() {
		
		return reciever;
	}
	
	public RequestStatus getStatus() {
		return status;
	}
	
	
	

}
