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
import com.ecokat.entity.Author;
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
@ManagedBean(name = "AuthorRegisterViewBean")
@ViewScoped
public class AuthorRegisterView implements Serializable{
    Author author;
    Author selectedAuthor;
    
    List<Author> authorList;
    
    RepoService reposervice;
    
    @PostConstruct
    public void init() {

        // Or in case you want to create a new entity.
        author = new Author();
    }
    
    public AuthorRegisterView(){
        
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Author getSelectedAuthor() {
        return selectedAuthor;
    }

    public void setSelectedAuthor(Author selectedAuthor) {
        this.selectedAuthor = selectedAuthor;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public RepoService getReposervice() {
        return reposervice;
    }

    public void setReposervice(RepoService reposervice) {
        this.reposervice = reposervice;
    }
    
    public void kaydet() throws SQLException {
        
        Connection connect = null;
        PreparedStatement ps = null;
        int i = 0;
 
        String url = "jdbc:mysql://db4free.net:3306/ecokat";
 
        String authorname = "ecokat";
        String password = "ecokat";
 
        try {
 
            Class.forName("com.mysql.jdbc.Driver");
 
            connect = DriverManager.getConnection(url, authorname, password);
            // System.out.println("Connection established"+connect);
            ps = connect.prepareStatement("INSERT INTO `author` (author_name) VALUES(?)");

            ps.setString(1, author.getAuthor_name());
            
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
                sonuc = "Kaydetme başarılı!";
            } else {
                sonuc = "Kaydetme başarısız!";
            }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Yazar Kaydetme İşlemi", sonuc));
        author = new Author();

    }
           
}
