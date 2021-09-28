package amazon;

import amazon.actors.Seller;

import java.util.List;

public class Product {

    int productId;
    String name;
    String description;
    Category category;
    Seller seller;
    double cost;

    List<Review> reviews;

}
