package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.simplify.payments.PaymentsApi;
import com.simplify.payments.PaymentsMap;
import com.simplify.payments.domain.Invoice;
import com.simplify.payments.domain.Payment;
import model.Cart;
import model.Product;
import model.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import util.Config;
import util.Utils;

public class UserController extends BaseController {

    public static Result addUser() {
        if (!Utils.checkJsonInput(request())) {
            Logger.info("Bad request data to add product" + request().body());
            return generateBadRequest("Bad input json");
        }

        JsonNode jsonReq = request().body().asJson();
        String name = Utils.safeStringFromJson(jsonReq, "name");
        String email = Utils.safeStringFromJson(jsonReq, "email");
        int zip = Utils.safeIntFromJson(jsonReq, "zip", 0);
        long purchasingPower = Utils.safeLongFromJson(jsonReq, "purchasingPower");
        long x = Utils.safeLongFromJson(jsonReq, "x");
        long y = Utils.safeLongFromJson(jsonReq, "y");
        String status = "new";
        String level = "normal";
        String segment = "normal";

        User user = new User(name, email, zip, purchasingPower, new double[]{x, y}, status, level, segment);
        getDataStore().save(user);

        ObjectNode result = Json.newObject();
        result.put("data", user.toJson());
        return ok(result);
    }

    public static Result getUser(String userId) {
        Datastore datastore = getDataStore();
        User user = datastore.get(User.class, new ObjectId(userId));
        if (user == null) {
            return generateBadRequest("User not found");
        }
        ObjectNode result = Json.newObject();
        result.put("data", user.toJson());
        return ok(result);
    }

    public static Result addCart(String userId) {

        if (!Utils.checkJsonInput(request())) {
            Logger.info("Register User. Bad request data for register user " + request().body());
            return generateBadRequest("Bad input json" + request().body());
        }

        Datastore datastore = getDataStore();
        JsonNode jsonReq = request().body().asJson();
        User user = datastore.get(User.class, new ObjectId(userId));
        if(user == null){
            return generateBadRequest("User not found");
        }

        user.setCurrentCart(new Cart());
        for (JsonNode productItem : jsonReq.withArray("products")) {
            String productId = Utils.safeStringFromJson(productItem, "productId");
            int quantity = Utils.safeIntFromJson(productItem, "quantity", 1);
            Product product = datastore.get(Product.class, new ObjectId(productId));
            user.getCurrentCart().addToCart(product, quantity);
        }
        datastore.save(user);
        return generateOkTrue();
    }

    public static Result updateLocation(String userId) {
        if (!Utils.checkJsonInput(request())) {
            Logger.info("Register User. Bad request data for register user " + request().body());
            return generateBadRequest("Bad input json" + request().body());
        }
        JsonNode jsonReq = request().body().asJson();
        Datastore datastore = getDataStore();
        long x = Utils.safeLongFromJson(jsonReq, "x");
        long y = Utils.safeLongFromJson(jsonReq, "y");
        User user = datastore.get(User.class, new ObjectId(userId));
        if(user == null){
            return generateBadRequest("User not found");
        }
        user.setCurrentLocation(new double[]{x, y});
        datastore.save(user);

        ObjectNode result = Json.newObject();
        result.put("user", user.toJson());
        return ok(result);
    }

    public static Result pay(String userId) {
        if (!Utils.checkJsonInput(request())) {
            Logger.info("Register User. Bad request data for add pay " + request().body());
            return generateBadRequest("Bad input json" + request().body());
        }
        JsonNode jsonReq = request().body().asJson();
        String token = jsonReq.get("token").asText();

        User user = getDataStore().get(User.class, new ObjectId(userId));
        if (user == null) {
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
