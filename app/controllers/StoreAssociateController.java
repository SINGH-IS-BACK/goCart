package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.StoreAssociate;
import model.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import util.Utils;

/**
 * Created by rbenkar on 10/24/15.
 */
public class StoreAssociateController extends BaseController{

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
        result.put("merchant", assoc.toJson());
        return ok("");
    }
}
