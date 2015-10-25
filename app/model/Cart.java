package model;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Entity("Carts")
public class Cart {

	@Id
    private ObjectId id;

	private List<Product> products;
	private long totalAmount;
	private String type; //0 - self, 1 - agent
	private boolean paid;
	
	public void Cart(){
	    this.products = null;
	    this.totalAmount = 0;
	    this.type = "self";
	    this.paid = false;
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

   public List<Product> getProducts() {
      return products;
   }

   public void setProducts(List<Product> products) {
      this.products = products;
   }

   public long getTotalAmount() {
      return totalAmount;
   }

   public void setTotalAmount(long totalAmount) {
      this.totalAmount = totalAmount;
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
	
	public JsonNode toJson(){
       ObjectNode result = Json.newObject();
       result.put("id", getId().toString());
       ArrayNode productArr = new ArrayNode(JsonNodeFactory.instance);
		for(Product product : getProducts()){
			productArr.add(product.toJson());
		}
	    result.put("products", productArr);
       //result.put("products", getProducts.toJSON());
       result.put("totalAmout", getTotalAmount());
       result.put("type", getType());
       result.put("paid", isPaid());
       return result;
   }
	
}
