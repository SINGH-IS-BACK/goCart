package model;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.geo.Point;

@Entity("Users")
public class User {

	@Id
    private ObjectId id;
	
	private String name;
	private int zip;
	private List<Cart> purchaseHistory;
	private long purchasingPower;
	private Point currentLocation;
	private Cart currentCart;
	
	public User(String name, int zip) {
		super();
		this.name = name;
		this.zip = zip;
	}
}
