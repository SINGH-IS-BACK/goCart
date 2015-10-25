package model;

import java.util.ArrayList;
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

@Entity("StoreAssociates")
public class StoreAssociate {
	
	@Id
    private ObjectId id;
	
	private String name;
	private Point currentLocation;
	private List<User> queuedUsers;
	private int level;
	
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

	public Point getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Point currentLocation) {
		this.currentLocation = currentLocation;
	}

	public List<User> getQueuedUsers() {
		return queuedUsers;
	}

	public void setQueuedUsers(List<User> queuedUsers) {
		this.queuedUsers = queuedUsers;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	
	public StoreAssociate(String name, Point currentLocation, List<User> queuedUsers, int level) {
		super();
		this.name = name;
		this.queuedUsers = new ArrayList<>();
		this.level = level;
	}
	public StoreAssociate(String name, int level) {
		super();
		this.name = name;
		this.level = level;
	}
	
	public void queueUser(User user){
        this.queuedUsers.add(user);
    }
    
    public JsonNode toJson(){
       ObjectNode result = Json.newObject();
       result.put("id", getId().toString());
       result.put("name", getName());
       ArrayNode userArr = new ArrayNode(JsonNodeFactory.instance);
		for(User user : getQueuedUsers()){
			userArr.add(user.toJsonForAgent());
		}
	   result.put("users", userArr);
       result.put("locLat", getCurrentLocation().getLatitude());
       result.put("locLon", getCurrentLocation().getLongitude());
       result.put("level", getLevel());
       return result;
   }
}
