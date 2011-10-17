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
	
//	public static void list(){
//	    List<User> users = User.listUsers();
//	    render(users);
//	}
//	
//	public static void show(Long id) {
//        User user = User.findById(id);
//        System.out.println(user.toString());
//        //render(user);
//    }
	
	public static void save(Long id){
	    //Get parameters from form
	    String email = params.get("object.email");
	    String password = params.get("object.password");
	    String firstName = params.get("object.firstName");
	    String lastName = params.get("object.lastName");
	    String strIsAdmin = params.get("object.isAdmin");
	    boolean isAdmin = Boolean.parseBoolean(strIsAdmin);
	    String save = params.get("_save");
	    String saveEdit = params.get("_saveAndContinue");
	    
	    Map<String, String[]> Param = params.all();
	    System.out.println(save);
	    System.out.println(saveEdit);
	    System.out.println(password);
	    System.out.println(strIsAdmin);
        
        //Validate data
//        validation.required(email);
//        validation.email(email);
//        validation.required(password);
        
        //Fetch user
        User object = User.findById(id);
        
        //Handle validation errors
        if(validation.hasErrors()){
            System.out.println("Danger!! High Voltage!");
            params.flash();
            validation.keep();
            redirect("/admin/staff/" + id);
        }
        
        //Add basic staff info
	    object.email = email;
	    object.password = password;
	    object.firstName = firstName;
	    object.lastName = lastName;
	    object.isAdmin = isAdmin;
	    
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
	    if(isAdmin){
	        if (params.get("_save") != null) {
                redirect("/admin/user");
            }
	        redirect("/admin/user/" + id);
	    }
	    
	    if (params.get("_save") != null) {
            redirect("/admin/staff");
        }
	    redirect("/admin/staff/" + id);
	}
}
