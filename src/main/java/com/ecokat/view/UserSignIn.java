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
public class UserSignIn {
    
    private String userName;
    private String userPassword;
    
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserPassword() {
        return userPassword;
    }
 
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
