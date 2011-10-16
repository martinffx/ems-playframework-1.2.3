package controllers;
 
import play.*;
import play.mvc.*;
import models.*;
import java.util.*;

@Check("admin")
@With(Secure.class)
public class Users extends CRUD {
	
	public static void listAdmin(){
	    render();
	}
	
	public static void save(Long id){
	    User user = User.findById(id);
	    List<Duty> duties = Duty.getForUser(id);
	    System.out.println(duties.size());
	    //Fetch all duty categories to loop through
	    List<DutyCategory> dutyCategories = DutyCategory.all().fetch();
	    for(DutyCategory dc : dutyCategories){
	        //Find all the duties selected for that category
	        System.out.println("duty." + dc.toString());
	        String[] dutyCategory = params.getAll("duty." + dc.toString());
	        if(dutyCategory != null){
	            for(String s : dutyCategory){
	                System.out.println(s);
	                duties.add(Duty.findByNameAndCategory(s, dc.name));
	            }
	        }
	    }
	    System.out.println(duties.size());
//	    for(String[] param : sParams.values()){
//	        System.out.println();
//	        for(String p : param){
//	            System.out.println(p);
//	        }
//	    }
	}
}
