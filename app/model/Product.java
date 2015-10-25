package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.geo.Point;

@Entity("Products")
public class Product {

	@Id
    private ObjectId id;
	
	private String name;
	private String brand;
	private String type;
	private String sku;
	private long price;
	private Point location;
	
	public Product(String name, String brand, String type, String sku, long price, Point location) {
		super();
		this.name = name;
		this.brand = brand;
		this.type = type;
		this.sku = sku;
		this.price = price;
		this.location = location;
	}
}
