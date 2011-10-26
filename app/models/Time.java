package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Time extends Model {

  @Required
  @ManyToOne
  public User staff;
  
  @Required
  @ManyToOne
  public Duty Duty;
  
	@Required
	public int Hours;
	
	@Required
	public int Minutes;

	@Required
	public Date Date;
	
	public Date capturedDate;
	
	
    
  public Time (int hours, int minutes, User staff, Duty duty) {
      this.Hours = hours;
      this.Minutes = minutes;
      this.staff = staff;
      this.Duty = duty;
  }
  
  public int getTimeMinutes(){
    return this.Minutes + (this.Hours * 60);
  }
  
  public int getTimeHours(){
    return this.Hours + (this.Minutes / 60);
  }
  
  public static List<Time> listBycategoryAndUser(String category, String email){
    List<Time> times = Time.find("select t from Time t join t.Duty d join d.Category c join t.staff u where" + 
      " c.name = '" + category + "' and u.email = '" + email + "'").fetch();
    return times;
  }

}
