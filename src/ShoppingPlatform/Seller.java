package ShoppingPlatform;


public class Seller extends User implements Comparable<Seller>  {
    private ItemList itemList;

    public Seller(String userName,String password) {
        super(userName,password);
        itemList = new ItemList();
    }

    public ItemList getSellingList() {
        return itemList;
    }

    public void setSellingList(ItemList sellingList) {
        this.itemList = sellingList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Itemlist:").append('\n').append(itemList.toString());
        return sb.toString();
    }

    @Override
    public int compareTo(Seller other) {
        return this.itemList.getListLen() - other.itemList.getListLen();
    }
}
