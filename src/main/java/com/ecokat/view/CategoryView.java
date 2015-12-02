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
import com.ecokat.entity.Category;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
/**
 *
 * @author atakanatak
 */
@ManagedBean(name = "CategoryViewBean")
@ViewScoped
public class CategoryView implements Serializable{
    Category category = new Category();
    List<Category> categoryList =  new ArrayList<Category>(); 
    
    @PostConstruct
    public void init() {

        try {
            categoryList = list();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public CategoryView(){
        
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    
    
    public List<Category> list() throws SQLException {
        
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Category cat = null;
        List<Category> catList = new ArrayList<>();
        
 
        String url = "jdbc:mysql://db4free.net:3306/ecokat";
 
        String authorname = "ecokat";
        String password = "ecokat";
 
        try {
 
            Class.forName("com.mysql.jdbc.Driver");
 
            connect = DriverManager.getConnection(url, authorname, password);
            String selectSQL = "SELECT * FROM `category`";
            ps = connect.prepareStatement(selectSQL);
            rs = ps.executeQuery(selectSQL);
           

            while (rs.next()) {
                
                cat = new Category();
                cat.setName(rs.getString("name"));
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
