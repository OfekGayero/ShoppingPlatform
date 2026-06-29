package ShoppingPlatform;

public class ShoppingCart implements Cloneable{
    private ItemList itemList;
    private String date;
    public ShoppingCart(ShoppingCart shoppingCart) {
        itemList = new ItemList(shoppingCart.getItemList().getList());
        date=shoppingCart.date;
    }
    public ShoppingCart() {
        this.itemList = new ItemList();
        date = "order not finished ";
    }
    public int getItemAmount() throws PaymentEmptyCartException{
        if (itemList.getListLen() == 0){
            throw new PaymentEmptyCartException();
        }
        return itemList.getListLen();
    }
    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    protected ShoppingCart clone() throws CloneNotSupportedException{
        ShoppingCart cloneCart = (ShoppingCart) (super.clone());
        ItemList cloneList = itemList.clone();
        cloneCart.setItemList(cloneList);
        cloneCart.date="order not finished";
        return cloneCart ;
    }
    @Override
    public String toString() {
        return  "\nItem List: " + itemList.toString()  +" Date:"+ date;
    }
}
