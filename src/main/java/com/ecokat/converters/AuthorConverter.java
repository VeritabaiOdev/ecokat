/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.converters;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import com.ecokat.db.service.ConverterService;
import com.ecokat.entity.Author;
 



/**
 *
 * @author Emre
 */
@FacesConverter("authorConverter")
public class AuthorConverter implements Converter, Serializable {

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {


                ConverterService cs = (ConverterService) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("converterServiceBean");
                return cs.getAuthorList().get(Integer.parseInt(value)-1);

            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return null;
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Author) object).getAuthor_id());
        } else {
            return null;
        }
    }
}  
