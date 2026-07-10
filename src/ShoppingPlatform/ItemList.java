package ShoppingPlatform;

import java.util.Arrays;

//Object to store items on sale for seller and as inherited objected for shopping cart

public class ItemList implements Cloneable {
    private Item[] list;
    private int listLen;
    public ItemList(Item[] itemList){
        for (int i = 0; i < itemList.length; i++) {
            list[i] = itemList[i];
        }
    }
    public ItemList() {
        this.list = new Item[0];
    }

    public Item[] getList() {
        return list;
    }

    public void setList(Item[] list) {
        this.list = list;
    }

    public int getListLen() {
        return listLen;
    }

    public Item[] addToList(Item newItem) {
        if (listLen == list.length) {
            if (list.length == 0) {
                list = new Item[1];
                list[listLen++] = newItem;
            } else {
                list = Arrays.copyOf(list, list.length * 2);
                list[listLen++] = newItem;
            }
        }else {
            list[listLen++]=newItem;
        }
        return list;
    }
    public Item findItem(String itemName){
        for(int i=0; i < listLen; i++){
            if (itemName.equalsIgnoreCase(list[i].getItemName())){
                return list[i];
            }
        }
        return null;
    }

    public boolean removeItem(int serial) {
        for (int i = 0; i < listLen; i++) {
            if (list[i].getSerial() == serial) {
                for (int j = i; j < listLen - 1; j++) {
                    list[j] = list[j + 1];
                }
                list[--listLen] = null;
                return true;
            }
        }
        return false;
    }
    public int getTotalPrice(){
        int sum=0;
        for (int i = 0; i < listLen; i++) {
            sum+=list[i].getPrice();
        }
        return sum;
    }
    public ItemList itemsByCatagory(Catagory catagory){
        ItemList catagoryList = new ItemList();
        for (int i = 0; i < listLen; i++) {
            if (list[i].getCatagory() == catagory){
                catagoryList.addToList(list[i]);
            }
        }
        return catagoryList;
    }
    @Override
    protected ItemList clone() throws CloneNotSupportedException{
        ItemList cloneList = (ItemList)(super.clone());
        for (int i = 0; i < listLen; i++) {
            cloneList.getList()[i].clone();
        }
        return cloneList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i< listLen;i++){
            sb.append(list[i].toString());
        }
        return sb.toString();
    }
}
