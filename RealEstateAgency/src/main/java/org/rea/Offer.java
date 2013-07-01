package org.rea;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(namespace = "http://rea.org/soa/types/",
		 name = "Offer",
		 propOrder = {  "id_offer", "price", "area", "dateAdded", "houseType",
                   "street", "town", "house_number", "description", "longitude", "latitude", 
                   "owner"})
public class Offer {
    private String id_offer;
    private int price;
    private Date dateAdded;
    private int area;
    private String houseType;
    private String street;
    private String town;
    private int house_number;
    private float longitude;  //dlug geog
    private float latitude;  //szer geog
    private String description;
    private String owner;

    public Offer() {
        super();
    }

  @Override
  public String toString() {
    return "Offer{" + "id_offer=" + id_offer + ", price=" + price + ", dateAdded=" + dateAdded + ", area=" + area + ", houseType=" + houseType + ", street=" + street + ", town=" + town + ", house_number=" + house_number + ", longitude=" + longitude + ", latitude=" + latitude + ", description=" + description + ", owner=" + owner + '}';
  }

  public int getArea() {
    return area;
  }

  public void setArea(int area) {
    this.area = area;
  }

  public Date getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(Date dateAdded) {
    this.dateAdded = dateAdded;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getHouseType() {
    return houseType;
  }

  public void setHouseType(String houseType) {
    this.houseType = houseType;
  }

  public int getHouse_number() {
    return house_number;
  }

  public void setHouse_number(int house_number) {
    this.house_number = house_number;
  }

  public String getId_offer() {
    return id_offer;
  }

  public void setId_offer(String id_offer) {
    this.id_offer = id_offer;
  }

  public float getLatitude() {
    return latitude;
  }

  public void setLatitude(float latitude) {
    this.latitude = latitude;
  }

  public float getLongitude() {
    return longitude;
  }

  public void setLongitude(float longitude) {
    this.longitude = longitude;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getTown() {
    return town;
  }

  public void setTown(String town) {
    this.town = town;
  }
    

  
    

    
    
}