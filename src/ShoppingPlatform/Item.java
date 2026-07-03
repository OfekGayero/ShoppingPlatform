package ShoppingPlatform;
// object that represents item sold in shop
public class Item implements Cloneable {
    private String itemName;
    private double price;
    private static int serialGenerator = 0;
    private int serial;
    private Catagory catagory;
    public Item(String itemName,double price,Catagory catagory) {
        this.price = price;
        this.itemName = itemName;
        this.catagory= catagory;
        this.serial = ++serialGenerator;
    }
    public Catagory getCatagory() {
        return catagory;
    }
    public void setCatagory(Catagory catagory) {
        this.catagory = catagory;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    @Override
    protected Item clone() throws CloneNotSupportedException{
        return (Item) (super.clone());
    }
    @Override
    public String toString() {
        return  "\nItem Name:'" + itemName + '\'' + ", Price:" + price +" Serial Number: "+serial+" Catagory: "+catagory;
    }
}
