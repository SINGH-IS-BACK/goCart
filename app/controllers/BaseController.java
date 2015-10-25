package controllers;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.Config;
import views.html.index;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.MongoClient;

public class BaseController extends Controller {
	
    private static final String ERROR_TAG = "error";

	public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Status generateInternalServer(String msg){
		ObjectNode result = Json.newObject();
		result.put(ERROR_TAG, msg);
		return internalServerError(result);
	}
	
    public static Status generateBadRequest(String msg){
		ObjectNode result = Json.newObject();
		result.put(ERROR_TAG, msg);
		return badRequest(result);
	}
	
    public static Status generateUnauthorizedRequest(String msg){
		ObjectNode result = Json.newObject();
		result.put(ERROR_TAG, msg);
		return unauthorized(result);
	}
	
    public static Status generateOkTrue(){
		ObjectNode result = Json.newObject();
		result.put("ok", true);
		return ok(result);
	}
    
    public static Datastore getDataStore(){
    	
		try {
			final Morphia morphia = new Morphia();
	    	// tell Morphia where to find your classes
	    	// can be called multiple times with different packages or classes
	    	morphia.mapPackage("model");
			MongoClient mongo = new MongoClient(Config.getMongoHost(), Config.getMongoPort());
			final Datastore datastore = morphia.createDatastore(mongo, Config.getMongoDB());
	    	datastore.ensureIndexes();
	    	return datastore;
		} catch (Exception e) {
			Logger.error("Could not connect to MongoDB " +e);
		}
		return null;
    } 
}
