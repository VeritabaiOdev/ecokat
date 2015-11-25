/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.gov.eba.projeyonetim.view;

import java.io.Serializable;
import tr.gov.eba.projeyonetim.entity.Kullanici;
import javax.faces.bean.ManagedBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import tr.gov.eba.projeyonetim.db.service.ConverterService;
import tr.gov.eba.projeyonetim.db.service.RepoService;
import tr.gov.eba.projeyonetim.entity.Baskanlik;
import tr.gov.eba.projeyonetim.entity.KullaniciTuru;
import tr.gov.eba.projeyonetim.service.impl.RepoServiceImpl;

/**
 *
 * @author okanb3
 */
@ManagedBean(name = "kullaniciViewBean")
@ViewScoped
 public class KullaniciView implements Serializable {

    private boolean gizle = false;
    Kullanici kullanici;
    Kullanici selectedkullanici;
    List<Kullanici> kullaniciList;
    List<KullaniciTuru> kullaniciTuruList;
    List<Baskanlik> baskanlikList;

    RepoService reposervice;
    
    
    @ManagedProperty(value="#{loginView}")
    LoginView loginView;
    
    @ManagedProperty(value="#{converterServiceBean}")
    ConverterService converterService;

    public KullaniciView() {

    }

    @PostConstruct
    public void init() {
        this.reposervice = new RepoServiceImpl();
        this.kullanici = new Kullanici();
        this.selectedkullanici = new Kullanici();
        this.kullaniciList = reposervice.listeleKullaniciListesi();
        kullaniciTuruList = converterService.getKullaniciTuruList();
        baskanlikList = converterService.getBaskanlikList();
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }
    
    public List<Baskanlik> getBaskanlikList() {
        return baskanlikList;
    }

    public void setBaskanlikList(List<Baskanlik> baskanlikList) {
        this.baskanlikList = baskanlikList;
    }

    public ConverterService getConverterService() {
        return converterService;
    }

    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    public List<KullaniciTuru> getKullaniciTuruList() {
        return kullaniciTuruList;
    }

    public void setKullaniciTuruList(List<KullaniciTuru> kullaniciTuruList) {
        this.kullaniciTuruList = kullaniciTuruList;
    }

    public boolean isGizle() {
        return gizle;
    }

    public void setGizle(boolean gizle) {
        this.gizle = gizle;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public Kullanici getSelectedkullanici() {
        return selectedkullanici;
    }

    public void setSelectedkullanici(Kullanici selectedkullanici) {
        this.selectedkullanici = selectedkullanici;
    }

    public List<Kullanici> getKullaniciList() {
        return kullaniciList;
    }

    public void setKullaniciList(List<Kullanici> kullaniciList) {
        this.kullaniciList = kullaniciList;
    }

    public RepoService getReposervice() {
        return reposervice;
    }

    public void setReposervice(RepoService reposervice) {
        this.reposervice = reposervice;
    }

    public void kaydet() {
        int i = 0;
        String sonuc;
        try {
            i = reposervice.kaydetKullanici(kullanici,loginView.getSessionUser());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kullaniciList = reposervice.listeleKullaniciListesi();
            if (i > 0) {
                sonuc = "Kaydetme başarılı!";
            } else {
                sonuc = "Kaydetme başarısız!";
            }
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Kullanıcı Kaydetme İşlemi", sonuc));
        kullanici = new Kullanici();

    }

    public void guncelle() {
        int i = 0;
        String sonuc;
        kullanici = selectedkullanici;
        i = reposervice.guncelleKullanici(kullanici, loginView.getSessionUser());
        if (i > 0) {
            sonuc = "Güncelleme başarılı!";
        } else {
            sonuc = "Güncelleme başarısız!";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Kullanıcı Güncelleme İşlemi", sonuc));
        panelgizle();
    }

    public void panelgoster() {

        gizle = true;
    }

    public void panelgizle() {
        gizle = false;
    }

}
