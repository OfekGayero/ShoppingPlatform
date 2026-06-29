package ShoppingPlatform;

import java.util.Arrays;
import java.util.Date;
//Manager class to manage all logical operations of the project

public class Admin {
    private Buyer[] buyerList;
    private int buyerCount;
    private Seller[] sellerList;
    private int sellerCount;
    public Admin() {
        sellerList = new Seller[0];
        buyerList = new Buyer[0];
    }

    public Buyer[] getBuyerList() {
        return buyerList;
    }

    public void setBuyerList(Buyer[] buyerList) {
        this.buyerList = buyerList;
    }

    public int getBuyerCount() {
        return buyerCount;
    }

    public void setBuyerCount(int buyerCount) {
        this.buyerCount = buyerCount;
    }

    public Seller[] getSellerList() {
        return sellerList;
    }
    
    public Seller[] getSortedSellers(){
        Seller[] sortedSellers=new Seller[sellerCount];
        for (int i = 0; i < sellerCount; i++) {
            sortedSellers[i]=sellerList[i];
        }
        Arrays.sort(sortedSellers);
        return sortedSellers;
    }
    
    public Buyer[] getSortedBuyers(){
        Buyer[] sortedBuyer=new Buyer[buyerCount];
        for (int i = 0; i < buyerCount; i++) {
            sortedBuyer[i]=buyerList[i];
        }
        Arrays.sort(sortedBuyer);
        return sortedBuyer;
    }

    public void setSellerList(Seller[] sellerList) {
        this.sellerList = sellerList;
    }

    public int getSellerCount() {
        return sellerCount;
    }

    public void setSellerCount(int sellerCount) {
        this.sellerCount = sellerCount;
    }

    public Seller[] addSellerToArray(String name,String password) {
        Seller newSeller = new Seller(name, password);
        if (sellerCount == sellerList.length) {
            if (sellerList.length == 0) {
                sellerList = new Seller[1];
                sellerList[sellerCount++] = newSeller;
            } else {
                sellerList = Arrays.copyOf(sellerList, sellerList.length * 2);
                sellerList[sellerCount++] = newSeller;
            }
        } else {
            sellerList[sellerCount++] = newSeller;
        }
        return sellerList;
    }

    public Buyer[] addBuyerToArray(String name,String password,String streetName,String userBuildingNum,String city,String country) {
        int buildingNum=Integer.parseInt(userBuildingNum);
        Address address=new Address(streetName,buildingNum,city,country);
        Buyer newBuyer = new Buyer(name, password, address);
        if (buyerCount == buyerList.length) {
            if (buyerList.length == 0) {
                buyerList = new Buyer[1];
                buyerList[buyerCount++] = newBuyer;
            } else {
                buyerList = Arrays.copyOf(buyerList, buyerList.length * 2);
                buyerList[buyerCount++] = newBuyer;
            }
        } else {
            buyerList[buyerCount++] = newBuyer;
        }
        return buyerList;
    }

    public int isInSellers(String sellerName) {
        for (int i = 0; i < sellerCount; i++) {
            if (sellerName.equals(sellerList[i].getUserName())) {
                return i;
            }
        }
        return -1;
    }

    public int isInBuyers(String buyerName) {
        for (int i = 0; i < buyerCount; i++) {
            if (buyerName.equals(buyerList[i].getUserName())) {
                return i;
            }
        }
        return -1;
    }
    
    public void addItemtoSeller(String name,String price,Catagory catagory,String addition,int index) {
        int actualPrice=Integer.parseInt(price);
        actualPrice+=Integer.parseInt(addition);
        Item newItem = new Item(name, actualPrice, catagory);
        sellerList[index].getSellingList().addToList(newItem);
    }

    public Item findItemInSeller(String itemName,int sellerindex){
        return sellerList[sellerindex].getSellingList().findItem(itemName);
    }
    public void addItemtoBuyer(Item item,int buyerindex){
        buyerList[buyerindex].getCurrent().getItemList().addToList(item);
    }
//    public boolean payForShoppingCart(int buyerIndex){
//        if (checkEmptyCart(buyerIndex)) {
//            Date date = new Date();
//            buyerList[buyerIndex].getCurrent().setDate(date.toString());
//            buyerList[buyerIndex].addToHistory();
//            buyerList[buyerIndex].setCurrent(new ShoppingCart());
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
    public void payForShoppingCart(int buyerIndex){
            Date date = new Date();
            buyerList[buyerIndex].getCurrent().setDate(date.toString());
            buyerList[buyerIndex].addToHistory();
            buyerList[buyerIndex].setCurrent(new ShoppingCart());
    }

    public String[] catagoryList(Catagory catagory) {
        ItemList catagoryArr = new ItemList();
        String[] itemToPrint = new String[0];
        int counter = 0;
        for (int i = 0; i < sellerCount; i++) {
            catagoryArr = sellerList[i].getSellingList().itemsByCatagory(catagory);
            for (int j = 0; j < catagoryArr.getListLen(); j++) {
                if (counter == itemToPrint.length) {
                    if (itemToPrint.length == 0) {
                        itemToPrint = new String[1];
                    } else
                        itemToPrint = Arrays.copyOf(itemToPrint, itemToPrint.length +1);
                }

                itemToPrint[counter++] = catagoryArr.getList()[j].toString();

            }
        }

            return itemToPrint;
    }
    public int getTotalPrice(int buyerIndex){
        return buyerList[buyerIndex].getCurrent().getItemList().getTotalPrice();
    }
    public String printSellerList(int sellerIndex){
        return sellerList[sellerIndex].getSellingList().toString();
    }
    public String printHistory(int buyerIndex) {
        StringBuilder sb = new StringBuilder();
        int historyLen = buyerList[buyerIndex].getHistoryLen();
        for (int i = 0; i < historyLen; i++) {
            sb.append("History cart").append(i+1).append(buyerList[buyerIndex].getHistory()[i].toString()).append("\n");
        }
        return sb.toString();
    }
    public int getBuyerHistoryLen(int buyerIndex){
        return buyerList[buyerIndex].getHistoryLen();
    }
    public void cloneShoppingCart(int buyerIndex,int cartIndex){
        try {
            buyerList[buyerIndex].setCurrent(buyerList[buyerIndex].getHistory()[cartIndex].clone());
        } catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());;
        }
    }
    public boolean wrongTypeHandling(String convertToInt){
        try {
            Integer.parseInt(convertToInt);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    public boolean checkName(String name){
        return isInSellers(name) == -1 && isInBuyers(name) == -1;
    }
    public boolean checkBuyerListEmpty(){
        return buyerCount == 0;
    }
    public boolean checkSellerListEmpty(){
        return sellerCount == 0;
    }
    public boolean checkEmptyCart(int buyerIndex){
        try {
            buyerList[buyerIndex].getCurrent().getItemAmount();
            return true;
        }catch (PaymentEmptyCartException e){
            return false;
        }
    }
}
