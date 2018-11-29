package main.java.guideLines.model;

public class Address {
    private String name;
    private final String street;
    private final String city;
    private final String locationId;
    private int houseNumber = -1;
    private String postCode = null;
    private FormOfTransport preferedWayOfTransport;
    
    public Address(String street, String city, String locationId) {
    	this.city = city;
    	this.street = street;
    	this.locationId = locationId;
    }
    
    public Address(String street, String city, String locationId, int houseNumber) {
    	this.street = street;
    	this.city = city;
    	this.houseNumber = houseNumber;
    	this.locationId = locationId;
    }
    
    public Address(String street, String city, String locationId, int houseNumber, String postCode) {
    	this.street = street;
    	this.city = city;
    	this.houseNumber = houseNumber;
    	this.postCode = postCode;
    	this.locationId = locationId;
    }
    
    public Address(String street, String city, String locationId, String postCode) {
    	this.street = street;
    	this.city = city;
    	this.postCode = postCode;
    	this.locationId = locationId;
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
    }
    
    public String getFullAddress(){
        return street + " " + houseNumber + " " + postCode + " " + city;
    }
    
    public String getStreet(){
        return this.street;
    }
    
    public int gethouseNumber(){
        return houseNumber;
    }
    
    public String getCity(){
        return this.city;
    }
    public String getPostCode(){
       return this.postCode;
    }
    
    public String getLocationId() {
    	return this.locationId;
    }
    

}
