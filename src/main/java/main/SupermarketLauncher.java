package main;


import model.Supermarket;
import utilities.SupermarketBuilder;

public class SupermarketLauncher {

    public static void main(String[] args) {
        String supermarketFile = "src/main/resources/jambiBigJson.txt";
        Supermarket supermarket = new SupermarketBuilder(supermarketFile).initializeSupermarket().addProducts().addCustomers().create();

        supermarket.printCustomerStatistics();
        supermarket.printProductStatistics();
        supermarket.printRevenueStatistics();
    }
}
