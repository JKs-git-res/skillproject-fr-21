package main.java.guideLines.model;

public class Profile {
	//private String userNameID;
	//private String firstName;
	private Address homeAddress;
	private Address[] destinationAddresses = new Address[3];
	
	Profile(Address home, Address... dest) {
		this.homeAddress = home;
		try {
			addDestinationAddress(dest[0]);
			addDestinationAddress(dest[1]);
			addDestinationAddress(dest[2]);
		} catch (fullDestinationsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        
       /*
        *  public void setFirstName(String firstName){
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
        
        */
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
