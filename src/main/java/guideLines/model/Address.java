package main.java.guideLines.model;

public class Address {
    private String name;
    private final String street;
    private final int streetNumber;
    private final String city;
    private final int postCode;
    private Station nearestStation;
    private FormOfTransport preferedWayOfTransport;
    
    Address(String name, String street, int streetNumber, String city, int postCode){
        this.name = name;
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.postCode = postCode;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setFormOfTransport(FormOfTransport preferedWayOfTransport){
        this.preferedWayOfTransport = preferedWayOfTransport;
    }
    public FormOfTransport getFormOfTransport(){
        return this.preferedWayOfTransport;
    }
    
    public void setNearestStation(Station nearestStation){
        this.nearestStation = nearestStation;
    }
    
    public String getFullAddress(){
        return street + " " + streetNumber + "\n" + postCode + " " + city;
    }
    public String getStreet(){
        return this.street;
    }
    public int getStreetNumber(){
        return streetNumber;
    }
    public String getCity(){
        return this.city;
    }
    public int getPostCode(){
       return this.postCode;
    }
    

}
