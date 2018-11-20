package main.java.guideLines.model;

public class Profile {
	private String userNameID;
	private String firstName;
	private Address homeAddress;
	private Address[] destinationAddresses = new Address[3];
	
	Profile(String firstName, String userNameID) {
		this.firstName = firstName;
		this.userNameID = userNameID;
	}
        
        public void setFirstName(String firstName){
            this.firstName = firstName;
        }
        public String getFirstName(){
            return this.firstName;
        }
        public void setUserNameID(String userNameID){
            this.userNameID = userNameID;
        }
        public String getUserNameID(){
            return this.userNameID;
        }
        public void setHomeAddress(Address homeAddress){
            this.homeAddress = homeAddress;
        }
        public Address getHomeAddress(){
            return this.homeAddress;
        }
        public void addDestinationAddress(Address newDestination) throws fullDestinationsException{
            if(destinationsFull())
                throw new fullDestinationsException();
            else {
                for(int i = 0; i<3 ; i++){
                    if(destinationAddresses[i] == null){
                        destinationAddresses[i] = newDestination;
                        break;
                    }
                }
                               
            }
            
        }
        public void removeDestinationAddress(Address toRemove){
            for(int i = 0; i<3 ; i++)
                destinationAddresses[i] = destinationAddresses[i].equals(toRemove)? null : destinationAddresses[i];
        }
        
        private boolean destinationsFull(){
           return destinationAddresses[0] != null && destinationAddresses[1] != null && destinationAddresses[2] != null;
        }
        

}
