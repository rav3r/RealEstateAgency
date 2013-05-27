package org.rea.web;

import java.io.Serializable;
import java.util.List;
import org.rea.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIForm;

/**
 *
 * @author rafal
 */
@ManagedBean
@SessionScoped
public class UsersMgr implements Serializable{
    List<User> userList;
    String login;
    String password;
    User user;
    
    /**
     * Creates a new instance of UsersMgr
     */
    public UsersMgr() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getUserList() {
        Users users = new Users();
        userList = users.ListUsers();
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }  

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void delete()
    {
        Users users = new Users();
        users.DeleteUser(user.getLogin());
    }
    
    public void addUser()
    {
        Users users = new Users();
        users.CreateUser(login, password);
    }

}
