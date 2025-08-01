package model;

import java.util.Objects;

/**
 * Supermarket Customer check-out
 * @author  hbo-ict@hva.nl
 */

public class Product implements Comparable<Product> {
    private String code;            // a unique product code; identical codes designate identical products
    private String description;     // the product description, useful for reporting
    private double price;           // the product's price

    public Product() {
    }

    public Product(String code, String description, double price) {
        this.code = code;
        this.description = description;
        this.price = price;
    }

    // TODO Stap 1: implement relevant overrides of equals(), hashcode(), compareTo for
    //  model classes to be able to use them in sets, maps
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return Objects.equals(code, product.code);
    }
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public int compareTo(Product otherProduct) {
        // Implement comparison based on your sorting criteria
        return this.code.compareTo(otherProduct.code);
    }


    public String toString() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }


}


