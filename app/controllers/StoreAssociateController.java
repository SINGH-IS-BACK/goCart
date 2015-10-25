package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.StoreAssociate;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import util.Utils;
import java.util.List;
import java.util.ArrayList;
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


public class StoreAssociateController extends BaseController{

	public static Result getAgent(String agentId){
		Datastore datastore = getDataStore();
        StoreAssociate storeAssociate = datastore.get(StoreAssociate.class, new ObjectId(agentId));
        if(storeAssociate == null){
            return generateBadRequest("Store associate not found");
        }
        ObjectNode result = Json.newObject();
        result.put("agent", storeAssociate.toJson());
        return ok(result);
    }
	
	public static Result verifyList(){
		if(!Utils.checkJsonInput(request())){
			Logger.info("Register User. Bad request data for register user "+request().body());
	    	return generateBadRequest("Bad input json" + request().body());
		}
		JsonNode jsonReq = request().body().asJson();
		String userId = jsonReq.get("userId").asText();
	
	    Datastore datastore = getDataStore();
        User user = datastore.get(User.class, new ObjectId(userId));
        if(user == null){
            return generateBadRequest("User not found");
        }
        Cart currentCart = user.getCurrentCart();
        user.addToPurchaseHistory(currentCart);
        user.setCurrentCart(new Cart());
        datastore.save(user);
	   
		//user.setMobileNumber(mobileNumber);
	    ObjectNode result = Json.newObject();
        result.put("user", user.toJson());
        return ok(result);
	    
	}
	
	public static Result updateLocation(String agentId){
		if(!Utils.checkJsonInput(request())){
			Logger.info("Register User. Bad request data for register user "+request().body());
	    	return generateBadRequest("Bad input json" + request().body());
		}
		JsonNode jsonReq = request().body().asJson();
		Datastore datastore = getDataStore();
        long x = Utils.safeLongFromJson(jsonReq, "x");
        long y = Utils.safeLongFromJson(jsonReq, "y");

        StoreAssociate storeAssociate = datastore.get(StoreAssociate.class, new ObjectId(agentId));
        if(storeAssociate == null){
            return generateBadRequest("Store associate not found");
        }
        storeAssociate.setCurrentLocation(new double[]{x,y});
        datastore.save(storeAssociate);

        ObjectNode result = Json.newObject();
        List<User> users = datastore.createQuery(User.class).asList();
        ArrayNode userArr = new ArrayNode(JsonNodeFactory.instance);
		for(User user : users){
			userArr.add(user.toJsonForAgent());
		}
	    result.put("users", userArr);
        return ok(result);
	}

    public static Result addAssociate(){

        if(!Utils.checkJsonInput(request())){
            Logger.info("Bad request data to add associate" + request().body());
            return generateBadRequest("Bad input json");
        }

        JsonNode jsonReq = request().body().asJson();
        String associateName = Utils.safeStringFromJson(jsonReq, "name");
        int associateRank = Utils.safeIntFromJson(jsonReq, "rank", 2);
        StoreAssociate assoc = new StoreAssociate(associateName, associateRank);
        getDataStore().save(assoc);

        ObjectNode result = Json.newObject();
        result.put("agent", assoc.toJson());
        return ok(result);
    }
}
