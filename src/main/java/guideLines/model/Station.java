package main.java.guideLines.model;

public class Station {
    private final String name;
    private final FormOfTransport typeOfStation;
    
    public Station(String name, FormOfTransport typeOfStation) {
        this.name = name;
        this.typeOfStation = typeOfStation;
    }
    
    public String getName(){
        return this.name;
    }
    
    public FormOfTransport getTypeOfStation(){
        return this.typeOfStation;
    }
    
}
