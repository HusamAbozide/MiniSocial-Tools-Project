package models;

import notification.EventNotification.notificationType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Notifications implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;

    @Enumerated(EnumType.STRING)
    private notificationType type;

    private String message;


    public Notifications() {}

    public Notifications(int userId, notificationType type, String message) {
        this.userId = userId;
        this.type = type;
        this.message = message;
    }

    public int getId() { return id; }
    
    public int getUserId() { return userId; }
    
    public notificationType getType() { return type; }
   
    public String getMessage() { return message; }

    public void setId(int id) { this.id = id; }
    
    public void setUserId(int userId) { this.userId = userId; }
    
    public void setType(notificationType type) { this.type = type; }
    
    public void setMessage(String message) { this.message = message; }
    
}
