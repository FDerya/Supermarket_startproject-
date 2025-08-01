package model;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Supermarket Customer and purchase statistics
 * @author  hbo-ict@hva.nl
 */
public class Supermarket {

    private String name;                 // name of the case for reporting purposes
    private Set<Product> products;      // a set of products that is being sold in the supermarket
    private Set<Customer> customers;   // a set of customers that have visited the supermarket
    private LocalTime openTime;         // start time of the simulation
    private LocalTime closingTime;      // end time of the simulation
    private static final int INTERVAL_IN_MINUTES = 15; // to use for number of customers and revenues per 15 minute intervals

    public Supermarket() {
        initializeCollections();
    }

    public Supermarket(String name, LocalTime openTime, LocalTime closingTime) {
        this.name = name;
        this.setOpenTime(openTime);
        this.setClosingTime(closingTime);
        initializeCollections();
    }

    public void initializeCollections() {
        // TODO stap 3: create empty data structures for products and customers
        this.products = new HashSet<>();
        this.customers = new HashSet<>();

    }

    public int getTotalNumberOfItems() {
        int totalItems = 0;
        // TODO stap 3: calculate the total number of shopped items of all customers
        if (checkSetupErrorProductCustomers()) {
            printErrorMessage();
            return totalItems;
        }

        for (Customer customer : customers) {
            totalItems += customer.getNumberOfItems();
        }

        return totalItems;
    }

    private void printErrorMessage() {
        System.out.println("No products or customers have been set up...");
    }

    private boolean checkSetupErrorProductCustomers() {
        return this.customers == null || this.products == null ||
                this.customers.size() == 0 || this.products.size() == 0;
    }

    /**
     * report statistics of the input data of customer
     */
    public void printCustomerStatistics() {
        if (checkSetupErrorProductCustomers()) {
            printErrorMessage();
            return;
        }
        System.out.printf("\n>>>>> Customer Statistics of '%s' between %s and %s <<<<<\n",
                this.name, this.openTime, this.closingTime);
        System.out.printf("%d customers have shopped %d items out of %d different products\n",
                this.customers.size(), this.getTotalNumberOfItems(), this.products.size());
        System.out.println();
        // TODO stap 4: calculate and show the customer(s) with the highest bill and most paying customer
        System.out.printf("Customer that has the highest bill of %.2f euro: \n", findHighestBill());
        System.out.println(findMostPayingCustomer());
    }

