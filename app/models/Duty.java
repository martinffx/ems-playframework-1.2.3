package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Duty extends Model {

	@Required
	public String name;
	  
    public Duty(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
 
}