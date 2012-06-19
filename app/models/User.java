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
    
    public User(String email, String password, String firstName, String lastName, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
    }
    
    public static User connect(String email, String password) {
        return find("byEmailAndPassword", email, password).first();
    }
    
    public static List<User> listAdminUsers(){
        List<User> users = User.find("select u from User u where u.isAdmin = true").fetch();
        return users;
    }
    
    public static User getUserByEmail(String email){
      List<User> users = User.find("select u from User u where u.email = '" + email + "'").fetch();
      User user = users.get(0);
      return user;
    }
    
    public static List<User> listUsers(){
        List<User> users = User.find("select u from User u where u.isAdmin = false").fetch();
        return users;
    }
    
    public String toString() {
        return firstName + " " + lastName;
    }
 
}