    /**
     * report statistics of data of products
     */
    public void printProductStatistics() {
        if (checkSetupErrorProductCustomers()) {
            printErrorMessage();
            return;
        }
        System.out.println(">>>>> Product Statistics of all purchases <<<<<");

        System.out.println();

        Map<Product, Integer> productsBoughtMap = findNumberOfProductsBought();

        List<Map.Entry<Product, Integer>> sortedEntries = new ArrayList<>(productsBoughtMap.entrySet());
        Collections.sort(sortedEntries, Comparator.comparing(entry -> entry.getKey().getDescription()));


        int maxDescriptionLength = sortedEntries.stream()
                .mapToInt(entry -> entry.getKey().getDescription().length())
                .max()
                .orElse(0);

        String format = "%-" + (maxDescriptionLength + 5) + "s %d\n";


        System.out.println(">>> Products and total number bought:");
        for (Map.Entry<Product, Integer> entry : sortedEntries) {
            String productDescription = entry.getKey().getDescription();
            int totalBought = entry.getValue();
            System.out.printf(format, productDescription, totalBought);
        }
        System.out.println();

        System.out.println(">>> Products and zipcodes");
        // TODO stap 3: display the description of the products and per product all zipcodes where the product is bought.

        Map<Product, Set<String>> productZipCodesMap = findZipcodesPerProduct();

        for (Map.Entry<Product, Set<String>> entry : productZipCodesMap.entrySet()) {
            Product product = entry.getKey();
            Set<String> zipCodes = entry.getValue();

            System.out.println(product.getDescription() + ":");

            StringBuilder indentation = new StringBuilder("    ");
            StringBuilder zipCodeLine = new StringBuilder();
            for (String zipCode : zipCodes) {

                if (zipCodeLine.length() + zipCode.length() + 2 > 76) { // 76 = 80 (line width) - 4 (indentation width)
                    System.out.println(indentation + zipCodeLine.toString());
                    zipCodeLine = new StringBuilder();
                }
                zipCodeLine.append(zipCode).append(", ");
            }
            if (zipCodeLine.length() > 0) {
                System.out.println(indentation + zipCodeLine.substring(0, zipCodeLine.length() - 2));
            }

        }


        System.out.println(">>> Most popular products");
        System.out.println();
        // TODO stap 5: display the product(s) that most customers bought
        Set<Product> mostPopularProducts = findMostPopularProducts();
        System.out.println("Product(s) bought by most customers: ");
        mostPopularProducts.forEach(product -> System.out.println("\t" + product.getDescription()));
        System.out.println();
        System.out.println(">>> Most bought products per zipcode");
        System.out.println();
        // TODO stap 5: display most bought products per zipcode
        Map<String, Product> mostBoughtProductByZipcode = findMostBoughtProductByZipcode();
        mostBoughtProductByZipcode.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String zipcode = entry.getKey();
                    Product product = entry.getValue();
                    System.out.println(zipcode + " " + product.getDescription());
                });

        System.out.println();
    }

    /**
     * report statistics of the input data of customer
     */
    public void printRevenueStatistics() {
        System.out.println("\n>>>>> Revenue Statistics of all purchases <<<<<");
        // TODO stap 5: calculate and show the total revenue and the average revenue
        System.out.printf("\nTotal revenue = %.2f\nAverage revenue per customer = %.2f\n", findTotalRevenue(), findAverageRevenue());
        System.out.println();
        System.out.print(">>> Revenues per zip-code:\n");
        System.out.println();
        // TODO stap 5 calculate and show total revenues per zipcode, use forEach and lambda expression
        Map<String, Double> revenues = this.getRevenueByZipcode();
        revenues.forEach((zipCode, revenue) -> System.out.printf("%s: %.2f\n", zipCode, revenue));

        System.out.println();
        System.out.printf(">>> Revenues per interval of %d minutes\n", INTERVAL_IN_MINUTES);
        System.out.println();
        // TODO stap 5: show the revenues per time interval of 15 minutes, use forEach and lambda expression
        Map<LocalTime, Double> revenuesByInterval = calculateRevenuePerInterval(INTERVAL_IN_MINUTES);
        List<Map.Entry<LocalTime, Double>> sortedEntries = new ArrayList<>(revenuesByInterval.entrySet());
        Collections.sort(sortedEntries, Comparator.comparing(Map.Entry::getKey));

// Iterate over the sorted time intervals and print the corresponding revenue
        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<LocalTime, Double> currentEntry = sortedEntries.get(i);
            LocalTime startTime = currentEntry.getKey();
            Double revenue = currentEntry.getValue();


            LocalTime endTime;
            if (i == sortedEntries.size() - 1) {

                endTime = closingTime;
            } else {

                endTime = sortedEntries.get(i + 1).getKey();
            }

            System.out.printf("Between %s and %s the revenue was %.2f\n", startTime, endTime, revenue);
        }


    }


    /**
     * @return Map with total number of purchases per product
     */
    public Map<Product, Integer> findNumberOfProductsBought() {
        // TODO stap 3: create an appropriate data structure for the products and their numbers bought
        //  and calculate the contents
// Create a map to store the total number of purchases per product
        Map<Product, Integer> productsBoughtMap = new HashMap<>();

        for (Customer customer : customers) {

            Map<Product, Integer> itemsCart = customer.getItemsCart();


            for (Product product : itemsCart.keySet()) {

                if (productsBoughtMap.containsKey(product)) {

                    int currentCount = productsBoughtMap.get(product);
                    int newCount = currentCount + itemsCart.get(product);
                    productsBoughtMap.put(product, newCount);
                } else {

                    productsBoughtMap.put(product, itemsCart.get(product));
                }
            }
        }

        return productsBoughtMap;
    }
    /**
     * builds a map of products and set of zipcodes where product has been bought
     * @return Map with set of zipcodes per product
     */
    public Map<Product, Set<String>> findZipcodesPerProduct() {
        // TODO stap 3: create an appropriate data structure for the products and their zipcodes
        //  and find all the zipcodes per product

        Map<Product, Set<String>> productZipCodesMap = new HashMap<>();

        // Iterate through each customer
        for (Customer customer : customers) {

            Map<Product, Integer> itemsCart = customer.getItemsCart();

            for (Product product : itemsCart.keySet()) {
                if (productZipCodesMap.containsKey(product)) {

                    productZipCodesMap.get(product).add(customer.getZipCode());
                } else {

                    Set<String> zipcodes = new HashSet<>();
                    zipcodes.add(customer.getZipCode());
                    productZipCodesMap.put(product, zipcodes);
                }
            }
        }

        return productZipCodesMap;
    }

    /**
     * builds a map of zipcodes with maps of products with number bougth
     * @return Map with map of product and number per zipcode
     */
    public Map<String, Map<Product, Integer>> findNumberOfProductsByZipcode() {
        // TODO stap 3: create an appropriate data structure for zipcodes
        //  and the collection of products with numbers bought
        //  find all the product per zipcode and the number bought by a customer with the zipcode
        Map<String, Map<Product, Integer>> productsByZipcodeMap = new HashMap<>();

        // Iterate through each customer
        for (Customer customer : customers) {
            String zipcode = customer.getZipCode();

            if (!productsByZipcodeMap.containsKey(zipcode)) {
                productsByZipcodeMap.put(zipcode, new HashMap<>());
            }
            Map<Product, Integer> productsBoughtMap = productsByZipcodeMap.get(zipcode);
            for (Product product : customer.getItemsCart().keySet()) {
                // Check  map for this zipcode
                if (productsBoughtMap.containsKey(product)) {

                    int currentCount = productsBoughtMap.get(product);
                    int newCount = currentCount + customer.getItemsCart().get(product);
                    productsBoughtMap.put(product, newCount);
                } else {

                    productsBoughtMap.put(product, customer.getItemsCart().get(product));
                }
            }
        }
        return  productsByZipcodeMap;
    }

    /**
     *
     * @return value of the highest bill
     */
    public double findHighestBill() {
        // TODO stap 4: use a stream to find the highest bill
        return customers.stream()
                .mapToDouble(Customer::calculateTotalBill )
                .max()
                .orElse(0.0);
    }

    /**
     *
     * @return customer with highest bill
     */
    public Customer findMostPayingCustomer() {
        // TODO stap 4: use a stream and the highest bill to find the most paying customer

        return customers.stream()
                .max(Comparator.comparingDouble(Customer::calculateTotalBill))
                .orElse(null);//Ik stel de standaardwaarde in op nul als er geen klanten zijn

    }

    /**
     * calculates the total revenue of all customers purchases
     * @return total revenue
     */
    public double findTotalRevenue() {
        // TODO Stap 5: use a stream to find the total of all bills

        return customers.stream()
                .mapToDouble(Customer::calculateTotalBill)
                .sum();
    }

    /**
     * calculates the average revenue of all customers purchases
     * @return average revenue
     */
    public double findAverageRevenue() {
        // TODO Stap 5: use a stream to find the average of the bills
        return customers.stream()
                .mapToDouble(Customer::calculateTotalBill)
                .average()
                .orElse(0.0);
    }

    /**
     * calculates a map of aggregated revenues per zip code that is also ordered by zip code
     * @return Map with revenues per zip code
     */
    public Map<String, Double> getRevenueByZipcode() {
        // TODO Stap 5: create an appropriate data structure for the revenue
        //  use stream and collector to find the content
        return customers.stream()
                .collect(Collectors.groupingBy(Customer::getZipCode,
                        TreeMap::new,
                        Collectors.summingDouble(customer -> customer.getItemsCart().entrySet().stream()
                                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                                .sum())));
    }


    /**
     * finds the product(s) found in the most carts of customers
     * @return Set with products bought by most customers
     */
    public Set<Product> findMostPopularProducts() {
        // TODO Stap 5: create an appropriate data structure for the most popular products and find its contents
        Map<Product, Long> productCountMap = customers.stream()
                .flatMap(customer -> customer.getItemsCart().keySet().stream())
                .collect(Collectors.groupingBy(
                        product -> product,
                        Collectors.counting()
                ));

        // Bepaal dan de hoogste waarde met behulp van een stream en haal het product op met die hoogste waarde
        long maxCount = productCountMap.values().stream()
                .max(Long::compare)
                .orElse(0L);

        // Filter de producten die het meest voorkomen
        Set<Product> mostPopularProducts = productCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return mostPopularProducts;
    }


    /**
     *
     * calculates a map of most bought products per zip code that is also ordered by zip code
     * if multiple products have the same maximum count, just pick one.
     * @return Map with most bought product per zip code
     */
    public Map<String, Product> findMostBoughtProductByZipcode() {
        // TODO Stap 5: create an appropriate data structure for the mostBought
        //  and calculate its contents
        Map<String, Map<Product, Integer>> productsByZipcodeMap = findNumberOfProductsByZipcode();
// Maak een map om de meest gekochte producten per postcode op te slaan
        Map<String, Product> mostBoughtProductByZipcode = new HashMap<>();

        //  postcode
        for (Map.Entry<String, Map<Product, Integer>> entry : productsByZipcodeMap.entrySet()) {
            String zipcode = entry.getKey();
            Map<Product, Integer> productCountMap = entry.getValue();

            // Bepaal het product met het hoogste aantal
            Product mostBoughtProduct = productCountMap.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            // Voeg het gevonden product toe aan de map
            if (mostBoughtProduct != null) {
                mostBoughtProductByZipcode.put(zipcode, mostBoughtProduct);
            }
        }

        return mostBoughtProductByZipcode;
    }


    /**
     *
     * calculates a map of revenues per time interval based on the length of the interval in minutes
     * @return Map with revenues per interval
     */
    public Map<LocalTime, Double> calculateRevenuePerInterval(int minutes) {
        // TODO Stap 5: create an appropiate data structure for the revenue per time interval
        //  Start time of an interval is a key. Find the total revenue for each interval

        Map<LocalTime, Double> revenuePerInterval = new HashMap<>();

        // Calculate the total time for the supermarket

        int totalMinutes = (int) openTime.until(closingTime, java.time.temporal.ChronoUnit.MINUTES);

        // Calculate the number of intervals
        int numIntervals = totalMinutes / minutes;

        LocalTime startTime = openTime;
        for (int i = 0; i < numIntervals; i++) {
            LocalTime endTime = startTime.plusMinutes(minutes);
            revenuePerInterval.put(startTime, 0.0);
            startTime = endTime;
        }

        // Iterate through each customer and update the revenue for the corresponding interval
        customers.forEach(customer -> {
            double totalBill = customer.calculateTotalBill();
            LocalTime purchaseTime = customer.getQueuedAt(); // Adjust based on actual return type
            LocalTime intervalStartTime = findIntervalStartTime(purchaseTime, minutes);
            double currentRevenue = revenuePerInterval.get(intervalStartTime);
            revenuePerInterval.put(intervalStartTime, currentRevenue + totalBill);
        });

        return revenuePerInterval;
    }

    // Helper method to find the start time of the interval containing a given time
    private LocalTime findIntervalStartTime(LocalTime time, int minutes) {
        int minuteOfDay = time.getHour() * 60 + time.getMinute();
        int intervalStart = (minuteOfDay / minutes) * minutes;
        return LocalTime.of(intervalStart / 60, intervalStart % 60);
    }



    public Set<Product> getProducts() {
        return products;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }


}

