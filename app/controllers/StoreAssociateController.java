package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.StoreAssociate;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import util.Utils;

public class StoreAssociateController extends BaseController{

	private static final String ACTIVITIES_TAG = "activities";

	public static Result getAgent(String agentId){
		   
		//user.setMobileNumber(mobileNumber);
		return generateOkTrue();
	}


	public static Result listVerifies(){
		if(!Utils.checkJsonInput(request())){
			Logger.info("Register User. Bad request data for register user "+request().body());
	    	return generateBadRequest("Bad input json" + request().body());
		}
		JsonNode jsonReq = request().body().asJson();
		String userId = jsonReq.get("userId").asText();
		   
		//user.setMobileNumber(mobileNumber);
		return generateOkTrue();
	}
	
	public static Result updateLocation(){
		if(!Utils.checkJsonInput(request())){
			Logger.info("Register User. Bad request data for register user "+request().body());
	    	return generateBadRequest("Bad input json" + request().body());
		}
		JsonNode jsonReq = request().body().asJson();
		String agentId = jsonReq.get("agentId").asText();
		String location = jsonReq.get("location").asText();
	       
		return generateOkTrue();
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
        //result.put("merchant", assoc.toJson());
        return ok("");
    }
}
