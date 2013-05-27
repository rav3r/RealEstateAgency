package org.rea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
public class Users
{

    /**
     * Create new user account
     */
    @WebMethod(operationName = "CreateUser")
    public CreateUserRes CreateUser(@WebParam(name = "login") String login,
                                    @WebParam(name = "md5password") String md5password)
    {
        CreateUserRes response = CreateUserRes.OK;
        
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
            // TODO: sprawdzanie poprawnosci loginu, hasla
            String query= "INSERT INTO users VALUES ('"+login+"', '"+md5password+"');";
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
            response = CreateUserRes.INVALID_USER;
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
        
        return response;
    }

    /**
     * Login user. Returns sessionId
     */
    @WebMethod(operationName = "login")
    public String Login(@WebParam(name = "login") String login,
                        @WebParam(name = "md5password") String md5password)
    {
        String result = null;
        
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
            // TODO: sprawdzanie poprawnosci loginu
            String query= "SELECT * FROM users WHERE login='"+login+"';";
            System.out.println(query);
            rs = st.executeQuery(query);
            if(rs.next())
            {
                if(rs.getString("md5password").equals(md5password))
                {
                    String sessionId = Util.md5(login+md5password+(new Date()));
                    st.executeUpdate("UPDATE users SET session_id='"+sessionId+"' WHERE login='"+login+"';");
                    result = sessionId;
                }
            }
            System.out.println("Polaczono");
////////////////////////////////////////////////////////////////////////////////
        }
        catch (SQLException e)
        {
            System.out.println("Blad polaczenia");
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
            result = null;
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
        return result;
    }

    /**
     * Logout
     */
    @WebMethod(operationName = "logout")
    @Oneway
    public void Logout(@WebParam(name = "sessionId") String sessionId) {
        
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
            st.executeUpdate("UPDATE users SET session_id=NULL WHERE session_id='"+sessionId+"';");

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
    }
    
     /**
     * ListUsers
     */
    @WebMethod(operationName = "listUsers")
    public List<User> ListUsers() {
        List<User> users = new LinkedList<User>();
        
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
                User user = new User();
                
                user.setFirstName(rs.getString("imie"));
                user.setLastName(rs.getString("nazwisko"));
                user.setLogin(rs.getString("login"));
                user.setMail(rs.getString("mail"));
                user.setPhoneNumber(rs.getString("telefon"));
                
                users.add(user);
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
        return users;
    }
    
     /**
     * DeleteUser
     */
    @WebMethod(operationName = "deleteUser")
    @Oneway
    public void DeleteUser(@WebParam(name = "login") String login) {
        
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
            st.execute("DELETE FROM users WHERE login='"+login+"\';");
            
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
    }
}
