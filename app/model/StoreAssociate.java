package model;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.geo.Point;

@Entity("StoreAssociates")
public class StoreAssociate {
	
	@Id
    private ObjectId id;
	
	private String name;
	private Point currentLocation;
	private List<User> queuedUsers;
	private int level;
	
	public StoreAssociate(String name, Point currentLocation, List<User> queuedUsers, int level) {
		super();
		this.name = name;
		this.currentLocation = currentLocation;
		this.queuedUsers = queuedUsers;
		this.level = level;
	}
	
	
	
}
