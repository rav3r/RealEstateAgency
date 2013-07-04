package org.rea;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
    
    
    
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
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
        String query = null;
      
      //dodawanie adresu
      query = "INSERT INTO adres(miasto, ulica, nr_domu, dl_geog, szer_geog) VALUES ('"
              + town + "', '" + street + "', " + house_number + ", " + longitude + ", " + latitude + ");";
      sqlExecuteStatementWithoutResult(query);
      
      
      //ustalanie id typu domu
      query = "SELECT id_typu_domu FROM typy_domow WHERE typ_domu='" + houseType + "';";
      ResultSet rsTypDomu = sqlExecuteStatement(query);
      int id_typu_domu=-1;
               rsTypDomu.next();
         id_typu_domu = rsTypDomu.getInt("id_typu_domu");
      
      //ustalanie id_adresu
      query = "SELECT id_adresu FROM adres WHERE (miasto='" + town + "' AND ulica='" + 
              street + "' AND nr_domu=" + house_number + " AND dl_geog=" + longitude +
              " AND szer_geog=" + latitude + ");";
      ResultSet rs_adres = sqlExecuteStatement(query);
      int id_adres=-1;
      while(rs_adres.next())
        {
          if (rs_adres.getInt("id_adresu")>id_adres)
            id_adres = rs_adres.getInt("id_adresu");
        }
              
      //dodawanie oferty
      query = "INSERT INTO oferty(cena, data_dodania, powierzchnia, id_typu_domu, id_adresu, opis, wlasciciel) "
              + "VALUES (" + price + ", current_date, " + area + ", " + id_typu_domu + ", "
              + id_adres + ", '" + description + "', '" + owner + "');";
      sqlExecuteStatementWithoutResult(query);
      }
      catch(SQLException e){}

        
      return true;
    }
    
    
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
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
              String query = "SELECT session_id FROM users WHERE login='" + login + "';";
      ResultSet rsb = sqlExecuteStatement(query);
      String ses_id = null;
              rsb.next();
        ses_id = rsb.getString("session_id");
            if (sessionId.equals(ses_id))
      {
        return createOfferAdmin(price, area, houseType, street, town, house_number,
                                longitude, latitude, description, login);
      }
      }
      catch(SQLException e){}


      return false;
    }
    
    
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
    @WebMethod(operationName = "deleteOfferAdmin")
    public boolean deleteOfferAdmin( @WebParam(name = "offerId") int offerId )
    {
      try
      {
        con = DriverManager.getConnection(  PostgresConfig.url,
                                            PostgresConfig.user,
                                            PostgresConfig.password);
        String id_adresuQuery = "SELECT id_adresu FROM oferty WHERE id_oferty=" + offerId + ";";
      ResultSet rsb = sqlExecuteStatement(id_adresuQuery);
      int id_adres = -1;
        rsb.next();
        id_adres = rsb.getInt("id_adresu");
      
      String ulubQuery = "DELETE FROM ulubione WHERE id_oferty=" + offerId + ";";
      String adresQuery = "DELETE FROM adres WHERE id_adresu=" + id_adres + ";";
      String ofertyQuery = "DELETE FROM oferty WHERE id_oferty=" + offerId + ";";
      String query = ulubQuery + " " + ofertyQuery + " " + adresQuery;
      sqlExecuteStatementWithoutResult(query);    
            return true;
            }
      catch(SQLException e){}
return false;
    }
    

    
    
    //------DONE------------------------------------------------------------
    //tested - ok
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
      ResultSet rsb = sqlExecuteStatement(query);
      String ses_id = null;
      try
      {
        rsb.next();
        ses_id = rsb.getString("session_id");
      }
      catch(SQLException e){}
      if (sessionId.equals(ses_id))
      {
        return deleteOfferAdmin(offerId);
      }
      return false;
    }
    
    
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
    @WebMethod(operationName = "updateOfferAdmin")
    public void updateOfferAdmin( @WebParam(name = "offerId") int offerId,
                                     @WebParam(name = "price") int price,
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
      
      String query = "SELECT id_oferty FROM oferty WHERE id_oferty= " + offerId + ";";
      ResultSet rsb = sqlExecuteStatement(query);
      try
      {
        if (rsb.next()==false)
        {
          //add offer
          createOfferAdmin(price, area, houseType, street, town, house_number, longitude,
                           latitude, description, owner);
          System.out.println("Nie ma wyniku");
        }
        else
        {
          //updateOffer
          int idOferty = rsb.getInt("id_oferty");
          
          query = "SELECT * FROM oferty WHERE id_oferty=" + idOferty + ";";
          rsb = sqlExecuteStatement(query);
          int id_adr = -1;
          
          rsb.next();
          id_adr = rsb.getInt("id_adresu");
          query = "UPDATE oferty SET cena=" + price + ", powierzchnia=" + area + ", id_typu_domu=(SELECT id_typu_domu FROM typy_domow WHERE typ_domu='" + houseType + "'), opis='" + description + "', wlasciciel='" + owner + "' WHERE id_oferty=" + offerId + ";";
          sqlExecuteStatementWithoutResult(query);
          query = "UPDATE adres SET miasto='" + town + "', ulica='" + street + "', nr_domu=" + house_number + ", dl_geog=" + longitude + ", szer_geog=" + latitude + " WHERE id_adresu=" + id_adr + ";";
          sqlExecuteStatementWithoutResult(query);
        }
      }
      catch (SQLException e){}
      
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
    //tested - ok
    @WebMethod(operationName = "addHouseType", action = "addHouseType")
    public void addHouseType(@WebParam(name = "houseType") String houseType)
    {
      try
      {
        con = DriverManager.getConnection(  PostgresConfig.url,
                                            PostgresConfig.user,
                                            PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String query = "INSERT INTO typy_domow(typ_domu) VALUES ('" + houseType + "');";
      sqlExecuteStatementWithoutResult(query);
    }
    
    
    
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
    @WebMethod(operationName = "addFavouriteOffer", action = "addFavouriteOffer")
    public boolean addFavouriteOffer(@WebParam(name = "login") String login,
                                     @WebParam(name = "sessionId") String sessionId,
                                     @WebParam(name = "offerId") int offerId)
    {
      boolean retBool = false;
      try
      {
        con = DriverManager.getConnection(  PostgresConfig.url,
                                            PostgresConfig.user,
                                            PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String query = "SELECT session_id FROM users WHERE login='" + login + "';";
      ResultSet rsb = sqlExecuteStatement(query);
      String ses_id = null;
      try
      {
        rsb.next();
        ses_id = rsb.getString("session_id");
      }
      catch(SQLException e){}
      if (sessionId.equals(ses_id))
      {
        query = "INSERT INTO ulubione VALUES(" + offerId + ", '" + login + "');";
        try
        {
          st = con.createStatement();
          st.executeUpdate(query);
          retBool = true;
        }
        catch (SQLException ex)
        {
          if(!(ex.getMessage().equals("Zapytanie nie zwróciło żadnych wyników.")))
            retBool = false;
        }
        
      }
      return retBool;
    }
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
    @WebMethod(operationName = "deleteFavouriteOffer", action = "deleteFavouriteOffer")
    public void deleteFavouriteOffer(@WebParam(name = "login") String login,
                                     @WebParam(name = "sessionId") String sessionId,
                                     @WebParam(name = "offerId") int offerId)
    {
      try
      {
        con = DriverManager.getConnection(  PostgresConfig.url,
                                            PostgresConfig.user,
                                            PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String query = "SELECT session_id FROM users WHERE login='" + login + "';";
      ResultSet rsb = sqlExecuteStatement(query);
      String ses_id = null;
      try
      {
        rsb.next();
        ses_id = rsb.getString("session_id");
      }
      catch(SQLException e){}
      if (sessionId.equals(ses_id))
      {
        query = "DELETE FROM ulubione WHERE id_oferty=" + offerId + " AND login='" + login + "';";
        sqlExecuteStatementWithoutResult(query);
      }
    }
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
    @WebMethod(operationName = "getSelectedOffers", action = "getSelectedOffers")
    public List<Offer> getSelectedOffers ( @WebParam(name="houseType") String houseType,  //house type null - dowolny typ, !=null - danego typu
                                           @WebParam(name="town") String town,  //town null - brak wyszukiwania po miescie, town != null - oferta z danego miasta
                                           @WebParam(name="priceLowerBorder") int priceLowerBorder,  //0 - brak dolnej granicy
                                           @WebParam(name="priceHigherBorder") int priceHigherBorder,  //0 - brak gornej granicy
                                           @WebParam(name="areaLowerBorder") int areaLowerBorder,  //0 - brak dolnej granicy
                                           @WebParam(name="areaHigherBorder") int areaHigherBorder  //0 - brak gornej granicy
            )
    {
      String warunki = "";
     
      if (houseType!=null && houseType.length()>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="td.typ_domu='" + houseType + "'";
      }
      
      if (town!=null && town.length()>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="a.miasto='" + town + "'";
      }
      
      if(priceLowerBorder>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="o.cena>=" + priceLowerBorder;
      }
      
      if(priceHigherBorder>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="o.cena<=" + priceHigherBorder;
      }
      
      if(areaLowerBorder>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="o.powierzchnia>=" + areaLowerBorder;
      }
      
      if(areaHigherBorder>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="o.powierzchnia<=" + areaHigherBorder;
      }
      
      String mainQuery = "SELECT id_oferty FROM oferty o JOIN typy_domow td ON o.id_typu_domu=td.id_typu_domu JOIN adres a ON o.id_adresu=a.id_adresu " + warunki + ";";
      
      ResultSet rsb = sqlExecuteStatement(mainQuery);
      LinkedList<Offer> offerList = new LinkedList<Offer>();
      try
      {
        while(rsb.next())
        {
          offerList.add(getOffer(rsb.getInt("id_oferty")));
        }
      }
      catch (SQLException e){}
      
      
      return offerList;
    }
    
    
    
    
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
    @WebMethod(operationName = "getHouseTypes", action = "getHouseTypes")
    public List<String> getHouseTypes()
    {
      List<String> houseTypeList = new LinkedList<String>();
      
      try
      {
        con = DriverManager.getConnection(  PostgresConfig.url,
                                            PostgresConfig.user,
                                            PostgresConfig.password);
        String query = "SELECT typ_domu FROM typy_domow;";
        ResultSet rsb = sqlExecuteStatement(query);
      
        while(rsb.next())
        {
          houseTypeList.add(rsb.getString("typ_domu"));
        }
      }
      catch(SQLException e){}
      
      return houseTypeList;
    }
    
    
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
    @WebMethod(operationName = "getNewestOffers", action = "getNewestOffers")
    public List<Offer> getNewestOffers(@WebParam(name = "quantity") int quantity)
    {     
      List<Offer> offerList = new LinkedList<Offer>();
      
      try{
      con = DriverManager.getConnection(  PostgresConfig.url,
                                          PostgresConfig.user,
                                          PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String query = "SELECT id_oferty FROM oferty ORDER BY data_dodania DESC LIMIT " + quantity + ";";
      ResultSet rsb = sqlExecuteStatement(query);
      try
      {
        while (rsb.next())
        {
          int id_oferty = rsb.getInt("id_oferty");
          offerList.add(getOffer(id_oferty));
        }
      }
      catch(SQLException e){}
      
      return offerList;
    }
    
    
    
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
    @WebMethod(operationName = "getRandomOffers", action = "getRandomOffers")
    public List<Offer> getRandomOffers(@WebParam(name = "quantity") int quantity)
    {
      try{
      con = DriverManager.getConnection(  PostgresConfig.url,
                                          PostgresConfig.user,
                                          PostgresConfig.password);
      }
      catch(SQLException e){}
      
      List<Integer> idOfferList = new LinkedList<Integer>();
      
      String query = "SELECT id_oferty FROM oferty;";
      ResultSet rsb = sqlExecuteStatement(query);
      try
      {
        while (rsb.next())
        {
          int id_oferty = rsb.getInt("id_oferty");
          idOfferList.add(id_oferty);
        }
      }
      catch(SQLException e){}
      
      Random random = new Random();
      List<Offer> offerList = new LinkedList<Offer>();
      int i=1;
      while (idOfferList.size()>0 && i<=quantity)
      {
        int randIndexList = random.nextInt(idOfferList.size());
        int randIdOffer = idOfferList.get(randIndexList);
        Offer offer = getOffer(randIdOffer);
        offerList.add(offer);
        idOfferList.remove(randIndexList);
        i++;
      }
      return offerList;
    }
            
            
            
            
            
            
    //------DONE------------------------------------------------------------
    //tested - ok
    @WebMethod(operationName = "getFavouriteOffers", action = "getFavouriteOffers")
    public List<Offer> getFavouriteOffers(@WebParam(name = "login") String login,
                                          @WebParam(name = "sessionId") String sessionId)
    {
      try
      {
        con = DriverManager.getConnection(  PostgresConfig.url,
                                            PostgresConfig.user,
                                            PostgresConfig.password);
      }
      catch(SQLException e){}
      
      String query = "SELECT session_id FROM users WHERE login='" + login + "';";
      ResultSet rsb = sqlExecuteStatement(query);
      String ses_id = null;
      try
      {
        rsb.next();
        ses_id = rsb.getString("session_id");
      }
      catch(SQLException e){}
      
      
      if (sessionId.equals(ses_id))
      {
        List<Offer> offerList = new LinkedList<Offer>();
        query = "SELECT id_oferty FROM ulubione WHERE login='" + login + "';";
        rsb = sqlExecuteStatement(query);
        
        try
        {
          while (rsb.next())
          {
            int id_oferty = rsb.getInt("id_oferty");
            offerList.add(getOffer(id_oferty));
          }
        }
        catch(SQLException e){}
        return offerList;
      }
      return null;
    }
    
    
    
    //------DONE------------------------------------------------------------ 
    //tested - ok
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
      
      String query = "SELECT id_oferty FROM oferty;";
      ResultSet rsb = sqlExecuteStatement(query);
      try
      {
        while (rsb.next())
        {
          int id_oferty = rsb.getInt("id_oferty");
          offerList.add(getOffer(id_oferty));
        }
      }
      catch(SQLException e){}
      
      return offerList;
    }
    
    
    
    
    
    
    //------DONE------------------------------------------------------------ 
    //tested - ok
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
      ResultSet rsb = sqlExecuteStatement(query);
      int id_typ_dom = -1;
      int id_adr = -1;
      try
      {
        rsb.next();
        offer.setId_offer(rsb.getInt("id_oferty"));
        offer.setPrice(rsb.getInt("cena"));
        offer.setDateAdded(rsb.getString("data_dodania"));
        offer.setArea(rsb.getInt("powierzchnia"));
        offer.setDescription(rsb.getString("opis"));
        offer.setOwner(rsb.getString("wlasciciel"));
        id_typ_dom = rsb.getInt("id_typu_domu");
        id_adr = rsb.getInt("id_adresu");
      }
      catch (SQLException ex)
      {
      }
      
      //wydobywanie typu domu
      query = "SELECT * FROM typy_domow WHERE id_typu_domu=" + id_typ_dom + ";";
      rsb = sqlExecuteStatement(query);
      try
      {
        rsb.next();
        offer.setHouseType(rsb.getString("typ_domu"));
      }
      catch (SQLException ex)
      {
      }
      
      //wydobywanie adresu
      query = "SELECT * FROM adres WHERE id_adresu=" + id_adr + ";";
      rsb = sqlExecuteStatement(query);
      try
      {
        rsb.next();
        offer.setStreet(rsb.getString("ulica"));
        offer.setTown(rsb.getString("miasto"));
        offer.setHouse_number(rsb.getInt("nr_domu"));
        offer.setLongitude(rsb.getFloat("dl_geog"));
        offer.setLatitude(rsb.getFloat("szer_geog"));
      }
      catch (SQLException ex)
      {
      }
      return offer;
    }
    
    
    
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
    private ResultSet sqlExecuteStatement(String query)
    {
      try
      {
        st = con.createStatement();
        rs = st.executeQuery(query);
      }
      catch (SQLException ex) {
    }
    return rs;
    }
    
    
    
    
    //------DONE------------------------------------------------------------
    //tested - ok
    private void sqlExecuteStatementWithoutResult(String query)
    {
      try
      {
        st = con.createStatement();
        st.executeUpdate(query);
        System.out.println("Offer query: " + query);
      }
      catch (SQLException ex)
      {
        Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    
}