package controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import com.simplify.payments.PaymentsApi;
import com.simplify.payments.PaymentsMap;
import com.simplify.payments.domain.Invoice;
import com.simplify.payments.domain.Payment;
import com.simplify.payments.exception.*;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import util.Config;
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
        String email = Utils.safeStringFromJson(jsonReq, "email");
        int zip = Utils.safeIntFromJson(jsonReq, "zip", 0);
        long purchasingPower = Utils.safeLongFromJson(jsonReq, "purchasingPower");
        long latitude = Utils.safeLongFromJson(jsonReq, "lat");
        long longitude = Utils.safeLongFromJson(jsonReq, "lon");
        Point currentLocation = new PointBuilder().latitude(latitude).longitude(longitude).build();
        String status = Utils.safeStringFromJson(jsonReq, "status");
        String level = Utils.safeStringFromJson(jsonReq, "level");
        String segment = Utils.safeStringFromJson(jsonReq, "segment");
        List<Cart> purchaseHistory = new ArrayList<Cart>();
        
        User user = new User(name, email, zip, purchaseHistory, purchasingPower, currentLocation, status, level, segment);
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
		String userId = Utils.safeStringFromJson(jsonReq, "userId");
        String list = jsonReq.get("list").asText();
		
		//add List<products>, amount, type)
		//return agent
	       
	    Datastore datastore = getDataStore();
        long latitude = Utils.safeLongFromJson(jsonReq, "lat");
        long longitude = Utils.safeLongFromJson(jsonReq, "lon");
        Point productLocation = new PointBuilder().latitude(latitude).longitude(longitude).build();

        return generateOkTrue();
	}
	
	public static Result updateLocation(){
		if(!Utils.checkJsonInput(request())){
			Logger.info("Register User. Bad request data for register user "+request().body());
	    	return generateBadRequest("Bad input json" + request().body());
		}
		JsonNode jsonReq = request().body().asJson();
		Datastore datastore = getDataStore();
        String userId = Utils.safeStringFromJson(jsonReq, "userId");
        long latitude = Utils.safeLongFromJson(jsonReq, "lat");
        long longitude = Utils.safeLongFromJson(jsonReq, "lon");
        Point userLocation = new PointBuilder().latitude(latitude).longitude(longitude).build();

        User user = datastore.get(User.class, new ObjectId(userId));
        user.setCurrentLocation(userLocation);
        datastore.save(user);

        ObjectNode result = Json.newObject();
        result.put("user", user.toJson());
        return ok(result);
	}
	
	public static Result pay(){
		if(!Utils.checkJsonInput(request())){
			Logger.info("Register User. Bad request data for add pay "+request().body());
	    	return generateBadRequest("Bad input json" + request().body());
		}
		JsonNode jsonReq = request().body().asJson();
		String userId = jsonReq.get("userId").asText();
		String token = jsonReq.get("token").asText();

        User user = getDataStore().get(User.class, new ObjectId(userId));
        if(user == null){
            return generateBadRequest("Invalid userId");
        }

        Payment payment;
        try {
            PaymentsApi.PUBLIC_KEY = Config.getMastercardPublicKey();
            PaymentsApi.PRIVATE_KEY = Config.getMastercardPrivateKey();
            payment = Payment.create(new PaymentsMap()
                    .set("currency", "USD")
                    .set("token", token)
                    .set("amount", user.getCurrentCart().getTotalAmount() * 100)); // In cents
        } catch (Exception e) {
            Logger.info("Server error while sending payment to master card");
            return generateInternalServer("Payment not successful");
        }

        if ("APPROVED".equals(payment.get("paymentStatus"))) {
            try {
                Invoice invoice = Invoice.create(new PaymentsMap()
                    .set("currency", "USD")
                    .set("email", user.getEmail())
                    .set("items[0].amount", 5504L)
                    .set("items[0].quantity", 1L)
                    .set("name", user.getName())
                    .set("note", "Thank you for shopping with GoCart")
                    .set("suppliedDate", System.currentTimeMillis())
                );

            } catch (Exception e) {
                Logger.info("Server error while sending invoice to user");

            }
        }

        return generateOkTrue();
	}
	
}
