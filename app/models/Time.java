package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Time extends Model {

	@Required
	public Time time;
	
	public Date date;
	
	public Date capturedDate;
	
	@Required
    @ManyToOne
    public User staff;
    
    @Required
    @ManyToOne
    public Duty duty;
	    
    public Time (Time time, User staff, Duty duty) {
        this.time = time;
        this.staff = staff;
        this.duty = duty;
    }
    
    public static List<Time> listBycategoryAndUser(String category, Long userId){
      List<Time> times = Time.find("select t from Time t join t.duty d join d.Category c join t.staff u where c.name = "   +         category + " and u.id = " + userId).fetch();
      return times;
    }

}
