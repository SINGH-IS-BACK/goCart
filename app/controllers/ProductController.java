package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Product;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.geo.Point;
import org.mongodb.morphia.geo.PointBuilder;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import util.Utils;

import java.util.List;

/**
 * Created by rbenkar on 10/24/15.
 */
public class ProductController extends BaseController{


    public static Result addProduct(){
        if(!Utils.checkJsonInput(request())){
            Logger.info("Bad request data to add product" + request().body());
            return generateBadRequest("Bad input json");
        }

        JsonNode jsonReq = request().body().asJson();
        String name = Utils.safeStringFromJson(jsonReq, "name");
        String brand = Utils.safeStringFromJson(jsonReq, "brand");
        String type = Utils.safeStringFromJson(jsonReq, "type");
        String sku = Utils.safeStringFromJson(jsonReq, "sku");
        long price = Utils.safeLongFromJson(jsonReq, "price");
        long latitude = Utils.safeLongFromJson(jsonReq, "lat");
        long longitude = Utils.safeLongFromJson(jsonReq, "lon");
        Point productLocation = new PointBuilder().latitude(latitude).longitude(longitude).build();

        Product product = new Product(name, brand, type, sku, price, productLocation);
        getDataStore().save(product);

        ObjectNode result = Json.newObject();
        result.put("data", product.toJson());
        return ok(result);
    }

    public static Result updateProductLocation(String productId){
        if(!Utils.checkJsonInput(request())){
            Logger.info("Bad request data to update product location" + request().body());
            return generateBadRequest("Bad input json");
        }

        Datastore datastore = getDataStore();
        JsonNode jsonReq = request().body().asJson();
        long latitude = Utils.safeLongFromJson(jsonReq, "lat");
        long longitude = Utils.safeLongFromJson(jsonReq, "lon");
        Point productLocation = new PointBuilder().latitude(latitude).longitude(longitude).build();

        Product product = datastore.get(Product.class, new ObjectId(productId));
        product.setLocation(productLocation);
        datastore.save(product);

        ObjectNode result = Json.newObject();
        result.put("data", product.toJson());
        return ok(result);
    }

}
