/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.db.service;

import java.io.Serializable;
import java.util.List;
import com.ecokat.entity.Author;
import com.ecokat.entity.Category;


/**
 *
 * @author Emre
 */
public interface ConverterService {
    public List<Author> getAuthorList();
    public List<Category> getCategoryList();
}
