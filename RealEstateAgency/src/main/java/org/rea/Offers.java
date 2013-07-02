package org.rea;

import java.sql.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

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
    
    
    
    
    
    //------DONE------------------------------------------------------------
    @WebMethod(operationName = "createOfferAdmin")
    public boolean createOfferAdmin( @WebParam(name = "price") int price,
                                     @WebParam(name = "area") int area,
                                     @WebParam(name = "houseType") String houseType,
                                     @WebParam(name = "street") String street,
                                     @WebParam(name = "town") String town,
                                     @WebParam(name = "house_number") int house_number,
                                     @WebParam(name = "longitude") float longitude,
                                     @WebParam(name = "latitude") float latitude,
                                     @WebParam(name = "description") String description,
                                     @WebParam(name = "owner") String owner
                                )
    {
      try
      {
        con = DriverManager.getConnection(  PostgresConfig.url,
                                            PostgresConfig.user,
                                            PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String query = null;
      
      //dodawanie adresu
      query = "INSERT INTO adres(miasto, ulica, nr_domu, dl_geog, szer_geog) VALUES ('"
              + town + "', '" + street + "', " + house_number + ", " + longitude + ", " + latitude + ");";
      System.out.println("Adres insert query: " + query);
      sqlExecuteStatementWithoutResult(query);
      
      
      //ustalanie id typu domu
      query = "SELECT id_typu_domu FROM typy_domow WHERE typ_domu='" + houseType + "';";
      ResultSet rsTypDomu = sqlExecuteStatement(query);
      int id_typu_domu=-1;
      try
      {
         rsTypDomu.next();
         id_typu_domu = rsTypDomu.getInt("id_typu_domu");
      } 
      catch (SQLException ex) {}
      System.out.println("Id typu domu: " + id_typu_domu);
      
      //ustalanie id_adresu
      query = "SELECT id_adresu FROM adres WHERE (miasto='" + town + "' AND ulica='" + 
              street + "' AND nr_domu=" + house_number + " AND dl_geog=" + longitude +
              " AND szer_geog=" + latitude + ");";
      System.out.println("Adres select query: " + query);
      ResultSet rs_adres = sqlExecuteStatement(query);
      int id_adres=-1;
      try
      {
         while(rs.next())
        {
          if (rs.getInt("id_adresu")>id_adres)
            id_adres = rs.getInt("id_adresu");
        }
      } 
      catch (SQLException ex) {}
      System.out.println("Id adresu: " + id_adres);
      
      //obecna data
//      Calendar calendar = Calendar.getInstance();
//      Date today = new Date(calendar.getTime().getTime());
              
      System.out.println("Tuz przed dodaniem oferty");
      //dodawanie oferty
      query = "INSERT INTO oferty(cena, data_dodania, powierzchnia, id_typu_domu, id_adresu, opis, wlasciciel) "
              + "VALUES (" + price + ", current_date, " + area + ", " + id_typu_domu + ", "
              + id_adres + ", '" + description + "', '" + owner + "');";
      System.out.println("Query: " + query);
      sqlExecuteStatementWithoutResult(query);
        
      return true;
    }
    
    
    
    
    //------DONE------------------------------------------------------------
    @WebMethod(operationName = "createOffer", action="createOffer")
    public boolean createOffer( @WebParam(name = "login")       String login,
                                @WebParam(name = "sessionId")   String sessionId,
                                @WebParam(name = "price") int price,
                                @WebParam(name = "area") int area,
                                @WebParam(name = "houseType") String houseType,
                                @WebParam(name = "street") String street,
                                @WebParam(name = "town") String town,
                                @WebParam(name = "house_number") int house_number,
                                @WebParam(name = "longitude") float longitude,
                                @WebParam(name = "latitude") float latitude,
                                @WebParam(name = "description") String description
                                )
    {
      try
      {
        con = DriverManager.getConnection(  PostgresConfig.url,
                                            PostgresConfig.user,
                                            PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String query = "SELECT session_id FROM users WHERE login='" + login + "';";
      ResultSet rs = sqlExecuteStatement(query);
      String ses_id = null;
      try
      {
        rs.next();
        ses_id = rs.getString("session_id");
      }
      catch(SQLException e){}
      System.out.println("Delete offer session id: " + ses_id);
      if (sessionId.equals(ses_id))
      {
        return createOfferAdmin(price, area, houseType, street, town, house_number,
                                longitude, latitude, description, login);
      }
      return false;
    }
    
    
    
    
    //------DONE------------------------------------------------------------
    @WebMethod(operationName = "deleteOfferAdmin")
    public boolean deleteOfferAdmin( @WebParam(name = "offerId") int offerId )
    {
      try
      {
        con = DriverManager.getConnection(  PostgresConfig.url,
                                            PostgresConfig.user,
                                            PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String id_adresuQuery = "SELECT id_adresu FROM oferty WHERE id_oferty=" + offerId + ";";
      ResultSet rs = sqlExecuteStatement(id_adresuQuery);
      int id_adres = -1;
      try
      {
        rs.next();
        id_adres = rs.getInt("id_adresu");
      }
      catch(SQLException e){}
      System.out.println("Id adresu: " + id_adres);
      
      String ulubQuery = "DELETE FROM ulubione WHERE id_oferty=" + offerId + ";";
      String adresQuery = "DELETE FROM adres WHERE id_adresu=" + id_adres + ";";
      String ofertyQuery = "DELETE FROM oferty WHERE id_oferty=" + offerId + ";";
      String query = ulubQuery + " " + ofertyQuery + " " + adresQuery;
      System.out.println("Usuwajace query: " + query);
      sqlExecuteStatementWithoutResult(query);     
      return true;
    }
    

    
    
    //------DONE------------------------------------------------------------
    @WebMethod(operationName = "deleteOffer", action="deleteOffer")
    public boolean deleteOffer( @WebParam(name = "login") String login,
                                @WebParam(name = "sessionId") String sessionId,
                                @WebParam(name = "offerId") int offerId )
    {
      try
      {
        con = DriverManager.getConnection(  PostgresConfig.url,
                                            PostgresConfig.user,
                                            PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String query = "SELECT session_id FROM users WHERE login='" + login + "';";
      ResultSet rs = sqlExecuteStatement(query);
      String ses_id = null;
      try
      {
        rs.next();
        ses_id = rs.getString("session_id");
      }
      catch(SQLException e){}
      System.out.println("Delete offer session id: " + ses_id);
      if (sessionId.equals(ses_id))
      {
        return deleteOfferAdmin(offerId);
      }
      return false;
    }
    
    
    
    
    //----------------------------------------------------------------------
    @WebMethod(operationName = "updateOfferAdmin")
    public boolean updateOfferAdmin( @WebParam(name = "price") int price,
                                     @WebParam(name = "dateAdded") String dateAdded,
                                     @WebParam(name = "area") int area,
                                     @WebParam(name = "houseType") String houseType,
                                     @WebParam(name = "street") String street,
                                     @WebParam(name = "town") String town,
                                     @WebParam(name = "house_number") int house_number,
                                     @WebParam(name = "longitude") float longitude,
                                     @WebParam(name = "latitude") float latitude,
                                     @WebParam(name = "description") String description,
                                     @WebParam(name = "owner") String owner
                                )
    {
        return true;
    }
    
    
    
    
    //----------------------------------------------------------------------
    @WebMethod(operationName = "updateOffer", action="updateOffer")
    public boolean updateOffer( @WebParam(name = "login")       String login,
                                @WebParam(name = "sessionId")   String sessionId,
                                @WebParam(name = "price") int price,
                                @WebParam(name = "dateAdded") String dateAdded,
                                @WebParam(name = "area") int area,
                                @WebParam(name = "houseType") String houseType,
                                @WebParam(name = "street") String street,
                                @WebParam(name = "town") String town,
                                @WebParam(name = "house_number") int house_number,
                                @WebParam(name = "longitude") float longitude,
                                @WebParam(name = "latitude") float latitude,
                                @WebParam(name = "description") String description
                                )
    {
        return true;
    }
    
    
    
    
    
    //------DONE------------------------------------------------------------ 
    @WebMethod(operationName = "getAllOffers", action="getAllOffers")
    public List<Offer> getAllOffers()
    {     
      List<Offer> offerList = new LinkedList<Offer>();
      
      try{
      con = DriverManager.getConnection(  PostgresConfig.url,
                                          PostgresConfig.user,
                                          PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String query = "SELECT id_oferty FROM oferty";
      ResultSet rs = sqlExecuteStatement(query);
      try
      {
        while (rs.next())
        {
          int id_oferty = rs.getInt("id_oferty");
          offerList.add(getOffer(id_oferty));
        }
      }
      catch(SQLException e){}
      
      return offerList;
    }
    
    
    
    
    
    
    //------DONE------------------------------------------------------------    
    private Offer getOffer(int idOffer)
    {
      Offer offer = new Offer();
      
      try{
      con = DriverManager.getConnection(  PostgresConfig.url,
                                          PostgresConfig.user,
                                          PostgresConfig.password);
      }
      catch(SQLException e){}
      
      //wydobywanie oferty
      String query = "SELECT * FROM oferty WHERE id_oferty=" + idOffer + ";";
      ResultSet rs = sqlExecuteStatement(query);
      int id_typ_dom = -1;
      int id_adr = -1;
      try
      {
        rs.next();
        offer.setId_offer(rs.getInt("id_oferty"));
        System.out.println("Offer id: " + offer.getId_offer());
        offer.setPrice(rs.getInt("cena"));
        System.out.println("Offer cena: " + offer.getPrice());
        offer.setDateAdded(rs.getString("data_dodania"));
        System.out.println("Offer data: " + offer.getDateAdded().toString());
        offer.setArea(rs.getInt("powierzchnia"));
        System.out.println("Offer powierzchnia: " + offer.getArea());
        offer.setDescription(rs.getString("opis"));
        System.out.println("Offre opis: " + offer.getDescription());
        offer.setOwner(rs.getString("wlasciciel"));
        System.out.println("Offer wlasciciel: " + offer.getOwner());
        id_typ_dom = rs.getInt("id_typu_domu");
        System.out.println("Id typu domu: " + id_typ_dom);
        id_adr = rs.getInt("id_adresu");
        System.out.println("Id adresu: " + id_adr);
      }
      catch (SQLException ex)
      {
        Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      //wydobywanie typu domu
      query = "SELECT * FROM typy_domow WHERE id_typu_domu=" + id_typ_dom + ";";
      rs = sqlExecuteStatement(query);
      try
      {
        rs.next();
        offer.setHouseType(rs.getString("typ_domu"));
      }
      catch (SQLException ex)
      {
        Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      //wydobywanie adresu
      query = "SELECT * FROM adres WHERE id_adresu=" + id_adr + ";";
      rs = sqlExecuteStatement(query);
      try
      {
        rs.next();
        offer.setStreet(rs.getString("ulica"));
        offer.setTown(rs.getString("miasto"));
        offer.setHouse_number(rs.getInt("nr_domu"));
        offer.setLongitude(rs.getFloat("dl_geog"));
        offer.setLatitude(rs.getFloat("szer_geog"));
      }
      catch (SQLException ex)
      {
        Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
      }
      return offer;
    }
    
    
    
    
    
    //------DONE------------------------------------------------------------
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
    
    
    
    
    //------DONE------------------------------------------------------------
    private void sqlExecuteStatementWithoutResult(String query)
    {
      try
      {
        st = con.createStatement();
        st.executeQuery(query);
        System.out.println("Offer query: " + query);
      }
      catch (SQLException ex)
      {
        Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    
}