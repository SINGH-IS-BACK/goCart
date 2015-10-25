package model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;


@Entity("Carts")
public class Cart {

    @Id
    private ObjectId id;

    private List<Product> products;
    private List<Integer> productsQuantity;
    private String type; //0 - self, 1 - agent
    private boolean paid;

    public Cart(){
        products = new ArrayList<>();
        productsQuantity = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addToCart(Product product, int quantity){
        products.add(product);
        productsQuantity.add(quantity);
    }

    public long getTotalAmount() {
        long totalAmount = 0;
        for(int i = 0; i < products.size(); i++){
            totalAmount = totalAmount + (products.get(i).getPrice() * productsQuantity.get(i));
        }
        return totalAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public JsonNode toJson() {
        ObjectNode result = Json.newObject();
        result.put("id", getId().toString());
        ArrayNode productArr = new ArrayNode(JsonNodeFactory.instance);
        for (Product product : getProducts()) {
            productArr.add(product.toJson());
        }
        result.put("products", productArr);
        result.put("totalAmount", getTotalAmount());
        result.put("type", getType());
        result.put("paid", isPaid());
        return result;
    }

}
