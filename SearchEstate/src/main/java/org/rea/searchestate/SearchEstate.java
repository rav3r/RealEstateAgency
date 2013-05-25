/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package org.rea.searchestate;

import java.sql.*;
//import org.postgresql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        
        
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        
        String url = "jdbc:postgresql://localhost/postgres";
        //String url = "jdbc:postgresql://localhost/postgres [postgres on public]";
        //jdbc:Postgresql://localhost/postgres  soateam.heliohost.org
        String user = "rafal";
        //postgres  soateam_user
        String password = "postgre";
        //postgre  gupipies1
        try
        {
            //Class.forName("org.postgresql.Driver").newInstance();
            System.out.println("Tu kurwa jeszcze dziala");
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Tu tez jeszcze kurwa dziala");
st = con.createStatement();
rs = st.executeQuery("SELECT VERSION()");
rs = st.executeQuery("SELECT * FROM tabela;");
if (rs.next()) {
                System.out.println(rs.getString(1));
            }
System.out.println("Polaczono");
}
        catch (SQLException e)
        {
        System.out.println("Blad polaczenia");
        System.out.println(e.getMessage());
        System.out.println(e.getErrorCode());
        }
        /*
        catch (ClassNotFoundException ex) {
            Logger.getLogger(SearchEstate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        catch (InstantiationException ex) {
                Logger.getLogger(SearchEstate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(SearchEstate.class.getName()).log(Level.SEVERE, null, ex);
            }
            * */
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            }
            catch (SQLException ex) {}
        }
        
        System.out.println("txt="+txt);
        return "Hello " + txt + " !";
    }
}