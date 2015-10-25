package model;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Entity("Carts")
public class Cart {

	@Id
    private ObjectId id;

	private List<Product> products;
	private long totalAmount;
	private String type; //0 - self, 1 - agent
	
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

	
	public JsonNode toJson(){
       ObjectNode result = Json.newObject();
       result.put("id", getId().toString());
       //result.put("products", getProducts.toJSON());
       result.put("totalAmout", getTotalAmount());
       result.put("type", getType());
       return result;
   }
	
}
