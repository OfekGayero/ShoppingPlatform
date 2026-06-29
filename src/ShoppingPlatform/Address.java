package ShoppingPlatform;
// A class to store address of buyer
public class Address {
    private String streetName;
    private int buildingNum;
    private String city;
    private String country;

    public Address(String streetName, int buildingNum, String city, String country) {
        this.streetName = streetName;
        this.buildingNum = buildingNum;
        this.city = city;
        this.country = country;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(int buildingNum) {
        this.buildingNum = buildingNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Address:");
        sb.append("street name: ").append(streetName).append('\n');
        sb.append("building number: ").append(buildingNum).append('\n');
        sb.append("city: ").append(city).append('\n');
        sb.append("country: ").append(country).append('\n');
        return sb.toString();
    }
}
