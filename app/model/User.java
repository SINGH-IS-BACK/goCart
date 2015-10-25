package model;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.geo.Point;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Entity("Users")
public class User {

	@Id
    private ObjectId id;
	
	private String name;
    private String email;
	private int zip;
	private List<Cart> purchaseHistory;
	private long purchasingPower;
	private Point currentLocation;
	private Cart currentCart;
	private String status;
	private String level; //0 - VVIP
	private String segment; //male/ female/ age combination

    public User(){

    }

	public User(String name, String email, int zip, List<Cart> purchaseHistory, long purchasingPower, Point currentLocation, String status, String level, String segment) {
		super();
		this.name = name;
        this.email = email;
		this.zip = zip;
		this.purchaseHistory = purchaseHistory;
		this.purchasingPower = purchasingPower;
		this.currentLocation = currentLocation;
		this.status = status;
		this.level = level;
		this.segment = segment;
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public List<Cart> getPurchaseHistory() {
		return purchaseHistory;
	}

	public void setPurchaseHistory(List<Cart> purchaseHistory) {
		this.purchaseHistory = purchaseHistory;
	}

	public long getPurchasingPower() {
		return purchasingPower;
	}

	public void setPurchasingPower(long purchasingPower) {
		this.purchasingPower = purchasingPower;
	}

	public Point getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Point currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Cart getCurrentCart() {
		return currentCart;
	}

	public void setCurrentCart(Cart currentCart) {
		this.currentCart = currentCart;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addToPurchaseHistory(Cart cart){
        getPurchaseHistory().add(cart);
    }

    public JsonNode toJsonForAgent(){
       ObjectNode result = Json.newObject();
       result.put("id", getId().toString());
       result.put("name", getName());
       result.put("zip", getZip());
       result.put("locLat", getCurrentLocation().getLatitude());
       result.put("locLon", getCurrentLocation().getLongitude());
       result.put("status", getStatus());
       result.put("level", getLevel());
       result.put("segment", getSegment());
       return result;
    }

	public JsonNode toJson(){
       ObjectNode result = Json.newObject();
       result.put("id", getId().toString());
       result.put("name", getName());
       result.put("zip", getZip());
       ArrayNode cartArr = new ArrayNode(JsonNodeFactory.instance);
		for(Cart cart : getPurchaseHistory()){
			cartArr.add(cart.toJson());
		}
	    result.put("purchaseHistory", cartArr);
       result.put("locLat", getCurrentLocation().getLatitude());
       result.put("locLon", getCurrentLocation().getLongitude());
       result.put("status", getStatus());
       result.put("level", getLevel());
       result.put("segment", getSegment());
       return result;
   }
}
