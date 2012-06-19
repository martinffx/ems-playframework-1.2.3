package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class DutyCategory extends Model {

    @Required
    public String Name;
    
    @OneToMany
    public List<Duty> dutys;
    
    public DutyCategory(String name) {
        this.Name = name;
    }

    public String toString() {
        return Name;
    }
    
    public static List<Duty> getDutys(Long id){
        DutyCategory dutyCategory = DutyCategory.findById(id);
        return dutyCategory.dutys;
    }
}
