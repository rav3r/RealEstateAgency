package org.rea.web;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.rea.Offer;
import org.rea.Offers;

/**
 *
 * @author Zbyszek
 */

@ManagedBean
@SessionScoped
public class OffersConnect  implements Serializable{
  
  private String id;
    private int price;
    private int area;
    private Date dateAdded;
    private String houseType;
    private String agreementType;
    private String street;
    private String town;
    private String description;
    private String notes;
    
    private float longitude;
    private float latitude;
    
    private String number;
    private String owner;
    private List<String> tags = new LinkedList<String>();
    
    private List<Offer> offerList;
    private Offer offer;
    
    
    
    public void addOffer()
    {
      Offers offers = new Offers();
      System.out.println("Dodawanie oferty");
      System.out.println("Cena: " + price);
      //offers.CreateOfferAdmin("a", "a"); 
    }
    
    public void lol()
    {
      System.out.println("Lol");
    }
  
  public List<Offer> getOfferList()
  {
    Offers offers = new Offers();
    offerList = offers.getAllOffer();
    //System.out.println("OffersConnect: Rozmiar listy ofert: " + offerList.size());
    return offerList;
  }
  
  public void delete()
    {
      Offers offers = new Offers();
      System.out.println("Usuwanie oferty w OffersConnect");
      //offers.DeleteOffer(town, id, owner);
    }
  
  public void update()
  {
    Offers offers = new Offers();
    System.out.println("Update oferty w OffersConnect");
  }
  
  public void deleteOfferAdmin()
  {
    Offers offers = new Offers();
    System.out.println(offer);
    System.out.println("Delete oferty");
    System.out.println("Offer id: " + offer.getId());
    offers.DeleteOfferAdmin(offer.getId());
    System.out.println("Oferte usunieto");
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

  public Offer getOffer() {
    return offer;
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
  }

  public void setOfferList(List<Offer> offerList) {
    this.offerList = offerList;
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

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
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
  
}