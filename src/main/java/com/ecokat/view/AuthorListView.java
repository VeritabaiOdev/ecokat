/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.view;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import com.ecokat.entity.Author;
//import com.ecokat.entity.Baskanlik;
//import com.ecokat.entity.KullaniciTuru;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
/**
 *
 * @author atakanatak
 */
@ManagedBean(name = "AuthorListViewBean")
@ViewScoped
public class AuthorListView implements Serializable{
    Author author = new Author();
    List<Author> authorList =  new ArrayList<Author>(); 
    
    @PostConstruct
    public void init() {

        try {
            authorList = list();
        } catch (SQLException ex) {
            Logger.getLogger(AuthorListView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public AuthorListView(){
        
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }
    
    
    public List<Author> list() throws SQLException {
        
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Author cat = null;
        List<Author> catList = new ArrayList<>();
        
 
        String url = "jdbc:mysql://db4free.net:3306/ecokat";
 
        String authorname = "ecokat";
        String password = "ecokat";
 
        try {
 
            Class.forName("com.mysql.jdbc.Driver");
 
            connect = DriverManager.getConnection(url, authorname, password);
            String selectSQL = "SELECT * FROM `author`";
            ps = connect.prepareStatement(selectSQL);
            rs = ps.executeQuery(selectSQL);
           

            while (rs.next()) {
                
                cat = new Author();
                cat.setAuthor_name(rs.getString("author_name"));
                cat.setAuthor_id(rs.getInt("author_id"));
                catList.add(cat);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            rs.close();
            ps.close();
            connect.close();
        }

        return catList;

    }
           
}
