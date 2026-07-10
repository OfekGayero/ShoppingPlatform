package ShoppingPlatform;
import ShoppingPlatform.DataAccessObject.QueryDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class Main {
    private static Scanner scan = new Scanner(System.in);
    private static Admin admin = new Admin();
    private static final QueryDAO queryDAO = new QueryDAO();

    public static void main(String[] args) {
        menu();
    }
    private static void menu() {
        String userOption="";
        int option;
        do {
            boolean checker=false;
            while (!checker) {
                System.out.print("Select Desired Option:\n" +
                        "Managment Options\n"+
                        "1 add seller\n" +
                        "2 add buyer\n" +
                        "3 add item to seller\n" +
                        "4 add item to buyer\n" +
                        "5 payment for buyer\n" +
                        "6 print all buyers data\n" +
                        "7 print all data for seller,\n" +
                        "8 print seller items by catagory\n" +
                        "9 Copy cart from Buyer History\n" +
                        "10 update item\n" +
                        "11 delete item\n" +
                        "Queries\n"+
                        "21 Show electric items\n"+
                        "22 Show average price of items from each category\n"+
                        "23 Show the user with most items\n"+
                        "24 Show shopping cart history of alice\n"+
                        "25 Show all items from cart with cid = 3\n"+
                        "26 Show all carts total price grouped by cart id\n"+
                        "27 Show dave_sells most expensive item\n"+
                        "28 Show number of carts per buyer (that has at least one cart) \n"+
                        "29 Show the item purchased the most\n"+
                        "30 Show number of orders per country\n"+
                        "0 to exit \n");
                userOption= scan.next();
                if(!admin.wrongTypeHandling(userOption)){
                    System.out.println("enter a number between 0-9!");
                }else checker=true;
            }
            option=Integer.parseInt(userOption);
            switch (option) {
                case 1: {
                    addSeller();
                    break;
                }
                case 2: {
                    addBuyer();
                    break;
                }
                case 3: {
                    addItemSeller();
                    break;
                }
                case 4: {
                    addItemBuyer();
                    break;
                }
                case 5: {
                    paymentBuyer();
                    break;
                }
                case 6: {
                    printBuyers();
                    break;
                }
                case 7: {
                    printSeller();
                    break;
                }
                case 8: {
                    printItemsByCategory();
                    break;
                }
                case 9:{
                    chooseHistoryCart();
                    break;
                }
                case 10: {
                    updateItem();
                    break;
                }
                case 11: {
                    deleteItem();
                    break;
                }
                case 21: {
                    getElectricItems();
                    break;
                }
                case 22:{
                    getAverageOfEachCategory();
                    break;
                }
                case 23:{
                    userWithMostItems();
                    break;
                }
                case 24:{
                    aliceCartHistory();
                    break;
                }
                case 25:{
                    itemsFromCartID3();
                    break;
                }
                case 26:{
                    totalPriceByCartID();
                    break;
                }
                case 27:{
                    davesMostExpensiveItem();
                    break;
                }
                case 28:{
                    cartsPerBuyer();
                    break;
                }
                case 29:{
                    mostPurchasedItem();
                    break;
                }
                case 30:{
                    ordersPerCountry();
                    break;
                }
            }
        } while (option != 0);
        scan.close();
        System.out.println("Exiting...");
    }


    // option 1
    private static void addSeller() {
        System.out.println("Enter name:");
        while (true) {
            String name = scan.next();
            if (admin.checkName(name)) {
                System.out.println("Enter password:");
                String password = scan.next();
                admin.addSeller(name, password);
                break;
            } else {
                System.out.println("Username already used. Try another.");
            }
        }
    }

    //option 2
    private static void addBuyer() {
        System.out.println("Enter name:");
        while (true) {
            String name = scan.next();
            if (admin.checkName(name)) {
                System.out.println("Enter password:");
                String password = scan.next();
                System.out.println("Enter address:");
                System.out.println("Enter street name:");
                String streetName=scan.next();
                String userBuildingNum="";
                boolean checker = false;
                while (!checker) {
                    System.out.println("Enter building number:");
                     userBuildingNum = scan.next();
                    if (!admin.wrongTypeHandling(userBuildingNum)) {
                        System.out.println("Enter a number!");
                    } else {
                        checker = true;
                    }
                }
                System.out.println("Enter city:");
                String city=scan.next();
                System.out.println("Enter country:");
                String country=scan.next();
                admin.addBuyer(name, password, streetName, userBuildingNum, city, country);
                break;
            } else {
                System.out.println("Username already used. Try another.");
            }
        }
    }

    //option 3
    private static void addItemSeller() {
        if (!admin.checkSellerListEmpty()) {
            System.out.println("Enter seller name:");
            while (true) {
                String name = scan.next();
                int index = admin.isInSellers(name);
                if (index != -1) {
                    System.out.println("Enter item name:");
                    String itemName = scan.next();
                    System.out.println("Enter item price:");
                    String price = "";
                    boolean checker = false;
                    while (!checker) {
                        price = scan.next();
                        if (!admin.wrongTypeHandling(price)) {
                            System.out.println("Enter a number!");
                        } else {
                            checker = true;
                        }
                    }
                    Catagory catagory = selectCatagory();
                    System.out.println("Special Package?\nType y for yes\nAny other key for no");
                    String choice = scan.next();
                    String addition = "0";
                    if (choice.equals("y")) {
                        System.out.println("Enter additional cost for special package");
                        checker = false;
                        while (!checker) {
                            addition = scan.next();
                            if (!admin.wrongTypeHandling(addition)) {
                                System.out.println("Enter a number!");
                            } else {
                                checker = true;
                            }
                        }
                    }
                    admin.addItemtoSeller(itemName, price, catagory, addition, index);
                    break;
                } else {
                    System.out.println("Seller doesn't exist. Enter again:");
                }
            }
        }
        else {
            System.out.println("Sellers list is empty. Select diffrent option");
        }
    }

    //option 4
    private static void addItemBuyer() {
        if (!admin.checkBuyerListEmpty()){
            System.out.println("enter buyer name:");
            while (true) {
                String buyerName = scan.next();
                int buyerIndex = admin.isInBuyers(buyerName);
                if (buyerIndex != -1) {
                    System.out.println("enter seller name:");
                    String sellerName = scan.next();
                    int sellerIndex = admin.isInSellers(sellerName);
                    if (sellerIndex != -1) {
                        System.out.println(admin.printSellerList(sellerIndex));
                        System.out.println("enter item name:");
                        String newItemName = scan.next();
                        Item found = admin.findItemInSeller(newItemName, sellerIndex);
                        if (found != null) {
                            admin.addItemtoBuyer(found, buyerIndex);
                            break;
                        } else {
                            System.out.println("item doesn't exist");
                        }
                    } else
                        System.out.println("seller doesn't exist");
                } else
                    System.out.println("buyer doesn't exist. Enter again:");
            }
        }else{
            System.out.println("Buyers list is empty. Select diffrent option");
        }
    }

    // option 5
    private static void paymentBuyer() {
        if(!admin.checkBuyerListEmpty())
        {
                System.out.println("enter buyer to pay:");
                while (true) {
                    String buyerName = scan.next();
                    int buyerIndex = admin.isInBuyers(buyerName);
                    if (buyerIndex != -1) {
                        if (admin.checkEmptyCart(buyerIndex)){
                            System.out.println("total price is: " + admin.getTotalPrice(buyerIndex));
                            admin.payForShoppingCart(buyerIndex);
                        }
                        else {
                            System.out.println("Shopping cart is empty.Returning to main menu");
                        }
                        break;
                    } else
                        System.out.println("Buyer doesn't exist. Enter again:");
                }
        }else {
            System.out.println("Buyers list is empty. Select diffrent option");
        }
    }
    // option 6
    //Showcases all buyers sorted
    private static void printBuyers() {
          int buyerCount=admin.getBuyerCount();
   Buyer[] buyers = admin.getSortedBuyers();
   for (int i = 0; i < buyerCount; i++) {
       System.out.println(buyers[i].toString());
       int buyerIndex = admin.isInBuyers(buyers[i].getUserName());
       System.out.println(admin.printHistory(buyerIndex));
       System.out.println();
   }
}

    //Showcases all buyers sorted
    //option 7
    private static void printSeller() {
        int sellerCount=admin.getSellerCount();
        for (int i = 0; i < sellerCount; i++) {
            System.out.println(admin.getSortedSellers()[i].toString());
            System.out.println();
        }
    }

    // option 8
    private static void printItemsByCategory() {
        Catagory catagory = selectCatagory();
        String[] items = admin.catagoryList(catagory);
        for (int i = 0; i < items.length; i++) {
            System.out.println(items[i]);
        }
    }
    // option 9
    private static void chooseHistoryCart() {
        if(!admin.checkBuyerListEmpty())
        {
            while (true) {
                System.out.println("enter buyer name:");
                String buyerName = scan.next();
                String userCartIndex = "";
                int cartindex;
                int buyerIndex = admin.isInBuyers(buyerName);
                if (buyerIndex != -1) {
                    int historylen = admin.getBuyerHistoryLen(buyerIndex);
                    if (historylen > 0) {
                        System.out.println((admin.printHistory(buyerIndex)));
                        System.out.println("Replace current cart from history? y for yes,any other key to cancel");
                        String choice = scan.next();
                        if (choice.equals("y")) {
                            System.out.println("choose a cart from history");
                            while (true) {
                                boolean checker = false;
                                while (!checker) {
                                    userCartIndex = scan.next();
                                    if (!admin.wrongTypeHandling(userCartIndex)) {
                                        System.out.println("enter a number between 1-" + historylen);
                                    } else {
                                        checker = true;
                                    }
                                }
                                cartindex = Integer.parseInt(userCartIndex) - 1;
                                if (cartindex >= 0 && cartindex < historylen)
                                    break;
                                else {
                                    System.out.println("enter a number between 1-" + historylen);
                                }
                            }
                            admin.cloneShoppingCart(buyerIndex, cartindex);
                            break;
                        }else {
                            System.out.println("Returning to main menu");
                            break;
                        }
                    }
                    else {
                        System.out.println("buyer's history is empty. Returning to main menu");
                        break;
                    }
                }else {
                    System.out.println("Buyer doesn't exist.");
                }
            }
        }else {
             System.out.println("Buyers list is empty. Select diffrent option");
        }
    }
    // option 10
    private static void updateItem() {
        if (admin.checkSellerListEmpty()) {
            System.out.println("Sellers list is empty. Select different option");
            return;
        }
        System.out.println("Enter seller name:");
        while (true) {
            String sellerName = scan.next();
            int sellerIndex = admin.isInSellers(sellerName);
            if (sellerIndex != -1) {
                System.out.println(admin.printSellerList(sellerIndex));
                System.out.println("Enter item name to update:");
                String itemName = scan.next();
                System.out.println("Enter new item name:");
                String newName = scan.next();
                System.out.println("Enter new price:");
                String newPrice = "";
                boolean checker = false;
                while (!checker) {
                    newPrice = scan.next();
                    if (!admin.wrongTypeHandling(newPrice)) {
                        System.out.println("Enter a number!");
                    } else {
                        checker = true;
                    }
                }
                admin.updateItem(sellerIndex, itemName, newName, newPrice);
                break;
            } else {
                System.out.println("Seller doesn't exist. Enter again:");
            }
        }
    }

    // option 11
    private static void deleteItem() {
        if (admin.checkSellerListEmpty()) {
            System.out.println("Sellers list is empty. Select different option");
            return;
        }
        System.out.println("Enter seller name:");
        while (true) {
            String sellerName = scan.next();
            int sellerIndex = admin.isInSellers(sellerName);
            if (sellerIndex != -1) {
                System.out.println(admin.printSellerList(sellerIndex));
                System.out.println("Enter item name to delete:");
                scan.nextLine();
                String itemName = scan.nextLine();
                admin.deleteItem(sellerIndex, itemName);
                break;
            } else {
                System.out.println("Seller doesn't exist. Enter again:");
            }
        }
    }

    private static Catagory selectCatagory() {
        Catagory catagory = Catagory.OTHER;
        boolean outerchecker=false;
        do  {
            String userSelect = "";
            boolean innerchecker = false;
            while (!innerchecker) {
                System.out.println("choose category:\n" +
                        "1 for child \n" + "2 for electric \n" + "3 for office\n" + "4 for clothing");
                userSelect = scan.next();
                if (!admin.wrongTypeHandling(userSelect)) {
                    System.out.println("enter a number between 1-4!");
                } else {
                    innerchecker = true;
                }
            }
            int select = Integer.parseInt(userSelect);
            switch (select) {
                case 1: {
                    catagory = Catagory.CHILD;
                    outerchecker = true;
                    break;
                }
                case 2: {
                    catagory = Catagory.ELECTRIC;
                    outerchecker = true;
                    break;
                }
                case 3: {
                    catagory = Catagory.OFFICE;
                    outerchecker = true;
                    break;
                }
                case 4: {
                    catagory = Catagory.CLOTHING;
                    outerchecker = true;
                    break;
                }
                default:
                    System.out.println("enter a number between 1-4!");
            }
        }while (!outerchecker);
        return catagory;
    }

    //Queries
    //Option 21
    private static void getElectricItems() {
        StringBuilder sb = new StringBuilder();
        try {
            ResultSet rs = queryDAO.getElectricItems();
            while (rs.next()) {
                sb.append("Serial: ").append(rs.getInt("SERIAL_NUMBER"))
                        .append(", Name: ").append(rs.getString("ITEM_NAME"))
                        .append(", Price: $").append(rs.getDouble("PRICE"))
                        .append("\n");
            }
            System.out.println(sb);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    // Option 22 - Average price per category
    private static void getAverageOfEachCategory() {
        try {
            ResultSet rs = queryDAO.getAverageOfEachCategory();
            while (rs.next()) {
                System.out.println("Category: " + rs.getString("CATEGORY") +
                                   ", Average Price: $" + rs.getDouble("average_price"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Option 23 - User with most items
    private static void userWithMostItems() {
        try {
            ResultSet rs = queryDAO.userWithMostItems();
            if (rs.next()) {
                System.out.println("User: " + rs.getString("USERNAME") +
                                   ", Items: " + rs.getInt("items_count"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Option 24 - Alice's cart history
    private static void aliceCartHistory() {
        try {
            ResultSet rs = queryDAO.aliceCartHistory();
            while (rs.next()) {
                System.out.println("Cart ID: " + rs.getInt("CID") +
                                   ", Date: " + rs.getString("CART_DATE"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Option 25 - Items from cart ID 3
    private static void itemsFromCartID3() {
        try {
            ResultSet rs = queryDAO.itemsFromCartID3();
            while (rs.next()) {
                System.out.println("Name: " + rs.getString("ITEM_NAME") +
                                   ", Price: $" + rs.getDouble("PRICE") +
                                   ", Category: " + rs.getString("CATEGORY"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Option 26 - Total price by cart ID
    private static void totalPriceByCartID() {
        try {
            ResultSet rs = queryDAO.totalPriceByCartID();
            while (rs.next()) {
                System.out.println("Cart ID: " + rs.getInt("CID") +
                                   ", Total: $" + rs.getDouble("total_cart_price"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Option 27 - Dave's most expensive item
    private static void davesMostExpensiveItem() {
        try {
            ResultSet rs = queryDAO.davesMostExpensiveItem();
            if (rs.next()) {
                System.out.println("Item: " + rs.getString("ITEM_NAME") +
                                   ", Price: $" + rs.getDouble("PRICE"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Option 28 - Buyers with more than one cart
    private static void cartsPerBuyer() {
        try {
            ResultSet rs = queryDAO.cartsPerBuyer();
            while (rs.next()) {
                System.out.println("User: " + rs.getString("USERNAME") +
                                  ", Orders: " + rs.getInt("order_count"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Option 29 - Most purchased item
    private static void mostPurchasedItem() {
        try {
            ResultSet rs = queryDAO.mostPurchasedItem();
            if (rs.next()) {
                System.out.println("Item: " + rs.getString("ITEM_NAME") +
                               ", Times Purchased: " + rs.getInt("times_purchased"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Option 30 - Orders per country
    private static void ordersPerCountry() {
        try {
            ResultSet rs = queryDAO.ordersPerCountry();
            while (rs.next()) {
                System.out.println("Country: " + rs.getString("COUNTRY") +
                               ", Total Orders: " + rs.getInt("total_orders"));
                }
            } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}