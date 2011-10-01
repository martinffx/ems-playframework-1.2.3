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

    public String toString() {
        return name;
    }
 
}
