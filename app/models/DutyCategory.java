package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class DutyCategory extends Model {

    @Required
    public String name;
    
    @OneToMany
    public List<Duty> dutys;
    
    public DutyCategory(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
    
    public static List<Duty> getDutys(Long id){
        DutyCategory dutyCategory = DutyCategory.findById(id);
        return dutyCategory.dutys;
    }
}
