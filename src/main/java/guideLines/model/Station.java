package main.java.guideLines.model;

import java.util.HashMap;

public class Station {
    private final String name;
    private final HashMap<String, FormOfTransport> lines;
    
    public Station(String name, HashMap<String, FormOfTransport> lines) {
        this.name = name;
        this.lines = lines;
    }
    
    public String getName(){
        return this.name;
    }
    
    public HashMap<String, FormOfTransport> getLines () {
    	return this.lines;
    }
    
}
