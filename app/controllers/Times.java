package controllers;

import play.*;
import play.data.binding.*;
import play.mvc.*;
import play.utils.Java;
import play.db.Model;
import play.data.validation.*;
import play.exceptions.*;
import play.i18n.*;

import java.util.*;
import javax.persistence.*;

import models.*;

import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;


@With(Secure.class)
public class Times extends CRUD {
  
  public static void list(String category, String email, int page, String search, String searchFields, String orderBy, 
    String order) {
    
    if(email == null){
      email = session.get("username");
    }
    
    if (page < 1) {
        page = 1;
    }
    
    List<Time> allObjects = Time.listBycategoryAndUser(category, email);
    int start = (page - 1) * getPageSize();
    int end = start + getPageSize();
    int count = allObjects.size();
    
    List<Time> objects;
    if(count > getPageSize()){
      if (end > count){
        objects = allObjects.subList(start, (count-1));
      } else{
        objects = allObjects.subList(start, end);
      }
    } else {
      objects = allObjects;
    }
    render("Times/list.html", objects, count, category);
      
  }
  
  public static void show(String category, Long id){
    Time object = Time.findById(id);
    render("Times/show.html", object, category);
  }
  
  public static void blank(String category) throws Exception {
      String email = session.get("username");
      
      User user = User.getUserByEmail(email);
      Time object = new Time(user);
      
      render("Times/blank.html", object, category, email);
    }
  
  public static void save(Long id, String category){
    Time object = Time.findById(id);
    notFoundIfNull(object);
    Binder.bind(object, "object", params.all());
    validation.valid(object);
        
    //Handle validation errors
    if(validation.hasErrors()){
      System.out.println("Danger!! High Voltage!");
      params.flash();
      validation.keep();
      redirect("/user/times/" + category + "/" + id);
    }
    
    object._save();
	    
    //render updated page
    if (params.get("_save") != null) {
      redirect("/user/times/" + category);
    }
    redirect("/user/times/" + category + "/" + id);
  }
  
  public static void create(String category, String email){
	User user = User.getUserByEmail(email);
    Time object = new Time(user);
    Binder.bind(object, "object", params.all());
    if(email == null){
      email = session.get("username");
    }
    object.staff = user;
    Long dutyId = Long.parseLong(params.get("object.Duty"));
    object.Duty = Duty.findById(dutyId);
    validation.valid(object);
    if (validation.hasErrors()) {
      renderArgs.put("error", Messages.get("crud.hasErrors"));
      try {
        System.out.println(params.get("object.Duty"));
        System.out.println(object.Duty);
        System.out.println(object.capturedDate);
        System.out.println(object.Date);
        redirect("/user/times/" + category + "/new");
      } catch (TemplateNotFoundException e) {
        System.out.println("validation error");
        redirect("/user/times/" + category + "/" + object._key());
      }
    }
    object._save();
    //render updated page
    if (params.get("_save") != null) {
      redirect("/user/times/" + category);
    }
    if(params.get("_saveAndAddAnother") != null){
      redirect("/user/times/" + category + "/new");
    }
    redirect("/user/times/" + category + "/" + object.id);
  }
  
  public static void delete(Long id, String category) {
        Time object = Time.findById(id);
        notFoundIfNull(object);
        try {
            object._delete();
        } catch (Exception e) {
            flash.error("Error deleting this time");
            redirect("/user/times/" + category + "/" + object._key());
        }
        flash.success("Successfully deleted the time.");
        redirect("/user/times/" + category);
    }

}
