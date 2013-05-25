/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rea;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author rafal
 */
@WebService(serviceName = "Users")
@Stateless()
public class Users {
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Usersasf " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "CreateUser")
    public CreateUserRes CreateUser(@WebParam(name = "login") String login, @WebParam(name = "md5password") String md5password) {
        return CreateUserRes.OK;
    }
}
