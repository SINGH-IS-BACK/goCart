package controllers;

import model.User;
import play.mvc.Result;

public class TestController extends BaseController{
	
	public static Result addToDb(){
		final User elmer = new User("Rohan Benkar", "San Jose");
		getMorphia().save(elmer);
		return ok("");
	}
	
}
