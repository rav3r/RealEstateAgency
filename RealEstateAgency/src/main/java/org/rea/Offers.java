/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rea;

import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.swing.JFrame;

/**
 *
 * @author rafal
 */
@WebService(serviceName = "Offers")
@Stateless()
public class Offers {

    
    /**
     * Test
     */
    @WebMethod(operationName = "test")
    public String testing()
    {
        System.out.println("Przetestowano");
        return "Test";
        
    }
    
    
    
    
    /**
     * Create new offer
     */
    @WebMethod(operationName = "CreateOffer")
    public Offer CreateOffer(   @WebParam(name = "login") String login,
                                @WebParam(name = "sessionId") String sessionId ) {
        Offer offer = new Offer();
        
        offer.setId("TODO");
        offer.setStreet("default");
        offer.setLatitude(0);
        offer.setLongitude(0);
        offer.setNumber("default");
        offer.setTown("default");
        offer.setDateAdded(new Date());
        
        List<String> tags = new LinkedList<String>();
        tags.add("piekny");
        tags.add("nowoczesny");
        offer.setTags(tags);
        
        
        Connection  con = null;
        Statement   st  = null;
        ResultSet   rs  = null;
     
        try
        {
            con = DriverManager.getConnection(  PostgresConfig.url,
                                                PostgresConfig.user,
                                                PostgresConfig.password);
            st = con.createStatement();
        ////////////////////////////////////////////////////////////////////////////////
            // TODO: dodawanie calosci oferty
            String query= "INSERT INTO oferty(cena) VALUES ('200');";
            System.out.println(query);
            st.execute(query);
            System.out.println("Polaczono");
////////////////////////////////////////////////////////////////////////////////
        }
        catch (SQLException e)
        {
            System.out.println("Blad polaczenia");
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }
        finally
        {
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
        }
        
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
    
}
