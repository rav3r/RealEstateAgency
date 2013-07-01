package org.rea;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(namespace = "http://rea.org/soa/types/",
		 name = "Offer",
		 propOrder = {  "id", "price", "area", "dateAdded", "houseType",
                   "agreementType", "street", "town", "description", "notes",
                   "longitude", "latitude", "owner", "tags"})
public class Offer {
    private String id_offer;
    private int price;
    private Date dateAdded;
    private int area;
    private String houseType;
    private String street;
    private String town;
    private int house_number;
    
    private String description;
    
    private float longitude;  //dlug geog
    private float latitude;  //szer geog
    
    private String owner;
    private List<String> tags = new LinkedList<String>();

    public Offer() {
        super();
    }

  public String getAgreementType() {
    return agreementType;
  }

  public void setAgreementType(String agreementType) {
    this.agreementType = agreementType;
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
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

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getTown() {
    return town;
  }

  public void setTown(String town) {
    this.town = town;
  }

  @Override
  public String toString() {
    return "Offer{" + "id=" + id + ", price=" + price + ", area=" + area + ", dateAdded=" + dateAdded + ", houseType=" + houseType + ", agreementType=" + agreementType + ", street=" + street + ", town=" + town + ", description=" + description + ", notes=" + notes + ", longitude=" + longitude + ", latitude=" + latitude + ", owner=" + owner + ", tags=" + tags + '}';
  }
  
  
  
    

    
    
}