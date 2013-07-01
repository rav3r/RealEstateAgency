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
    @WebMethod(operationName = "CreateOfferAdmin")
    public Offer CreateOfferAdmin(   @WebParam(name = "login") String login,
                                     @WebParam(name = "sessionId") String sessionId,
                                     @WebParam(name = "price") int price,
                                     @WebParam(name = "area") int area,
                                     @WebParam(name = "dateAdded") String dateAdded,
                                     @WebParam(name = "houseType") String houseType,
                                     @WebParam(name = "aggrementType") String aggrementType,
                                     @WebParam(name = "street") String street,
                                     @WebParam(name = "town") String town,
                                     @WebParam(name = "description") String description,
                                     @WebParam(name = "notes") String notes,
                                     @WebParam(name = "longitude") float longitude,
                                     @WebParam(name = "latitude") float latitude,
                                     @WebParam(name = "owner") String owner,
                                     @WebParam(name = "tags") List<String> tags
                                ) {
      try{
      con = DriverManager.getConnection(  PostgresConfig.url,
                                              PostgresConfig.user,
                                              PostgresConfig.password);
      }
      catch(SQLException e){}
      String query = null;
      
      //ustalanie id typu domu
      query = "SELECT id_typu_domu FROM typy_domow WHERE typ_domu=" + houseType + ";";
      ResultSet rsTypDomu = sqlExecuteStatement(query);
      int id_typu_domu=-1;
      try {
         rsTypDomu.next();
         id_typu_domu = rs.getInt("id_typu_domu");
      } catch (SQLException ex) {}
      System.out.println("Id typu domu: " + id_typu_domu);
      
      
      
      
      
      
      
        Offer offer = new Offer();
        
        offer.setStreet("default");
        offer.setLatitude(0);
        offer.setLongitude(0);
        offer.setTown("default");
        offer.setDateAdded(new Date());
        
        List<String> tagi = new LinkedList<String>();
        tagi.add("piekny");
        tagi.add("nowoczesny");
        offer.setTags(tagi);
        
//        String query2= "INSERT INTO oferty(cena) VALUES ('200');";
//        sqlExecuteStatement(query2);
        
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
    
    
    
    /**
     * Delete offer admin
     */
    @WebMethod(operationName = "DeleteOfferAdmin")
    public boolean DeleteOfferAdmin( @WebParam(name = "offerId")   String offerId ) {
      try{
      con = DriverManager.getConnection(  PostgresConfig.url,
                                              PostgresConfig.user,
                                              PostgresConfig.password);
      }
      catch(SQLException e){}
      String id_adresuQuery = "SELECT id_adresu FROM oferty WHERE id_oferty=" + offerId + ";";
      ResultSet rs = sqlExecuteStatement(id_adresuQuery);
      String id_adres = null;
      try
      {
      rs.next();
      id_adres = rs.getString("id_adresu");
      }
      catch(SQLException e){}
      System.out.println("Id adresu: " + id_adres);
      
      String tagOfferQuery = "DELETE FROM tagoferta WHERE id_oferty=" + offerId + ";";
      String adresQuery = "DELETE FROM adres WHERE id_adresu=" + id_adres + ";";
      String ulubQuery = "DELETE FROM ulubione WHERE id_oferty=" + offerId + ";";
      String ofertyQuery = "DELETE FROM oferty WHERE id_oferty=" + offerId + ";";
      String query = tagOfferQuery + ulubQuery + ofertyQuery + adresQuery;
      sqlExecuteStatementWithoutResult(query);      
      return true;
    }
    
    
    //done
    @WebMethod(operationName = "GetAllOffers")
    public List<Offer> getAllOffer()
    {
      ResultSet rsHouse = null;
      ResultSet rsAgg = null;
      ResultSet rsAdres = null;
      ResultSet rsTag = null;
      ResultSet rsTagi = null;
      
      try{
      con = DriverManager.getConnection(  PostgresConfig.url,
                                              PostgresConfig.user,
                                              PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String query = "SELECT * FROM oferty";
      ResultSet rs = sqlExecuteStatement(query);
      List<Offer> offerList = new LinkedList<Offer>();
      try 
      {
        while(rs.next())
        {
          Offer offer = new Offer();
                
          offer.setId(rs.getString("id_oferty"));
          offer.setPrice(rs.getInt("cena"));
          offer.setArea(rs.getInt("powierzchnia"));
          offer.setDescription(rs.getString("opis"));
          offer.setNotes(rs.getString("uwagi"));
                
          int houseType = rs.getInt("id_typu_domu");
          query = "SELECT typ_domu FROM typy_domow WHERE id_typu_domu=" + houseType;
          rsHouse = sqlExecuteStatement(query);
          //System.out.println("Po wykonaniu SELECT na typy_domow");
          rsHouse.next(); //tu test
          offer.setHouseType(rsHouse.getString("typ_domu"));
          //System.out.println("Typ domu: " + offer.getHouseType());
               
          int agreementType = rs.getInt("id_typu_umowy");
          query = "SELECT typ_umowy FROM typy_umow WHERE id_typu_umowy=" + agreementType;
          rsAgg = sqlExecuteStatement(query);
          rsAgg.next();
          //System.out.println("Po wykonaniu SELECT na typy umow");
          offer.setAgreementType(rsAgg.getString("typ_umowy"));
          //System.out.println("Typ umowy: " + offer.getAgreementType());
              
          int adresInt = rs.getInt("id_adresu");
          query = "SELECT miasto, ulica FROM adres WHERE id_adresu=" + adresInt;
          rsAdres = sqlExecuteStatement(query);
          rsAdres.next();
          offer.setStreet(rsAdres.getString("ulica"));
          offer.setTown(rsAdres.getString("miasto"));
          //System.out.println("Miasto: " + offer.getTown());
          //System.out.println("Ulica: " + offer.getStreet());
             
          int idOferty = rs.getInt("id_oferty");
          List<String> tagList = new LinkedList<String>();
          query = "SELECT id_tagu FROM tagoferta WHERE id_oferty=" + idOferty;
          rsTagi = sqlExecuteStatement(query);
          while (rsTagi.next())
          {
            query = "SELECT tresc FROM tagi WHERE id_tagu=" + rsTagi.getInt("id_tagu");
            rsTag = sqlExecuteStatement(query);
            rsTag.next();
            tagList.add(rsTag.getString("tresc"));
            if (rsTag!=null) rsTag.close();
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
        if (rsHouse  != null) rsHouse.close();
        if (rsAgg  != null) rsAgg.close();
        if (rsTagi != null) rsTagi.close();
        if (rsAdres  != null) rsAdres.close();
        if (st  != null) st.close();
        if (con != null) con.close();
      }
      catch (SQLException ex)
      {
        System.out.println("Blad zamykania polaczenia");
        System.out.println(ex.getMessage());
        System.out.println(ex.getErrorCode());
      }
      //System.out.println("Rozmiar listy ofert: " + offerList.size());
      
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
    
    
    
    //done
    private ResultSet sqlExecuteStatement(String query)
    {
      try
      {
        st = con.createStatement();
        //System.out.println("Offer query: " + query);
        rs = st.executeQuery(query);
      }
      catch (SQLException ex) {
      Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
    }
    return rs;
    }
    
    //done
    private void sqlExecuteStatementWithoutResult(String query)
            {
      try
      {
        st = con.createStatement();
        st.executeQuery(query);
        System.out.println("Offer query: " + query);
      }
      catch (SQLException ex) {
      Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
}