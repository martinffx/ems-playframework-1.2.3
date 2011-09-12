package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class User extends Model {

    @Email
    @Required
    public String email;
    
    @Required
    @Password
    public String password;
    
    public String firstName;
    
    public String lastName;
    
    public Date dob;
    
    public boolean isAdmin;
    
    @ManyToMany(cascade=CascadeType.PERSIST)
	public Set<Duty> duties;
    
    
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public User(String email, String password, String firstName, String lastName, Date dob) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }
    
    public static User connect(String email, String password) {
        return find("byEmailAndPassword", email, password).first();
    }

    public String toString() {
        return firstName + " " + lastName;
    }
 
}