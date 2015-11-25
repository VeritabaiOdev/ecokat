/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.db.service;

import java.util.Date;
import java.util.List;
import tr.gov.eba.projeyonetim.entity.Baskanlik;
import tr.gov.eba.projeyonetim.entity.Bilesen;
import tr.gov.eba.projeyonetim.entity.Kullanici;
import tr.gov.eba.projeyonetim.entity.KullaniciTuru;
import tr.gov.eba.projeyonetim.entity.ProjeDurum;
import tr.gov.eba.projeyonetim.entity.Project;
import tr.gov.eba.projeyonetim.entity.ProjeSorumlu;

/**
 *
 * @author Peynir
 */
public interface RepoService {

    //Kullanıcı türü
    public int kaydetKullaniciTuru(KullaniciTuru kullaniciTuru, Kullanici kullanici);

    public List listeleKullaniciTurleriListesi();

    public int guncelleKullaniciTuru(KullaniciTuru kullaniciTuru, Kullanici kullanici);

    //Bilesen
    public int kaydetBilesen(Bilesen bilesen,Kullanici kullanici);

    //Baskanlik
    public int kaydetBaskanlik(Baskanlik baskanlik);

    public List listeleBaskanlik();

    public int guncelleBaskanlik(Baskanlik baskanlik);
 
    public List listeleBaskanlik(String baskanlikAdi, String yoneticiAdi, Date olusturmaZamaniBaslangic, Date olusturmaZamaniBitis, int durum);

    //Kullanıcı
    public int kaydetKullanici(Kullanici kullanici, Kullanici kullanici2);

    public List listeleKullaniciListesi();

    public int guncelleKullanici(Kullanici kullanici, Kullanici kullanici2);

    //ProjeDurum
    public int kaydetProjeDurum(ProjeDurum projeDurum, Kullanici kullanici);

    public List listeleProjeDurumListesi();

    public int guncelleProjeDurum(ProjeDurum projeDurum);

  
    //Proje
    public int kaydetProje(Project proje);
     
    public List listeleProjeListesi();

    public int guncelleProje(Project proje);
    //projesorumlu
    public int kaydetProjeSorumlu(ProjeSorumlu projesorumlu);

    public List listeleProjeSorumlu();

    public int guncelleProjeSorumlu(ProjeSorumlu projesorumlu);
    //LoginView
    public Kullanici getirKullanici (String kullaniciAdi);
    
}
