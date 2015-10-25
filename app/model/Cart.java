package model;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("Carts")
public class Cart {

	@Id
    private ObjectId id;

	private List<Product> products;
	private long totalAmount;
}
