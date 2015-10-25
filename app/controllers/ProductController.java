package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Product;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
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
        long x = Utils.safeLongFromJson(jsonReq, "x");
        long y = Utils.safeLongFromJson(jsonReq, "y");
        double[] productLocation = new double[]{ x, y};

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
        long x = Utils.safeLongFromJson(jsonReq, "x");
        long y = Utils.safeLongFromJson(jsonReq, "y");
        double[] productLocation = new double[]{ x, y};

        Product product = datastore.get(Product.class, new ObjectId(productId));
        product.setLocation(productLocation);
        datastore.save(product);

        ObjectNode result = Json.newObject();
        result.put("data", product.toJson());
        return ok(result);
    }

    public static Result getAllProducts(){
        List<Product> products = getDataStore().find(Product.class).asList();

        ObjectNode result = Json.newObject();
        ArrayNode resultArr = new ArrayNode(JsonNodeFactory.instance);
        for(Product product: products){
            resultArr.add(product.toJson());
        }
        result.put("products", resultArr);
        return ok(result);
    }

    public static Result getProductsAtLocation(long xCord, long yCord){
        List<Product> products = getDataStore().find(Product.class).field("location").near(xCord, yCord, 5).asList();
        ObjectNode result = Json.newObject();
        ArrayNode resultArr = new ArrayNode(JsonNodeFactory.instance);
        for(Product product: products){
            resultArr.add(product.toJson());
        }
        result.put("products", resultArr);
        return ok(result);
    }
}