package models;

import java.util.*;
import java.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class DutyCategory extends Model {

    @Required
	public String name;

    public DutyCategory(String name) {
	this.name = name;
    }

    public String toString(){
	return name;
    }
}