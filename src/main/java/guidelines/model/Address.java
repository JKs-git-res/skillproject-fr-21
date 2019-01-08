package guidelines.model;

import java.io.IOException;

import com.fasterxml.jackson.annotation.*;
import guidelines.OutputStrings;
import guidelines.exceptions.NoFormOfTransportException;

@JsonPropertyOrder(value ={"name","street","houseNumber","postCode","city"},alphabetic = true)
public class Address {
    private String name;
    private String street;
    private String city;
    private String locationId;
    private int houseNumber = -1;
    private String postCode = null;
    private Station nearestStation;

    public Address(){
        super();
    }
    
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
    @JsonGetter
    public Station getNearestStation() {
    		return this.nearestStation;
    }

    @JsonGetter
    public String getName(){
        return this.name;
    }

    @JsonGetter
    public String getStreet(){
        return this.street;
    }

    @JsonGetter
    public String getCity(){
        return this.city;
    }

    @JsonGetter
    public String getPostCode(){
        return this.postCode;
    }

    @JsonGetter
    public int gethouseNumber(){
        return houseNumber;
    }

    @JsonGetter
    public String getLocationId(){ return this.locationId; }

    @JsonSetter
    public void setNearestStation(Station nearestStation) {this.nearestStation = nearestStation; }

    @JsonSetter
    public void setName(String name){
        this.name = name;
    }

    @JsonSetter
    public void setStreet(String street){
        this.street = street;
    }

    @JsonSetter
    public void setCity(String city){
        this.city = city;
    }

    @JsonSetter
    public void setPostCode(String postCode){
        this.postCode = postCode;
    }

    @JsonSetter
    public void setHouseNumber(int houseNumber){
        this.houseNumber = houseNumber;
    }

    @JsonSetter
    public void setLocationId(String locationId){
        this.locationId = locationId;
    }


    private void setStation() {
    	try {
			nearestStation = new NearbyStationFinder().findNearestStation(this);
		} catch (IOException | NoFormOfTransportException e) {
              // TODO Auto-generated catch block

		}
      // TODO Auto-generated catch block
      
    }


    @JsonIgnoreProperties
    public String AddressSpeech(){
        return street + " "+ houseNumber + OutputStrings.SPEECH_BREAK_SHORT.toString() +" "+ city;
    }

    

    



    

}
