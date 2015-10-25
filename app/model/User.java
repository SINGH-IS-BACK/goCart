package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("Users")
public class User {

	@Id
    private ObjectId id;
	
	private String name;
	private String city;
	
	public User(String name, String city){
		this.name = name;
		this.city = city;
	}
}
