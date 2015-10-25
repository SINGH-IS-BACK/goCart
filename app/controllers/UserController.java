package controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import util.Utils;
import model.User;
import model.Cart;
import org.bson.types.ObjectId;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.geo.Point;
import org.mongodb.morphia.geo.PointBuilder;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserController extends BaseController{

	private static final String ACTIVITIES_TAG = "activities";

    public static Result addUser(){
        if(!Utils.checkJsonInput(request())){
            Logger.info("Bad request data to add product" + request().body());
            return generateBadRequest("Bad input json");
        }
        
        JsonNode jsonReq = request().body().asJson();
        String name = Utils.safeStringFromJson(jsonReq, "name");
        int zip = Utils.safeIntFromJson(jsonReq, "zip", 0);
        long purchasingPower = Utils.safeLongFromJson(jsonReq, "pirchasingPower");
        long latitude = Utils.safeLongFromJson(jsonReq, "lat");
        long longitude = Utils.safeLongFromJson(jsonReq, "lon");
        Point currentLocation = new PointBuilder().latitude(latitude).longitude(longitude).build();
        String status = Utils.safeStringFromJson(jsonReq, "status");
        String level = Utils.safeStringFromJson(jsonReq, "level");
        String segment = Utils.safeStringFromJson(jsonReq, "segment");
        List<Cart> purchaseHistory = new ArrayList<Cart>();
        
        User user = new User(name, zip, purchaseHistory, purchasingPower, currentLocation, status, level, segment);
        getDataStore().save(user);

        ObjectNode result = Json.newObject();
        result.put("data", user.toJson());
        return ok(result);
    }

	public static Result getUser(String userId){
    	Datastore datastore = getDataStore();
        User user = datastore.get(User.class, new ObjectId(userId));
        ObjectNode result = Json.newObject();
        result.put("data", user.toJson());
        return ok(result);
    }

    public static Result addCart(){
		if(!Utils.checkJsonInput(request())){
			Logger.info("Register User. Bad request data for register user "+request().body());
	    	return generateBadRequest("Bad input json" + request().body());
		}
		JsonNode jsonReq = request().body().asJson();
		String userId = jsonReq.get("userId").asText();
		String list = jsonReq.get("list").asText();
		
		//add List<products>, amount, type)
		//return agent
	       
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
	    //update location in DB
		return generateOkTrue();
	}
	
	public static Result pay(){
		if(!Utils.checkJsonInput(request())){
			Logger.info("Register User. Bad request data for register user "+request().body());
	    	return generateBadRequest("Bad input json" + request().body());
		}
		JsonNode jsonReq = request().body().asJson();
		String userId = jsonReq.get("userId").asText();
		String token = jsonReq.get("token").asText();
		//pay
		//return agent
	       
		return generateOkTrue();
	}
	
}
