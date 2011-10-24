package controllers;
 
import models.*;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String username, String password) {
      session.put("username", username);
      return User.connect(username, password) != null;
    }
    
    static boolean check(String profile) {
	    if("admin".equals(profile)) {
	        User user = User.find("byEmail", connected()).<User>first();
	        if(user != null){
	            return user.isAdmin;
	        }
	    }
	    return false;
	}
	
	static void onDisconnected() {
    Application.index();
	}
	
	static void onAuthenticated() {
		Application.home();
	}
}
