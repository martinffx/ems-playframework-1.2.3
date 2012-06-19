package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Duty extends Model {

    @Required
    public String Name;
    
    @Required
    @ManyToOne
    public DutyCategory Category;
    
    public Integer Students;
    
    public Integer Assistants;
    
    public Boolean Grants;
    
    public Duty(String name, DutyCategory category) {
        this.Name = name;
        this.Category = category;
    }
    
    public static List<Duty> getByCategory(String category){
        List<Duty> result = Duty.find("select distinct d from Duty d join " +
            "d.Category c where c.Name = ? order by d.Name", category).fetch();
        return result;
    }
    
    public static Duty findByNameAndCategory(String name, String category){
        List<Duty> result = Duty.find("select distinct d from Duty d join " +
            "d.Category c where d.Name = '" + name + 
            "' and c.Name = '" + category + "'").fetch();
        return result.get(0);
    }
    
    public static List<Duty> getForUser(Long id){
        List<Duty> result = Duty.find("select d from User u join u.duties d where u.id = " + id).fetch();
        return result;
    }
    
    public static List<Duty> getForUser(Long id, String category){
        List<Duty> result = Duty.find("select d from User u join u.duties d where u.id = " + id 
          + " and d.Category.Name = '" + category + "'").fetch();
        return result;
    }
    
    public static List<Duty> getForUser(String email, String category){
        List<Duty> result = Duty.find("select d from User u join u.duties d where u.email = '" + email 
          + "' and d.Category.Name = '" + category + "'").fetch();
        return result;
    }
    
    public static boolean userHasDuty(Long id, String name, String category){
        List<Duty> duties = getForUser(id);
        Duty duty = Duty.findByNameAndCategory(name, category);
        return duties.contains(duty);
    }
    
    public String toString() {
        return Name;
    }
 
}
