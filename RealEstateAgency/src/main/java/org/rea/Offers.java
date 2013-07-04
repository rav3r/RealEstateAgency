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
         while(rs_adres.next())
        {
          if (rs_adres.getInt("id_adresu")>id_adres)
            id_adres = rs_adres.getInt("id_adresu");
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
    //authorization ok
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
      ResultSet rsb = sqlExecuteStatement(query);
      String ses_id = null;
      try
      {
        rsb.next();
        ses_id = rsb.getString("session_id");
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
    //test - ok
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
      ResultSet rsb = sqlExecuteStatement(id_adresuQuery);
      int id_adres = -1;
      try
      {
        rsb.next();
        id_adres = rsb.getInt("id_adresu");
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
    //authorization ok
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
    //not tested - should be ok
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
    //adding offers ok, bug with return value
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
      System.out.println("Add favourite offer session id: " + ses_id);
      if (sessionId.equals(ses_id))
      {
        System.out.println("Add favourite offer: Authorization succesful");
        query = "INSERT INTO ulubione VALUES(" + offerId + ", '" + login + "');";
        try
        {
          st = con.createStatement();
          System.out.println("Add favourite offer: after create statement");
          st.executeQuery(query);
          System.out.println("Add favourite offer: after execute query");
          System.out.println("Favourite offer insert query: " + query);
          //return true;
          retBool = true;
          
        }
        catch (SQLException ex)
        {
          System.out.println("Caught SQL exception");
          System.out.println("Error message: " + ex.getMessage());
          System.out.println("Error toString: " + ex.toString());
          //return false;
          retBool = false;
          //Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      }
      //return false;
      System.out.println("Final retern value: " + retBool);
      //retBool = false;
      return retBool;
    }
    
    
    //------DONE------------------------------------------------------------
    //not tested - should be ok
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
      System.out.println("Add favourite offer session id: " + ses_id);
      if (sessionId.equals(ses_id))
      {
        query = "DELETE FROM ulubione WHERE id_oferty=" + offerId + " AND login='" + login + "';";
        sqlExecuteStatementWithoutResult(query);
      }
    }
    
    
    //------DONE------------------------------------------------------------
    //not tested - should be ok
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
      System.out.println("Warunki selekcji ofert 1: " + warunki);
      
      if (town!=null && town.length()>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="a.miasto='" + town + "'";
      }
      System.out.println("Warunki selekcji ofert 2: " + warunki);
      
      if(priceLowerBorder>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="o.cena>=" + priceLowerBorder;
      }
      System.out.println("Warunki selekcji ofert 3: " + warunki);
      
      if(priceHigherBorder>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="o.cena<=" + priceHigherBorder;
      }
      System.out.println("Warunki selekcji ofert 4: " + warunki);
      
      if(areaLowerBorder>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="o.powierzchnia>=" + areaLowerBorder;
      }
      System.out.println("Warunki selekcji ofert 5: " + warunki);
      
      if(areaHigherBorder>0)
      {
        if (warunki.length()<1)
          warunki += "WHERE ";
        else
          warunki +=" AND ";
        warunki +="o.powierzchnia<=" + areaHigherBorder;
      }
      System.out.println("Warunki selekcji ofert 6: " + warunki);
      
      //if (warunki==null) warunki="";
      String mainQuery = "SELECT id_oferty FROM oferty o JOIN typy_domow td ON o.id_typu_domu=td.id_typu_domu JOIN adres a ON o.id_adresu=a.id_adresu " + warunki + ";";
      System.out.println("Offer selection main query: " + mainQuery);
      
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
      //return getAllOffers();
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
      
//      houseTypeList.add("mieszkanie");
//      houseTypeList.add("szeregowy");
//      houseTypeList.add("wolnostojacy");
//      houseTypeList.add("dom jednorodzinny");
      
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
    //not tested, should be ok
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
      System.out.println("Get favourite offers session id: " + ses_id);
      
      
      if (sessionId.equals(ses_id))
      {
        //pobieranie ulubionych ofert
        //SELECT id_oferty FROM ulubione WHERE login='trol';
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
        System.out.println("Offer id: " + offer.getId_offer());
        offer.setPrice(rsb.getInt("cena"));
        System.out.println("Offer cena: " + offer.getPrice());
        offer.setDateAdded(rsb.getString("data_dodania"));
        System.out.println("Offer data: " + offer.getDateAdded().toString());
        offer.setArea(rsb.getInt("powierzchnia"));
        System.out.println("Offer powierzchnia: " + offer.getArea());
        offer.setDescription(rsb.getString("opis"));
        System.out.println("Offre opis: " + offer.getDescription());
        offer.setOwner(rsb.getString("wlasciciel"));
        System.out.println("Offer wlasciciel: " + offer.getOwner());
        id_typ_dom = rsb.getInt("id_typu_domu");
        System.out.println("Id typu domu: " + id_typ_dom);
        id_adr = rsb.getInt("id_adresu");
        System.out.println("Id adresu: " + id_adr);
      }
      catch (SQLException ex)
      {
        Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
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
        //System.out.println("Offer query: " + query);
        rs = st.executeQuery(query);
      }
      catch (SQLException ex) {
      Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
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
        st.executeQuery(query);
        System.out.println("Offer query: " + query);
      }
      catch (SQLException ex)
      {
        Logger.getLogger(Offers.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    
}