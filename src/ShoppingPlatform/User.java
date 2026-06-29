package ShoppingPlatform;

public abstract class User {
    private String userName;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int compareTo(User other){
        return this.userName.compareTo(other.userName);
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Username:").append(userName).append('\n').append("Password:").append(password).append('\n');
        return sb.toString();
    }
}
