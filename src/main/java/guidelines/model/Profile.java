package guidelines.model;
 import guidelines.exceptions.FullDestinationsException;

public class Profile {
	//private String userNameID;
	//private String firstName;
	private Address homeAddress;
	private Address[] destinationAddresses = new Address[3];
	private FormOfTransport preferedWayOfTransport;
	public Profile(Address home, Address destA, Address destB, Address destC) {
		this.homeAddress = home;
		try {
			addDestinationAddress(destA);
                                                       addDestinationAddress(destB);
                                                       addDestinationAddress(destC);
                        
		} catch (FullDestinationsException e) {
                  // TODO Auto-generated catch block

		}
	}
        
        public FormOfTransport getPreferedWayOfTransport(){
            return this.preferedWayOfTransport;
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
        public Address getDestination(int i) {
            if(i <= 3)
                return this.destinationAddresses[i];
            else
                return null;
        }
        
        public void addPreferedFormOfTransport(FormOfTransport fot){
            this.preferedWayOfTransport = fot;
        }
        public void addDestinationAddress(Address newDestination) throws FullDestinationsException{
            if(destinationsFull())
                throw new FullDestinationsException();
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
         public Address findByName(String addressName){
            for(int i = 0; i<3;i++){
                if(destinationAddresses[i].getName().equals(addressName)){
                    return destinationAddresses[i];
                }
            }
            return null;
        }
        
        private boolean destinationsFull(){
           return destinationAddresses[0] != null && destinationAddresses[1] != null && destinationAddresses[2] != null;
        }
        
 }