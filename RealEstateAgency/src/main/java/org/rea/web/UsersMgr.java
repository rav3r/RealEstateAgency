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
    String firstName;
    String lastName;
    String phoneNumber;
    String mail;
    String staticLogin;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getStaticLogin() {
        return staticLogin;
    }

    public void setStaticLogin(String staticLogin) {
        this.staticLogin = staticLogin;
    }

    public List<User> getUserList() {
        Users users = new Users();
        userList = users.listUsers();
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
        users.deleteUser(user.getLogin());
    }
    
    public void addUser()
    {
        Users users = new Users();
        if(login != null && password != null)
            users.createUser(login, Util.md5(password));
    }
    
    public void updateUser()
    {
        Users users = new Users();
        users.updateUser(password, staticLogin, firstName, lastName, phoneNumber, mail);
    }

    public void select()
    {
        if(user == null)
            return;
        
        Users users = new Users();
        User selectedUser = users.getUser("", user.getLogin());
        
        if(selectedUser == null)
            return;
        
        this.staticLogin = user.getLogin();
        this.firstName = selectedUser.getFirstName();
        this.lastName = selectedUser.getLastName();
        this.mail = selectedUser.getMail();
        this.phoneNumber = selectedUser.getPhoneNumber();
    }
}
