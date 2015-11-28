/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.db.service;

import java.util.Date;
import java.util.List;
import com.ecokat.entity.Book;
import com.ecokat.entity.Category;
import com.ecokat.entity.Comment;
import com.ecokat.entity.Favorite;
import com.ecokat.entity.Rating;
import com.ecokat.entity.Sale;
import com.ecokat.entity.User;

/**
 *
 * @author Peynir
 */
public interface RepoService {

    //Kullanıcı
    public int registerUser(User user);

    public List listUser();

    public int updateUser(User user, User user2);
    
}
