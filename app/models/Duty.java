package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Duty extends Model {

    @Required
    public String name;
    
    @Required
    @ManyToOne
    public DutyCategory Category;
    
    public Integer Students;
    
    public Integer Assistants;
    
    public Boolean Grants;
    
    public Duty(String name, DutyCategory category) {
        this.name = name;
        this.Category = category;
    }
    
    public static List<Duty> getByCategory(String category){
        List<Duty> result = Duty.find("select distinct d from Duty d join " +
            "d.Category c where c.name = ? order by d.name", category).fetch();
        return result;
    }
    
    public static Duty findByNameAndCategory(String name, String category){
        List<Duty> result = Duty.find("select distinct d from Duty d join " +
            "d.Category c where d.name = '" + name + 
            "' and c.name = '" + category + "'").fetch();
        return result.get(0);
    }
    
    public static List<Duty> getForUser(Long id){
        List<Duty> result = Duty.find("select d from User u join u.duties d where u = " + id).fetch();
        return result;
    }
    
    public static boolean userHasDuty(Long id, String name, String category){
        List<Duty> duties = getForUser(id);
        Duty duty = Duty.findByNameAndCategory(name, category);
        return duties.contains(duty);
    }
    public String toString() {
        return name;
    }
 
}
