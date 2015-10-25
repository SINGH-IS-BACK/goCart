package controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import util.Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserController extends BaseController{

	private static final String ACTIVITIES_TAG = "activities";

	public static Result addList(){
		if(!Utils.checkJsonInput(request())){
			Logger.info("Register User. Bad request data for register user "+request().body());
	    	return generateBadRequest("Bad input json" + request().body());
		}
		JsonNode jsonReq = request().body().asJson();
		String userId = jsonReq.get("userId").asText();
		String list = jsonReq.get("list").asText();
	       
		//user.setMobileNumber(mobileNumber);
		return generateOkTrue();
	}
	
	public static Result updateLocation(){
		if(!Utils.checkJsonInput(request())){
			Logger.info("Register User. Bad request data for register user "+request().body());
	    	return generateBadRequest("Bad input json" + request().body());
		}
		JsonNode jsonReq = request().body().asJson();
		String userId = jsonReq.get("userId").asText();
		String location = jsonReq.get("location").asText();
	       
		//user.setMobileNumber(mobileNumber);
		return generateOkTrue();
	}
	
}
