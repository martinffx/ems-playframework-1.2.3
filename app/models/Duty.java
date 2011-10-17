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
    public DutyCategory category;

    public Duty(String name, DutyCategory category) {
        this.name = name;
        this.category = category;
    }
    
    public static List<Duty> getByCategory(String category){
        List<Duty> result = Duty.find("select distinct d from Duty d join " +
            "d.category c where c.name = ? order by d.name", category).fetch();
        return result;
    }
    
    public static Duty findByNameAndCategory(String name, String category){
        List<Duty> result = Duty.find("select distinct d from Duty d join " +
            "d.category c where d.name = '" + name + 
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
