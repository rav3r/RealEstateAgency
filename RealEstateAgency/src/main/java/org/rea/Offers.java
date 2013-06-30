package org.rea;

import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Zbyszek
 */
@WebService(serviceName = "Offers")
@Stateless()

public class Offers {
    Connection  con = null;
    Statement   st  = null;
    ResultSet   rs  = null;
      
    /**
     * Create new offer
     */
    @WebMethod(operationName = "CreateOffer")
    public Offer CreateOffer(   @WebParam(name = "login") String login,
                                @WebParam(name = "sessionId") String sessionId ) {
        Offer offer = new Offer();
        
        offer.setStreet("default");
        offer.setLatitude(0);
        offer.setLongitude(0);
        offer.setTown("default");
        offer.setDateAdded(new Date());
        
        List<String> tags = new LinkedList<String>();
        tags.add("piekny");
        tags.add("nowoczesny");
        offer.setTags(tags);
        
        String query= "INSERT INTO oferty(cena) VALUES ('200');";
        sqlExecuteStatement(query);
        
        return offer;
    }
    
    /**
     * Delete offer
     */
    @WebMethod(operationName = "DeleteOffer")
    public boolean DeleteOffer( @WebParam(name = "login")       String login,
                                @WebParam(name = "sessionId")   String sessionId,
                                @WebParam(name = "offerId")   String offerId ) {
        return true;
    }
    
    /**
     * Update offer
     */
    @WebMethod(operationName = "UpdateOffer")
    public boolean UpdateOffer( @WebParam(name = "login")       String login,
                                @WebParam(name = "sessionId")   String sessionId,
                                @WebParam(name = "offer")       Offer offer ) {
        return true;
    }
    
    @WebMethod(operationName = "GetAllOffers")
    public List<Offer> getAllOffer()
    {
      String query = "SELECT * FROM oferty";
      ResultSet rs = sqlExecuteStatement(query);
      List<Offer> offerList = new LinkedList<Offer>();
      try 
      {
        while(rs.next())
        {
          Offer offer = new Offer();
                
          offer.setPrice(rs.getInt("cena"));
          offer.setArea(rs.getInt("powierzchnia"));
          offer.setDescription(rs.getString("opis"));
          offer.setNotes(rs.getString("uwagi"));
                
          int houseType = rs.getInt("id_typu_domu");
          query = "SELECT typ_domu FROM typy_domow WHERE id_typu_domu=" + houseType;
          ResultSet rsHouse = sqlExecuteStatement(query);
          System.out.println("Po wykonaniu SELECT na typy_domow");
          rsHouse.next(); //tu test
          offer.setHouseType(rsHouse.getString("typ_domu"));
          System.out.println("Typ domu: " + offer.getHouseType());
               
          int agreementType = rs.getInt("id_typu_umowy");
          query = "SELECT typ_umowy FROM typy_umow WHERE id_typu_umowy=" + agreementType;
          ResultSet rsAgg = sqlExecuteStatement(query);
          rsAgg.next();
          System.out.println("Po wykonaniu SELECT na typy umow");
          offer.setAgreementType(rsAgg.getString("typ_umowy"));
          System.out.println("Typ umowy: " + offer.getAgreementType());
              
          int adresInt = rs.getInt("id_adresu");
          query = "SELECT miasto, ulica FROM adres WHERE id_adresu=" + adresInt;
          ResultSet rsAdres = sqlExecuteStatement(query);
          rsAdres.next();
          offer.setStreet(rsAdres.getString("ulica"));
          offer.setTown(rsAdres.getString("miasto"));
          System.out.println("Miasto: " + offer.getTown());
          System.out.println("Ulica: " + offer.getStreet());
             
          int idOferty = rs.getInt("id_oferty");
          List<String> tagList = new LinkedList<String>();
          query = "SELECT id_tagu FROM tagoferta WHERE id_oferty=" + idOferty;
          ResultSet rsTagi = sqlExecuteStatement(query);
          while (rsTagi.next())
          {
            query = "SELECT tresc FROM tagi WHERE id_tagu=" + rsTagi.getInt("id_tagu");
            ResultSet rsTag = sqlExecuteStatement(query);
            rsTag.next();
            tagList.add(rsTag.getString("tresc"));
          }
          offer.setTags(tagList);
                
          offerList.add(offer);
          }
      }
      catch (SQLException ex)
      {
        Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
      }
      try
      {
        if (rs  != null) rs.close();
        if (st  != null) st.close();
        if (con != null) con.close();
      }
      catch (SQLException ex)
      {
        System.out.println("Blad zamykania polaczenia");
        System.out.println(ex.getMessage());
        System.out.println(ex.getErrorCode());
      }
      System.out.println("Rozmiar listy ofert: " + offerList.size());
      
      return offerList;
    }
    
    /**
     * Get offers by tag
     */
    @WebMethod(operationName = "GetOffersByTag")
    public List<Offer> GetOffersByTag( @WebParam(name = "tag") String tag) {
        
        List<Offer> offers = new LinkedList<Offer>();
        
        Offer offer = new Offer();
        offer.setId("pierwsza");
        offers.add(offer);
        offer = new Offer();
        offer.setId("druga");
        offers.add(offer);
        
        return offers;
    }
    
    /**
     * Get tags
     */
    @WebMethod(operationName = "GetTags")
    public List<String> GetTags() {
        

        List<String> tags = new LinkedList<String>();
        
        tags.add("fajny");
        tags.add("duzy");
        tags.add("przestronny");
        
        return tags;
    }
    
    
    //TODO - bool i komunikat na stronie, ze zapisano/nie zapisano to cos
    private ResultSet sqlExecuteStatement(String query)
    {

     
      try
      {
          con = DriverManager.getConnection(  PostgresConfig.url,
                                              PostgresConfig.user,
                                              PostgresConfig.password);
          st = con.createStatement();
          System.out.println("Offer query: " + query);
          rs = st.executeQuery(query);
          System.out.println("Query wykonano");
      }
      catch (SQLException e)
      {
          System.out.println("Blad wykonania SQL");
          System.out.println(e.getMessage());
          System.out.println(e.getErrorCode());
      }
      finally
      {
        
      }
      return rs;
    }
}