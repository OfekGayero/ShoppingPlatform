package ShoppingPlatform;

import java.util.Arrays;

public class Buyer extends User implements Comparable<Buyer> {
    private Address address;
    private ShoppingCart[] history;
    private int historyLen;
    private ShoppingCart current;
    public Buyer(String userName, String password, Address address) {
        super(userName,password);
        this.address = address;
        history = new  ShoppingCart[0];
        current = new ShoppingCart();
    }
    public ShoppingCart[] getHistory() {
        return history;
    }

    public void setHistory(ShoppingCart[] history) {
        this.history = history;
    }
    public ShoppingCart[] addToHistory() {
        if (historyLen == history.length) {
            if (history.length == 0) {
                history = new ShoppingCart[1];
                history[historyLen++] = current;
            } else {
                history = Arrays.copyOf(history, history.length * 2);
                history[historyLen++] = current;
            }
        }else {
            history[historyLen++]=current;
        }
        return history;
    }
    public int getHistoryLen() {
        return historyLen;
    }
    public ShoppingCart getCurrent() {
        return current;
    }

    public void setCurrent(ShoppingCart current) {
        this.current = current;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(address).append("\n").append("shopping history:\n");
        for(int i=0; i < historyLen; i++){
            sb.append(history[i].toString()).append("\n");
        }
        sb.append("current shopping cart:").append(current.toString());
        return sb.toString();
    }
    @Override
    public int compareTo(Buyer b) {
        return this.getUserName().compareTo(b.getUserName());
    }
}
