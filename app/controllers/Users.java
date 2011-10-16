package controllers;
 
import play.*;
import play.mvc.*;

@Check("admin")
@With(Secure.class)
public class Users extends CRUD {
	
	public static void listAdmin(){
	    render();
	}
}
