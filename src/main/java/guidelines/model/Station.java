package guidelines.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.HashMap;
import java.util.Objects;

public class Station {

  private String name;
  private HashMap<String, FormOfTransport> lines;
  private String id;
  private String city;
  private double latitude;
  private double longitude;

  /**
   * Station object. It represents a station from Here API.
   *
   * @param name Name of the station
   * @param id Station id from Here API
   * @param lines A HashMap with all the public transportation names and the
   * form of transport defined in {@link FormOfTransport}
   */
  public Station(String name, String id, String city, HashMap<String, FormOfTransport> lines, double latitude, double longitude) {
    this.name = name;
    this.lines = lines;
    this.id = id;
    this.city = city;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Station(){
      super();
  }
  @JsonGetter
  public String getName() {
    return this.name;
  }
  @JsonGetter
  public HashMap<String, FormOfTransport> getLines() {
    return this.lines;
  }
  @JsonGetter
  public String getId() {
    return id;
  }
  @JsonGetter
  public String getCity() {
    return city;
  }

  @JsonGetter
  public double getLatitude() {
	  return this.latitude;
  }

  @JsonGetter
  public double getLongitude() {
	  return this.longitude;
  }


  @JsonSetter
  public void setName(String name){ this.name = name;}
  @JsonSetter
  public void setLines(HashMap<String,FormOfTransport> lines){this.lines=lines;}
  @JsonSetter
  public void setId(String id){this.id = id;}
  @JsonSetter
  public void setCity(String city){this.city = city;}
  @JsonSetter
  public void setLongitude(double longitude){this.longitude = longitude;}
  @JsonSetter
  public void setLatitude(double latitude){this.latitude = latitude;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station station = (Station) o;
        return Double.compare(station.getLatitude(), getLatitude()) == 0 &&
                Double.compare(station.getLongitude(), getLongitude()) == 0 &&
                Objects.equals(getName(), station.getName()) &&
                Objects.equals(getLines(), station.getLines()) &&
                Objects.equals(getId(), station.getId()) &&
                Objects.equals(getCity(), station.getCity());
    }

}
