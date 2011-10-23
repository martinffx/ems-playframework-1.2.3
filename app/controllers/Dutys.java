package controllers;
 
import play.*;
import play.mvc.*;

import models.*;

import java.util.*;

@Check("admin")
@With(Secure.class)
public class Dutys extends CRUD {

    public static void list(){
        render();
    }
}
