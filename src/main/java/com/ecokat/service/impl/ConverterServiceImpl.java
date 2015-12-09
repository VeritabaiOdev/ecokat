/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.ecokat.db.service.ConverterService;
import com.ecokat.entity.Author;
import com.ecokat.entity.Category;
import com.ecokat.view.CategoryView;
import com.ecokat.view.AuthorListView;

@ManagedBean(name= "converterServiceBean")
@SessionScoped
public class ConverterServiceImpl implements ConverterService, Serializable {

    CategoryView categoryView = new CategoryView();
    AuthorListView authorView = new AuthorListView();
    private List<Author> authors;
    private List<Category> categories;
    
    
    @PostConstruct
    public void init() {
       categories = categoryView.getCategoryList();
       authors = authorView.getAuthorList();
    }

    @Override
    public List<Author> getAuthorList() {
        return authors;
    }

    @Override
    public List<Category> getCategoryList() {
        return categories;
    }
    
}

