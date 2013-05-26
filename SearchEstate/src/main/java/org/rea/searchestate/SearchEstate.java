package org.rea.searchestate;

import java.sql.*;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import org.rea.PostgresConfig;

/**
*
* @author raver
*/
@WebService(serviceName = "SearchEstate")
@Stateless()
public class SearchEstate
{
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt)
    {
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
            rs = st.executeQuery("SELECT * FROM users;");
            while(rs.next())
            {
                System.out.println(rs.getString("login"));
            }
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
        
        return "Hello " + txt + " !";
    }
}