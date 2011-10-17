package controllers;
 
import play.*;
import play.mvc.*;
import play.data.*;
import play.data.validation.*;
import models.*;
import java.util.*;

@Check("admin")
@With(Secure.class)
public class Users extends CRUD {
	
	public static void admin(){
	    List<User> users = User.listAdminUsers();
	    render(users);
	}
	
	public static void list(){
	    List<User> users = User.listUsers();
	    render(users);
	}
	
//	public static void show(Long id) {
//        User user = User.findById(id);
//        System.out.println(user.toString());
//        //render(user);
//    }
	
	public static void save(
	    Long id,
	    @Required(message="Email address is required.") @Email(message="Valid email address required.") String email, 
        @Required(message="A password is required") String password, 
        String firstName,
        String lastName,
        Date dob,
        boolean isAdmin){
        
        User user = User.findById(id);
        
        if(validation.hasErrors()){
            render("Users/show.html", user);
        }
        
        //Add basic staff info
	    user.email = email;
	    user.password = password;
	    user.firstName = firstName;
	    user.lastName = lastName;
	    user.isAdmin = isAdmin;
	    
	    //Get selected duties to user
	    Set<Duty> duties = user.duties;
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
	    
	    //Add duties to user
	    user.duties = duties;
	    
	    //Save new details
	    user.save();
	    
	    //render updated page
	    render("Users/show.html", user);
	}
}
