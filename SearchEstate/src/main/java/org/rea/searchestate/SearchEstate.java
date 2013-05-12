/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rea.searchestate;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author raver
 */
@WebService(serviceName = "SearchEstate")
@Stateless()
public class SearchEstate
{
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt)
    {
        System.out.println("txt="+txt);
        return "Hello " + txt + " !";
    }
}
