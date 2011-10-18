package controllers;
 
import play.*;
import play.mvc.*;
import play.data.*;
import play.data.validation.*;

import models.*;

import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@Check("admin")
@With(Secure.class)
public class Users extends CRUD {
	
	public static void admin(){
	    List<User> users = User.listAdminUsers();
	    render(users);
	}
	
    public static void blankAdmin(){
        render("Users/blankAdmin.html");
    }
    
    public static void showAdmin(Long id){
        User object = User.findById(id);
        render("Users/showAdmin.html", object);
    }
    
    public static void createAdmin(){
        String email = params.get("object.email");
	    String password = params.get("object.password");
	    String firstName = params.get("object.firstName");
	    String lastName = params.get("object.lastName");
	    
        validation.required(email);
        validation.email(email);
        validation.required(password);
        
        if(validation.hasErrors()){
            params.flash();
            validation.keep();
            redirect("/admin/users/admin/new");
        }
        
        User user = new User(email, password, firstName, lastName, true);
        user.save();
        
        if (params.get("_save") != null) {
            System.out.println("save");
            redirect("/admin/users");
        } else if (params.get("_saveAndContinue") != null) {
            System.out.println("saveAndContinue");
            redirect("/admin/users/admin/" + user.id);
        }
        System.out.println("saveAndAdd");
        redirect("/admin/users/admin/new");
    }
	
	public static void save(Long id){
	    DateFormat df = DateFormat.getDateInstance();
	    //Get parameters from form
	    String email = params.get("object.email");
	    String password = params.get("object.password");
	    String firstName = params.get("object.firstName");
	    String lastName = params.get("object.lastName");
	    
	    Date dob = new Date();
	    try{
	       dob = df.parse(params.get("object.dob"));
	    } catch(ParseException pe){
	        System.out.println(pe.toString());
	    }
	    
	    
        //Validate data
        validation.required(email);
        validation.email(email);
        validation.required(password);
        
        //Fetch user
        User object = User.findById(id);
        
        //Handle validation errors
        if(validation.hasErrors()){
            System.out.println("Danger!! High Voltage!");
            params.flash();
            validation.keep();
            redirect("/admin/users/staff/" + id);
        }
        
        //Add basic staff info
	    object.email = email;
	    object.password = password;
	    object.firstName = firstName;
	    object.lastName = lastName;
	    object.dob = dob;
	    
	    //Get selected duties to user
	    Set<Duty> duties = object.duties;
	    System.out.println(duties.size());
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
	    //Add duties to user
	    object.duties = duties;
	    
	    //Save new details
	    object.save();
	    
	    //render updated page
	    if (params.get("_save") != null) {
            redirect("/admin/users");
        }
	    redirect("/admin/users/staff/" + id);
	}
	
	public static void saveAdmin(Long id){
	    //Get parameters from form
	    String email = params.get("object.email");
	    String password = params.get("object.password");
	    String firstName = params.get("object.firstName");
	    String lastName = params.get("object.lastName");
	    
	    //Validate data
        validation.required(email);
        validation.email(email);
        validation.required(password);
        
        //Fetch user
        User object = User.findById(id);
        
        //Handle validation errors
        if(validation.hasErrors()){
            System.out.println("Danger!! High Voltage!");
            params.flash();
            validation.keep();
            redirect("/admin/users/staff/" + id);
        }
        
        //Add basic staff info
	    object.email = email;
	    object.password = password;
	    object.firstName = firstName;
	    object.lastName = lastName;
	    object.isAdmin = true;
	    
	    object.save();
	    
	    if (params.get("_save") != null) {
            redirect("/admin/users");
        }
        redirect("/admin/users/admin/" + id);
	}
}
