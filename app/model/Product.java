package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.geo.Point;
import play.libs.Json;

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
	private String imageUrl;

    public Product(){

    }

	public Product(String name, String brand, String type, String sku, long price, Point location) {
		super();
		this.name = name;
		this.brand = brand;
		this.type = type;
		this.sku = sku;
		this.price = price;
		this.location = location;
	}

    public JsonNode toJson(){
        ObjectNode result = Json.newObject();
        result.put("id", getId());
        result.put("name", getName());
        result.put("type", getType());
        result.put("sku", getSku());
        result.put("price", getPrice());
        result.put("locLat", getLocation().getLatitude());
        result.put("locLon", getLocation().getLongitude());
        return result;
    }

    public String getId() {
        return id.toString();
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
