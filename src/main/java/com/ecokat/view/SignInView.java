/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.view;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import com.ecokat.entity.User;
import javax.faces.bean.ManagedBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
//import com.ecokat.db.service.ConverterService;
import com.ecokat.db.service.RepoService;
//import com.ecokat.entity.Baskanlik;
//import com.ecokat.entity.KullaniciTuru;
import com.ecokat.service.impl.RepoServiceImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
/**
 *
 * @author atakanatak
 */
@ManagedBean(name = "SignInViewBean")
@ViewScoped
public class SignInView implements Serializable{
    User user;
    User selectedUser;
    
    List<User> userList;
    
    RepoService reposervice;
    
    @PostConstruct
    public void init() {

        // Or in case you want to create a new entity.
        user = new User();
    }
    
    public SignInView(){
        
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public RepoService getReposervice() {
        return reposervice;
    }

    public void setReposervice(RepoService reposervice) {
        this.reposervice = reposervice;
    }
    
    public void giris() throws SQLException {
        
        Connection connect = null;
        PreparedStatement ps = null;
        int i = 0;
 
        String url = "jdbc:mysql://db4free.net:3306/ecokat";
 
        String username = "ecokat";
        String password = "ecokat";
 
        try {
 
            Class.forName("com.mysql.jdbc.Driver");
 
            connect = DriverManager.getConnection(url, username, password);
            // System.out.println("Connection established"+connect);
            ps = connect.prepareStatement("INSERT INTO `user` (first_name,password) VALUES(?,?)");

            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            i = ps.executeUpdate();
 
        } catch (Exception ex) {
            System.out.println("in exec");
            System.out.println(ex.getMessage());
        } finally {
            ps.close();
            connect.close();
        }

        String sonuc;
            if (i > 0) {
                sonuc = "Hosgeldiniz!";
            } else {
                sonuc = "Giris başarısız!";
            }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Kullanıcı Kaydetme İşlemi", sonuc));
        user = new User();

    }
    
    
    
}
