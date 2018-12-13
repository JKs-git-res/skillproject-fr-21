package main.java.guideLines.model;

import java.io.IOException;

import main.java.exceptions.NoFormOfTransportException;

public class Address {
    private String name;
    private final String street;
    private final String city;
    private final String locationId;
    private int houseNumber = -1;
    private String postCode = null;
    private Station nearestStation;
    
    public Address(String street, String city, String locationId) {
    	this.city = city;
    	this.street = street;
    	this.locationId = locationId;
    	setStation();
    }
    
   
    
    public Address(String street, String city, String locationId, int houseNumber) {
    	this.street = street;
    	this.city = city;
    	this.houseNumber = houseNumber;
    	this.locationId = locationId;
    	setStation();
    }
    
    public Address(String street, String city, String locationId, int houseNumber, String postCode) {
    	this.street = street;
    	this.city = city;
    	this.houseNumber = houseNumber;
    	this.postCode = postCode;
    	this.locationId = locationId;
    	setStation();
    }
    
    public Address(String street, String city, String locationId, String postCode) {
    	this.street = street;
    	this.city = city;
    	this.postCode = postCode;
    	this.locationId = locationId;
    	setStation();
    }
    
    public Station getStation() {
    		return this.nearestStation;
    }
    
    private void setStation() {
    	try {
			nearestStation = new NearbyStationFinder().findNearestStation(this);
		} catch (IOException | NoFormOfTransportException e) {
              // TODO Auto-generated catch block

		}
      // TODO Auto-generated catch block
      
    }
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
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
