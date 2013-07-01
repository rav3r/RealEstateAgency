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
    
    private List<Offer> offerList;
    private Offer offer;
    
    
    
    public void addOffer()
    {
      Offers offers = new Offers();
      System.out.println("Dodawanie oferty");
      System.out.println("Cena: " + price);
      //offers.CreateOfferAdmin("a", "a"); 
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
    System.out.println("Offer id: " + offer.getId_offer());
    offers.DeleteOfferAdmin(offer.getId_offer());
    System.out.println("Oferte usunieto");
  }
  
  
  
  
  
  public void setOfferList(LinkedList<Offer> offer)
  {
    offerList = offer;
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

  public Offer getOffer() {
    return offer;
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
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