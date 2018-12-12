package main.java.guideLines.model;

import java.util.HashMap;

public class Station {

  private final String name;
  private final HashMap<String, FormOfTransport> lines;
  private final String id;
  private final String city;

  /**
   * Station object. It represents a station from Here API.
   *
   * @param name Name of the station
   * @param id Station id from Here API
   * @param lines A HashMap with all the public transportation names and the
   * form of transport defined in {@link FormOfTransport}
   */
  public Station(String name, String id, String city, HashMap<String, FormOfTransport> lines) {
    this.name = name;
    this.lines = lines;
    this.id = id;
    this.city = city;
  }

  public String getName() {
    return this.name;
  }

  public HashMap<String, FormOfTransport> getLines() {
    return this.lines;
  }

  public String getId() {
    return id;
  }

  public String getCity() {
    return city;
  }

}
