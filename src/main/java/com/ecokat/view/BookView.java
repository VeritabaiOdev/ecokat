/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.view;
import com.ecokat.entity.Author;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import com.ecokat.entity.Book;
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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import com.ecokat.entity.Category;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
/**
 *
 * @author atakanatak
 */
@ManagedBean(name = "BookViewBean")
@ViewScoped
public class BookView implements Serializable{
    Book book = new Book();
    List<Book> bookList =  new ArrayList<Book>(); 
    List<Author> authorList = new ArrayList<Author>();
    List<Category> categoryList = new ArrayList<Category>();
    CategoryView categories = new CategoryView();
    AuthorListView authors = new AuthorListView();
    UploadedFile file;
    String imagepath;
    
    @PostConstruct
    public void init() {

        try {
            bookList = list();
            categoryList = categories.list();
            authorList = authors.list();
        } catch (SQLException ex) {
            Logger.getLogger(BookView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BookView(){
        
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public AuthorListView getAuthors() {
        return authors;
    }

    public void setAuthors(AuthorListView authors) {
        this.authors = authors;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public CategoryView getCategories() {
        return categories;
    }

    public void setCategories(CategoryView categories) {
        this.categories = categories;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
    
    
    public void fileUpload(FileUploadEvent event) throws IOException {
        String path = FacesContext.getCurrentInstance().getExternalContext()
                .getRealPath("/");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = fmt.format(new Date())
                + event.getFile().getFileName().substring(
                      event.getFile().getFileName().lastIndexOf('.'));
        File file = new File(path + "images/" + name);

        InputStream is = event.getFile().getInputstream();
        OutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0)
            out.write(buf, 0, len);
        is.close();
        out.close();
        imagepath = path + "images/" + name;
    }
   
    public void kaydet() throws SQLException{
        
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
            ps = connect.prepareStatement("INSERT INTO `book` (name,author_id,page_num,content,published_date,language,category_id,publisher,stock,cost,image) VALUES(?,?,?,?,?,?,?,?,?,?,?)");

            ps.setString(1, book.getName());
            ps.setInt(2, book.getAuthor_id());
            ps.setInt(3, book.getPageNumber());
            ps.setString(4, book.getContent());
            ps.setString(5, book.getPublish_date());
            ps.setString(6, book.getLanguage());
            ps.setInt(7, book.getCategory_id());
            System.out.println(book.getCategory_id());
            ps.setString(8, book.getPublisher());
            ps.setInt(9, book.getStock());
            ps.setDouble(10, book.getCost());
            ps.setString(11, imagepath);
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
        context.addMessage(null, new FacesMessage("Kullanıcı Kaydetme İşlemi", sonuc));

    }
    
    
    public List<Book> list() throws SQLException {
        
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Book cat = null;
        List<Book> catList = new ArrayList<>();
        
 
        String url = "jdbc:mysql://db4free.net:3306/ecokat";
 
        String authorname = "ecokat";
        String password = "ecokat";
 
        try {
 
            Class.forName("com.mysql.jdbc.Driver");
 
            connect = DriverManager.getConnection(url, authorname, password);
            String selectSQL = "SELECT * FROM `book`";
            ps = connect.prepareStatement(selectSQL);
            rs = ps.executeQuery(selectSQL);
           

            while (rs.next()) {
                
                cat = new Book();
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
