/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rea;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author rafal
 */
@WebService(serviceName = "Offers")
@Stateless()
public class Offers {

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
