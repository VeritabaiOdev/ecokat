/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.view;
import javax.faces.bean.ManagedBean;
/**
 *
 * @author atakanatak
 */
public class UserRegister {
    private String name;
    private String surname;
    private String password;
    private String date;
    private String phone;
    private String mail;
    private String city;
    
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }
 
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getDate() {
        return date;
    }
 
    public void setDate(String date) {
        this.date = date;
    }
 
    public String getPhone() {
        return phone;
    }
 
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getMail() {
        return mail;
    }
 
    public void setMail(String mail) {
        this.mail = mail;
    }
    
    public String getCity() {
        return city;
    }
 
    public void setCity(String city) {
        this.city = city;
    }
}
