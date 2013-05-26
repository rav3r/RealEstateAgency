/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rea;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.jws.Oneway;

/**
 *
 * @author rafal
 */
@WebService(serviceName = "Offers")
@Stateless()
public class Offers {

    /**
     * Create new user account
     */
    @WebMethod(operationName = "CreateOffer")
    public Offer CreateOffer(@WebParam(name = "login") String login, @WebParam(name = "sessionId") String sessionId ) {
        Offer offer = new Offer();
        offer.setStreet("default");
        offer.setLatitude(0);
        offer.setLongitude(0);
        offer.setNumber("default");
        offer.setTown("default");
        return offer;
    }
}
