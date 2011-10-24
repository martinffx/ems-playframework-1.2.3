package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import javax.persistence.*;

import models.*;

@With(Secure.class)
public class Times extends CRUD {
  
  public static void list(String category, String email, int page, String search, String searchFields, String orderBy, 
    String order) {
    if(email == null){
      email = session.get("username");
    }
    List<Time> objects = Time.listBycategoryAndUser(category, email);
    Long count = Long.valueOf(objects.size());
    System.out.println("Render Time values");
    render("Times/list.html", objects, count, category);
      
  }
  
//  public static void tmp(){
//    ObjectType type = ObjectType.get(getControllerClass());
//    notFoundIfNull(type);
//    if (page < 1) {
//        page = 1;
//    }
//    List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, (String) request.args.get("where"));
//    Long count = type.count(search, searchFields, (String) request.args.get("where"));
//    Long totalCount = type.count(null, null, (String) request.args.get("where"));
//    try {
//        render(type, objects, count, totalCount, page, orderBy, order);
//    } catch (TemplateNotFoundException e) {
//        render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
//    }
//  }
}
