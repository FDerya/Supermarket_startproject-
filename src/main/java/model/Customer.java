package model; /**
 * Supermarket Customer check-out and Cashier simulation
 * @author  hbo-ict@hva.nl
 */


import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Customer implements Comparable<Customer>{
    private LocalTime queuedAt;      // time of arrival at cashier
    private String zipCode;          // zip-code of the customer
    private Map<Product, Integer> itemsCart = new HashMap<>();;     // items purchased by customer
    private int actualWaitingTime;   // actual waiting time in seconds before check-out
    private int actualCheckOutTime;  // actual check-out time at cashier in seconds

    public Customer() {
    }

    public Customer(LocalTime queuedAt, String zipCode) {
        this.queuedAt = queuedAt;
        this.zipCode = zipCode;
    }

    // TODO stap 1: implement relevant overrides of equals(), hashcode(), compareTo for
    //  model classes to be able to use them in sets, maps
    //
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return Objects.equals(queuedAt, customer.queuedAt) &&
                Objects.equals(zipCode, customer.zipCode);
    }
    //
    @Override
    public int hashCode() {
        return Objects.hash(queuedAt, zipCode);
    }
    /**
     * calculate the total number of items purchased by this customer
     * @return
     */
    public int getNumberOfItems() {
        int numItems = 0;
        // TODO stap 2: Calculate the total number of items

        for (int itemCount : itemsCart.values()) {
            numItems += itemCount;
        }
        return itemsCart.values().stream().mapToInt(Integer::intValue).sum();
    }


    public void addToCart(Product product, int number) {
        // TODO stap 2: When adding a number of products to the cart,
        //  the number should be adjusted when product already exists in cart

        boolean productExistsInCart = itemsCart.containsKey(product);

        // If the product already exists in the cart, adjust the quantity
        if (productExistsInCart) {
            itemsCart.merge(product, number, Integer::sum);
        } else {
            // If the product is not in the cart, add a new item
            itemsCart.put(product, number);
        }
    }


    public double calculateTotalBill() {
        double totalBill = 0.0;
        // TODO stap 4: Calculate the total cost of all items, use a stream

        // Using stream to calculate the total cost of all items
        return itemsCart.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }


    public String toString() {
        StringBuilder result = new StringBuilder("queuedAt: " + queuedAt);
        result.append("\nzipCode: " + zipCode);
        result.append("\nPurchases:" );
        for (Product product : itemsCart.keySet()) {
            result.append("\n\t" + product + ": " + itemsCart.get(product));
        }
        result.append("\n");
        return result.toString();
    }


    public LocalTime getQueuedAt() {
        return queuedAt;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Map<Product, Integer> getItemsCart() {
        return itemsCart;
    }

    //
    @Override
    public int compareTo(Customer otherCustomer) {
        // compareTo method
        return this.queuedAt.compareTo(otherCustomer.queuedAt);
    }

}

