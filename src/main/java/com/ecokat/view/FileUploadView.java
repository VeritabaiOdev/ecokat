/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.view;

/**
 *
 * @author peyloride
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
 
import org.primefaces.model.UploadedFile;
 
@ManagedBean(name = "FileUploadViewBean")
@SessionScoped
public class FileUploadView {
    UploadedFile file;
 
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
 
    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void fileUpload(FileUploadEvent event) throws IOException {
        String path = FacesContext.getCurrentInstance().getExternalContext()
                .getRealPath("/");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = fmt.format(new Date())
                + event.getFile().getFileName().substring(
                      event.getFile().getFileName().lastIndexOf('.'));
        File file = new File(path + "images/" + name);
            System.out.println(path + "images/"+ name);

        InputStream is = event.getFile().getInputstream();
        OutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0)
            out.write(buf, 0, len);
        is.close();
        out.close();
    }
}
