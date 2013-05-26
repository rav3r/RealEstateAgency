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
@WebService(serviceName = "Users")
@Stateless()
public class Users {

    /**
     * Create new user account
     */
    @WebMethod(operationName = "CreateUser")
    public CreateUserRes CreateUser(@WebParam(name = "login") String login, @WebParam(name = "md5password") String md5password) {
        return CreateUserRes.OK;
    }

    /**
     * Login user. Returns sessionId
     */
    @WebMethod(operationName = "login")
    public String Login(@WebParam(name = "login") String login, @WebParam(name = "md5password") String md5password) {
        return Util.md5(login);
    }

    /**
     * Logout
     */
    @WebMethod(operationName = "logout")
    @Oneway
    public void Logout(@WebParam(name = "sessionId") String sessionId) {
        // TODO
    }
}
