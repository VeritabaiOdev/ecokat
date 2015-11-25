
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.gov.eba.projeyonetim.service.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import tr.gov.eba.projeyonetim.db.DBConnectionManager;
import tr.gov.eba.projeyonetim.db.service.RepoService;
import tr.gov.eba.projeyonetim.entity.Baskanlik;
import tr.gov.eba.projeyonetim.entity.Bilesen;
import tr.gov.eba.projeyonetim.entity.Kullanici;
import tr.gov.eba.projeyonetim.entity.KullaniciTuru;
import tr.gov.eba.projeyonetim.entity.ProjeDurum;
import tr.gov.eba.projeyonetim.entity.Project;
import tr.gov.eba.projeyonetim.entity.ProjeSorumlu;
import tr.gov.eba.projeyonetim.interfaces.LogUtils;
import tr.gov.eba.projeyonetim.utils.FpyConstants;
import tr.gov.eba.projeyonetim.utils.LogUtilsImpl;

/**
 *
 * @author Peynir
 */
public class RepoServiceImpl implements RepoService, Serializable {

    LogUtils utils;

    public RepoServiceImpl() {
        utils = new LogUtilsImpl();

    }

    //KullanÃ„Â±cÃ„Â± TÃƒÂ¼rÃƒÂ¼
    @Override
    public int kaydetKullaniciTuru(KullaniciTuru kullaniciTuru, Kullanici kullanici) {

        DBConnectionManager db = new DBConnectionManager();
        Date date = new java.util.Date();
        Timestamp olusturma_zamani = new Timestamp(date.getTime());
        int i = 0;

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();

            ps = connection.prepareStatement("INSERT INTO `kullanici_turleri` (tur_adi,olusturma_zamani) VALUES(?,?)");

            ps.setString(1, kullaniciTuru.getTur_adi());
            ps.setTimestamp(2, olusturma_zamani);
            i = ps.executeUpdate();
            if (i > 0) {
                ps = connection.prepareStatement("INSERT INTO islem_kaydi (islem_tipi,kullanici_id,islem_detay,islem_tarihi) VALUES(?,?,?,?)");

                ps.setInt(1, FpyConstants.KULLANICI_TUR_KAYDET);
                ps.setInt(2, kullanici.getKullanici_id());
                ps.setString(3, kullanici.getKullanici_adi() );
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                i = ps.executeUpdate();
            }

        } catch (Exception ex) {
            utils.insertErrorLog("kaydetKullaniciTuru", ex);
        } finally {
            db.closePSStatement(ps);
            db.closeConnection();
        }

        return i;

    }

    @Override
    public List listeleKullaniciTurleriListesi() {
        DBConnectionManager db = new DBConnectionManager();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        KullaniciTuru kullaniciTuru = null;
        List<KullaniciTuru> kullaniciTurleriList = null;
        kullaniciTurleriList = new ArrayList<KullaniciTuru>();

        try {
            connection = db.getConnection();
            String selectSQL = "SELECT * FROM `kullanici_turleri`";
            ps = connection.prepareStatement(selectSQL);
            rs = ps.executeQuery(selectSQL);

            while (rs.next()) {

                kullaniciTuru = new KullaniciTuru();
                kullaniciTuru.setTur_id(rs.getInt("tur_id"));
                kullaniciTuru.setTur_adi(rs.getString("tur_adi"));
                kullaniciTuru.setDurum(rs.getInt("durum"));
                kullaniciTuru.setOlusturma_zamani(rs.getTimestamp("olusturma_zamani"));
                kullaniciTuru.setGuncelleme_zamani(rs.getTimestamp("guncelleme_zamani"));
                kullaniciTurleriList.add(kullaniciTuru);

            }

        } catch (Exception ex) {
            utils.insertErrorLog("listeleKullaniciTurleriListesi", ex);
        } finally {

            db.closeResultSet(rs);
            db.closePSStatement(ps);
            db.closeConnection();
        }

        return kullaniciTurleriList;

    }

    @Override
    public int guncelleKullaniciTuru(KullaniciTuru kullaniciTuru, Kullanici kullanici) {
        int i = 0;
        DBConnectionManager db = new DBConnectionManager();
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();
            String selectSQL = "UPDATE kullanici_turleri SET tur_adi = ?, durum = ? WHERE tur_id = ?";
            ps = connection.prepareStatement(selectSQL);
            ps.setString(1, kullaniciTuru.getTur_adi());
            ps.setInt(2, kullaniciTuru.getDurum());
            ps.setInt(3, kullaniciTuru.getTur_id());

            i = ps.executeUpdate();
            if (i > 0) {
                ps = connection.prepareStatement("INSERT INTO islem_kaydi (islem_tipi,kullanici_id,islem_detay,islem_tarihi) VALUES(?,?,?,?)");

                ps.setInt(1, FpyConstants.KULLANICI_TUR_GUNCELLE);
                ps.setInt(2, kullanici.getKullanici_id());
                ps.setString(3, kullanici.getKullanici_adi() );
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                i = ps.executeUpdate();
            }

        } catch (Exception ex) {
            utils.insertErrorLog("guncelleKullaniciTurleriListesi", ex);
        } finally {

            db.closePSStatement(ps);
            db.closeConnection();
        }

        return i;
    }

    //BaskanlÄ±k kaydetme
    @Override
    public int kaydetBaskanlik(Baskanlik baskanlik) {

        DBConnectionManager db = new DBConnectionManager();
        int i = 0;

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();

            ps = connection.prepareStatement("INSERT INTO `baskanlik` (baskanlik_adi,yonetici_adi,olusturma_zamani) VALUES(?,?,?)");

            ps.setString(1, baskanlik.getBaskanlikAdi());
            ps.setString(2, baskanlik.getYoneticiAdi());
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            i = ps.executeUpdate();

            db.closePSStatement(ps);
            db.closeConnection();

        } catch (Exception ex) {
            utils.insertErrorLog("guncelleProjeDurum", ex);
          } finally {
            db.closePSStatement(ps);
            db.closeConnection();
        }
        return i;
    }

    //ProjeDurumKaydetme
    @Override
    public int kaydetProjeDurum(ProjeDurum projeDurum, Kullanici kullanici) {

        DBConnectionManager db = new DBConnectionManager();

        int i = 0;

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try {

            connection = db.getConnection();
            ps = connection.prepareStatement("INSERT INTO proje_durum (renk,durum_adi,renk_kodu,olusturma_zamani) VALUES(?,?,?,?)");

            ps.setString(1, projeDurum.getRenk());
            ps.setString(2, projeDurum.getDurum_adi());
            ps.setString(3, projeDurum.getRenk_kodu());
            ps.setTimestamp(4, new Timestamp(new Date().getTime()));
            i = ps.executeUpdate();
            if (i > 0) {
                ps = connection.prepareStatement("INSERT INTO islem_kaydi (islem_tipi,kullanici_id,islem_detay,islem_tarihi) VALUES(?,?,?,?)");

                ps.setInt(1, FpyConstants.PROJE_DURUM_KAYDET);
                ps.setInt(2, kullanici.getKullanici_id());
                ps.setString(3, projeDurum.getDurum_adi());
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                i = ps.executeUpdate();
            }
            db.closePSStatement(ps);
            db.closeConnection();
        } catch (Exception ex) {
            utils.insertErrorLog("kaydetProjeDurum", ex);

        } finally {
            db.closePSStatement(ps);
            db.closeConnection();
        }

        return i;
    }
    //ProjeDurumListeleme
    @Override
    public List listeleProjeDurumListesi() {
        DBConnectionManager db = new DBConnectionManager();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProjeDurum projeDurum = null;
        List<ProjeDurum> projeDurumList = null;
        projeDurumList = new ArrayList<ProjeDurum>();

        try {
            connection = db.getConnection();
            String selectSQL = "SELECT * FROM proje_durum";
            ps = connection.prepareStatement(selectSQL);
            // execute select SQL stetement
            rs = ps.executeQuery(selectSQL);

            while (rs.next()) {

                projeDurum = new ProjeDurum();
                projeDurum.setDurum(rs.getInt("durum"));
                projeDurum.setProje_durum_id(rs.getInt("proje_durum_id"));
                projeDurum.setRenk(rs.getString("renk"));
                projeDurum.setDurum_adi(rs.getString("durum_adi"));
                projeDurum.setRenk_kodu(rs.getString("renk_kodu"));
                projeDurum.setOlusturma_zamani(rs.getTimestamp("olusturma_zamani"));
                projeDurum.setGuncelleme_zamani(rs.getTimestamp("guncelleme_zamani"));
                projeDurumList.add(projeDurum);

            }

        } catch (Exception ex) {
            utils.insertErrorLog("listeleProjeDurumListesi", ex);
        } finally {

            db.closeResultSet(rs);
            db.closePSStatement(ps);
            db.closeConnection();
        }

        return projeDurumList;

    }

    //ProjeDurumguncelleme
    @Override
    public int guncelleProjeDurum(ProjeDurum projeDurum) {
        //UPDATE `Proje Durum` SET renk = "mavi" WHERE proje_durum_id = "1"
        int i = 0;
        DBConnectionManager db = new DBConnectionManager();
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();
            String selectSQL = "UPDATE `proje_durum` SET  durum=? , renk = ? , durum_adi=?, renk_kodu = ?,  guncelleme_zamani=? WHERE proje_durum_id = ?";
            ps = connection.prepareStatement(selectSQL);
            ps.setInt(1, projeDurum.getDurum());
            ps.setString(2, projeDurum.getRenk());
            ps.setString(3, projeDurum.getDurum_adi());
            ps.setString(4, projeDurum.getRenk_kodu());
            ps.setTimestamp(5, new Timestamp(new Date().getTime()));
            ps.setInt(6, projeDurum.getProje_durum_id());

            i = ps.executeUpdate();

        } catch (Exception ex) {
            utils.insertErrorLog("guncelleProjeDurum", ex);

        } finally {

            db.closePSStatement(ps);
            db.closeConnection();
        }

        return i;

    }

    // baskanlikListeleme

    @Override
    public List listeleBaskanlik() {
        DBConnectionManager db = new DBConnectionManager();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Baskanlik baskanlik = null;
        List<Baskanlik> baskanlikList = null;
        baskanlikList = new ArrayList<Baskanlik>();

        try {
            connection = db.getConnection();
            String selectSQL = "SELECT * FROM `baskanlik`";
            ps = connection.prepareStatement(selectSQL);
            // execute select SQL stetement
            rs = ps.executeQuery(selectSQL);

            while (rs.next()) {
                baskanlik = new Baskanlik();
                baskanlik.setBaskanlik_id(rs.getInt("baskanlik_id"));
                baskanlik.setBaskanlikDurum(rs.getInt("durum"));
                baskanlik.setBaskanlikAdi(rs.getString("baskanlik_adi"));
                baskanlik.setYoneticiAdi(rs.getString("yonetici_adi"));
                baskanlik.setOlusturma_zamani(rs.getTimestamp("olusturma_zamani"));
                baskanlik.setGuncelleme_zamani(rs.getTimestamp("guncelleme_zamani"));
                baskanlikList.add(baskanlik);
            }

        } catch (Exception ex) {
            utils.insertErrorLog("guncelleProjeDurum", ex);
        } finally {
            db.closeResultSet(rs);
            db.closePSStatement(ps);
            db.closeConnection();
        }
        return baskanlikList;
    }

    // baskanlik guncelleme

    @Override
    public int guncelleBaskanlik(Baskanlik baskanlik) {
        //"UPDATE `Baskanlik` SET baskanlik_adi = SistemDaireBaÅŸkanlÄ±ÄŸÄ±, yonetici_adi = OÄŸuzhan WHERE baskanlik_id = 2";
        int i = 0;
        DBConnectionManager db = new DBConnectionManager();
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();
            String selectSQL = "UPDATE `baskanlik` SET baskanlik_adi = ?, yonetici_adi = ?, guncelleme_zamani = ?, durum = ? WHERE baskanlik_id = ?";
            ps = connection.prepareStatement(selectSQL);
            ps.setString(1, baskanlik.getBaskanlikAdi());
            ps.setString(2, baskanlik.getYoneticiAdi());
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setInt(4, baskanlik.getBaskanlikDurum());
            ps.setInt(5, baskanlik.getBaskanlik_id());

            i = ps.executeUpdate();

        } catch (Exception ex) {
            utils.insertErrorLog("guncelleProjeDurum", ex);
        } finally {

            db.closePSStatement(ps);
            db.closeConnection();
        }
        return i;
    }

    //Bilesen
    @Override
    public int kaydetBilesen(Bilesen bilesen,Kullanici kullanici) {

        DBConnectionManager db = new DBConnectionManager();
        int i = 0;
        Date date = new java.util.Date();
        Timestamp olusturmaZamani = new Timestamp(date.getTime());

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();

            ps = connection.prepareStatement("INSERT INTO bilesen (bilesen_adi,olusturma_zamani) VALUES(?,?)");

            ps.setString(1, bilesen.getAdi());
            ps.setTimestamp(2, olusturmaZamani);
            i = ps.executeUpdate();
            if (i > 0) {
                ps = connection.prepareStatement("INSERT INTO islem_kaydi (islem_tipi,kullanici_id,islem_detay,islem_tarihi) VALUES(?,?,?,?)");

                ps.setInt(1, FpyConstants.KULLANICI_KAYDET);
                ps.setInt(2, kullanici.getKullanici_id());
                ps.setString(3, kullanici.getKullanici_adi() );
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                i = ps.executeUpdate();
            }

            db.closePSStatement(ps);
            db.closeConnection();
            

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            db.closePSStatement(ps);
            db.closeConnection();
        }
        return i;

    }

    @Override
    public List listeleKullaniciListesi() {
        DBConnectionManager db = new DBConnectionManager();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Kullanici kullanici = null;
        KullaniciTuru kullaniciTur = null;
        Baskanlik baskanlik = null;
        List<Kullanici> kullaniciList = null;
        kullaniciList = new ArrayList<Kullanici>();

        try {
            connection = db.getConnection();
            String selectSQL = "SELECT *,k.olusturma_zamani as kolusturma,k.guncelleme_zamani as kguncelleme FROM"
                    + " kullanici k LEFT JOIN baskanlik b ON k.k_baskanlik_id = b.baskanlik_id "
                    + "LEFT JOIN kullanici_turleri kt on k.tur_id = kt.tur_id;";
            ps = connection.prepareStatement(selectSQL);
            rs = ps.executeQuery(selectSQL);

            while (rs.next()) {

                kullanici = new Kullanici();
                kullaniciTur = new KullaniciTuru();
                baskanlik = new Baskanlik();

                kullanici.setKullanici_id(rs.getInt("k.kullanici_id"));
                kullanici.setTc(rs.getLong("tc"));
                kullanici.setKullanici_adi(rs.getString("kullanici_adi"));
                kullanici.setDurum(rs.getInt("durum"));

                kullaniciTur.setTur_id(rs.getInt("tur_id"));
                kullaniciTur.setTur_adi(rs.getString("tur_adi"));
                kullaniciTur.setGuncelleme_zamani(rs.getTimestamp("guncelleme_zamani"));
                kullaniciTur.setOlusturma_zamani(rs.getTimestamp("olusturma_zamani"));
                kullanici.setTur(kullaniciTur);

                kullanici.setIsim(rs.getString("isim"));
                kullanici.setSoyisim(rs.getString("soyisim"));

                baskanlik.setBaskanlik_id(rs.getInt("baskanlik_id"));
                baskanlik.setBaskanlikAdi(rs.getString("baskanlik_adi"));
                baskanlik.setYoneticiAdi(rs.getString("yonetici_adi"));
                //baskanlik.setOlusturma_zamani(rs.getTimestamp("olusturma_zamani"));
                //baskanlik.setGuncelleme_zamani(rs.getTimestamp("guncelleme_zamani"));
                kullanici.setBaskanlik(baskanlik);

                kullanici.setEmail(rs.getString("email"));
                kullanici.setTelefon(rs.getString("telefon"));
                kullanici.setOlusturma_zamani(rs.getTimestamp("kolusturma"));
                kullanici.setGuncelleme_zamani(rs.getTimestamp("kguncelleme"));
                kullaniciList.add(kullanici);

            }

        } catch (Exception ex) {
            utils.insertErrorLog("listeleKullaniciListesi", ex);
        } finally {

            db.closeResultSet(rs);
            db.closePSStatement(ps);
            db.closeConnection();
        }

        return kullaniciList;

    }

    @Override
    public int guncelleKullanici(Kullanici kullanici, Kullanici kullanici2) {
        int i = 0;
        DBConnectionManager db = new DBConnectionManager();
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();
            String selectSQL = "UPDATE `kullanici` SET isim = ?, soyisim = ?, tur_id = ?, k_baskanlik_id = ?, email = ?, telefon = ?, durum = ? WHERE tc = ?";
            ps = connection.prepareStatement(selectSQL);
            ps.setString(1, kullanici.getIsim());
            ps.setString(2, kullanici.getSoyisim());
            ps.setInt(3, kullanici.getTur().getTur_id());
            ps.setInt(4, kullanici.getBaskanlik().getBaskanlik_id());
            ps.setString(5, kullanici.getEmail());
            ps.setString(6, kullanici.getTelefon());
            ps.setInt(7, kullanici.getDurum());
            ps.setLong(8, kullanici.getTc());

            i = ps.executeUpdate();
            if (i > 0) {
                ps = connection.prepareStatement("INSERT INTO islem_kaydi (islem_tipi,kullanici_id,islem_detay,islem_tarihi) VALUES(?,?,?,?)");

                ps.setInt(1, FpyConstants.KULLANICI_GUNCELLE);
                ps.setInt(2, kullanici2.getKullanici_id());
                ps.setString(3, kullanici2.getKullanici_adi() );
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                i = ps.executeUpdate();
            }

        } catch (Exception ex) {
            utils.insertErrorLog("guncelleKullanici", ex);
        } finally {
            db.closePSStatement(ps);
            db.closeConnection();
        }

        return i;
    }

    //proje kaydetme
    public int kaydetProje(Project proje) {

        DBConnectionManager db = new DBConnectionManager();

        int i = 0;

        Connection connection = null;
        PreparedStatement ps = null;

        try {

            connection = db.getConnection();
            ps = connection.prepareStatement("INSERT INTO proje (proje_adi,proje_tanimi,baskanlik_id,proje_durum_id,olusturma_zamani) VALUES(?,?,?,?,?)");

            ps.setString(1, proje.getProje_adi());
            ps.setString(2, proje.getProje_tanimi());
            ps.setInt(3, proje.getBaskanlik().getBaskanlik_id());
            ps.setInt(4, proje.getProjeDurum().getProje_durum_id());
            ps.setTimestamp(5, new Timestamp(new Date().getTime()));
            i = ps.executeUpdate();

            db.closePSStatement(ps);
            db.closeConnection();
        } catch (Exception ex) {
            utils.insertErrorLog("kaydetProje", ex);
           
        } finally {
            db.closePSStatement(ps);
            db.closeConnection();
        }
        return i;

    }

    //proje listeleme

    @Override
    public List listeleProjeListesi() {

        DBConnectionManager db = new DBConnectionManager();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProjeDurum projeDurum = null;
        List<Project> projeList = null;
        projeList = new ArrayList<Project>();

        try {
            connection = db.getConnection();
            String selectSQL = "SELECT p.*, b.baskanlik_adi, b.yonetici_adi,pd.renk, pd.durum_adi,pd.renk_kodu "
                    + "FROM proje AS p "
                    + "LEFT JOIN baskanlik AS b ON p.baskanlik_id = b.baskanlik_id "
                    + "LEFT JOIN proje_durum AS pd ON p.proje_durum_id = pd.proje_durum_id";
            ps = connection.prepareStatement(selectSQL);
            // execute select SQL stetement
            rs = ps.executeQuery(selectSQL);

            Project proje;
            Baskanlik baskanlik;

            while (rs.next()) {

                proje = new Project();
                proje.setProje_adi(rs.getString("proje_adi"));
                proje.setProje_tanimi(rs.getString("proje_tanimi"));
                proje.setProje_id(rs.getInt("proje_id"));
                proje.setDurum(rs.getInt("durum"));
                baskanlik = new Baskanlik();
                baskanlik.setBaskanlik_id(rs.getInt("baskanlik_id"));
                baskanlik.setBaskanlikAdi(rs.getString("baskanlik_adi"));
                baskanlik.setYoneticiAdi(rs.getString("yonetici_adi"));
                proje.setBaskanlik(baskanlik);
                projeDurum = new ProjeDurum();
                projeDurum.setProje_durum_id(rs.getInt("proje_durum_id"));
                projeDurum.setDurum_adi(rs.getString("durum_adi"));
                projeDurum.setRenk(rs.getString("renk"));
                projeDurum.setRenk_kodu(rs.getString("renk_kodu"));
                proje.setProjeDurum(projeDurum);
                proje.setOlusturma_zamani(rs.getTimestamp("olusturma_zamani"));
                proje.setGuncelleme_zamani(rs.getTimestamp("guncelleme_zamani"));
                
                List<Kullanici> kListe = sorumluKullaniciListele(proje.getProje_id(), connection);
                proje.setSorumlulari(kListe);
                
                projeList.add(proje);

            }

        } catch (Exception ex) {
            utils.insertErrorLog("listeleProjeListesi", ex);
            
        } finally {

            db.closeResultSet(rs);
            db.closePSStatement(ps);
            db.closeConnection();
        }

        return projeList;

    }
        
     public List<Kullanici> sorumluKullaniciListele(int projeId, Connection connection) throws SQLException 
     {      

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Kullanici> kullanici = new ArrayList<Kullanici>();

            String selectSQL = "SELECT k.isim, k.soyisim FROM kullanici as k "
                    + "LEFT JOIN proje_sorumlulari ps ON (k.kullanici_id=ps.ps_kullanici_id) "
                    + "WHERE ps.ps_proje_id="+projeId;
            ps = connection.prepareStatement(selectSQL);
           
            rs = ps.executeQuery(selectSQL);
              Kullanici k ;
            while (rs.next()) 
            {
                
                k = new Kullanici();
                k.setIsim(rs.getString("isim"));
                k.setSoyisim(rs.getString("soyisim"));  
                kullanici.add(k);
 
            } 
      
        return kullanici;
 
     }
        
        
    
    //proje guncelleme 
    @Override
    public int guncelleProje(Project proje) {

        int i = 0;
        DBConnectionManager db = new DBConnectionManager();
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();
            String selectSQL = "UPDATE `proje` SET durum=? , proje_adi=?, proje_tanimi=?, baskanlik_id=?, proje_durum_id=? WHERE proje_id = ?";
            ps = connection.prepareStatement(selectSQL);
            ps.setInt(1, proje.getDurum());
            ps.setString(2, proje.getProje_adi());
            ps.setString(3, proje.getProje_tanimi());
            ps.setInt(4, proje.getBaskanlik().getBaskanlik_id());
            ps.setInt(5, proje.getProjeDurum().getProje_durum_id());
            ps.setInt(6, proje.getProje_id());

            i = ps.executeUpdate();

        } catch (Exception ex) {
             utils.insertErrorLog("guncelleProje", ex);
            
        } finally {

            db.closePSStatement(ps);
            db.closeConnection();
        }

        return i;

    }

    //KullanÄ±cÄ±
    @Override
    public int kaydetKullanici(Kullanici kullanici, Kullanici kullanici2) {

        DBConnectionManager db = new DBConnectionManager();
        Date date = new java.util.Date();
        Timestamp olusturma_zamani = new Timestamp(date.getTime());
        int i = 0;

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();

            ps = connection.prepareStatement("INSERT INTO `kullanici` (tc,tur_id,kullanici_adi,isim,soyisim,k_baskanlik_id,email,telefon,olusturma_zamani) VALUES(?,?,?,?,?,?,?,?,?)");

            ps.setLong(1, kullanici.getTc());
            ps.setInt(2, kullanici.getTur().getTur_id());
            ps.setString(3, kullanici.getKullanici_adi());
            ps.setString(4, kullanici.getIsim());
            ps.setString(5, kullanici.getSoyisim());
            ps.setInt(6, kullanici.getBaskanlik().getBaskanlik_id());
            ps.setString(7, kullanici.getEmail());
            ps.setString(8, kullanici.getTelefon());
            ps.setTimestamp(9, olusturma_zamani);
            i = ps.executeUpdate();
            if (i > 0) {
                ps = connection.prepareStatement("INSERT INTO islem_kaydi (islem_tipi,kullanici_id,islem_detay,islem_tarihi) VALUES(?,?,?,?)");

                ps.setInt(1, FpyConstants.KULLANICI_KAYDET);
                ps.setInt(2, kullanici2.getKullanici_id());
                ps.setString(3, kullanici2.getKullanici_adi() );
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                i = ps.executeUpdate();
            }

        } catch (Exception ex) {
               utils.insertErrorLog("kaydetKullanici", ex);
            
        } finally {
            db.closePSStatement(ps);
            db.closeConnection();
        }

        return i;

    }

    public List listeleBilesen() {
        DBConnectionManager db = new DBConnectionManager();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Bilesen bilesen = null;
        List<Bilesen> bilesenList = null;
        bilesenList = new ArrayList<Bilesen>();

        try {
            connection = db.getConnection();
            String selectSQL = "SELECT * FROM bilesen";
            ps = connection.prepareStatement(selectSQL);
            // execute select SQL stetement
            rs = ps.executeQuery(selectSQL);

            while (rs.next()) {

                bilesen = new Bilesen();
                bilesen.setId(rs.getInt("bilesen_id"));
                bilesen.setAdi(rs.getString("bilesen_adi"));
                bilesen.setGuncellemeZamani(rs.getTimestamp("guncelleme_zamani"));
                bilesen.setOlusturmaZamani(rs.getTimestamp("olusturma_zamani"));
                bilesen.setDurum(rs.getInt("durum"));
                bilesenList.add(bilesen);

            }

        } catch (Exception ex) {
            utils.insertErrorLog("listeleBilesen", ex);
        } finally {

            db.closeResultSet(rs);
            db.closePSStatement(ps);
            db.closeConnection();

        }

        return bilesenList;

    }

    public int guncelleBilesen(Bilesen bilesen,Kullanici kullanici) {

        int i = 0;
        DBConnectionManager db = new DBConnectionManager();
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();
            String selectSQL = "UPDATE `bilesen` SET durum = ?, bilesen_adi = ? WHERE bilesen_id = ?";
            ps = connection.prepareStatement(selectSQL);
            ps.setInt(1, bilesen.getDurum());
            ps.setString(2, bilesen.getAdi());
            ps.setInt(3, bilesen.getId());

            i = ps.executeUpdate();
            if (i > 0) {
                ps = connection.prepareStatement("INSERT INTO islem_kaydi (islem_tipi,kullanici_id,islem_detay,islem_tarihi) VALUES(?,?,?,?)");

                ps.setInt(1, FpyConstants.KULLANICI_KAYDET);
                ps.setInt(2, kullanici.getKullanici_id());
                ps.setString(3, kullanici.getKullanici_adi() );
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                i = ps.executeUpdate();
            }

        } catch (Exception ex) {
            utils.insertErrorLog("guncelleBilesen", ex);
            
        } finally {
            db.closePSStatement(ps);
            db.closeConnection();
        }

        return i;

    }

    //ProjeSorumlu
    public int kaydetProjeSorumlu(ProjeSorumlu projesorumlu) {

        DBConnectionManager db = new DBConnectionManager();
        Date date = new java.util.Date();
        Timestamp olusturma_zamani = new Timestamp(date.getTime());
        int i = 0;

        Connection connection = null;
        PreparedStatement ps = null;
        for(int l=0;l<projesorumlu.getKullaniciList().size();l++){
        try {
            connection = db.getConnection();

            Kullanici kullanici = new Kullanici();

            ps = connection.prepareStatement("INSERT INTO proje_sorumlulari (ps_proje_id,ps_kullanici_id,olusturma_zamani) VALUES(?,?,?)");

          ps.setInt(1, projesorumlu.getProje().getProje_id());
            ps.setInt(2, projesorumlu.getKullaniciList().get(l).getKullanici_id());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

           
            i = ps.executeUpdate();

        } catch (Exception ex) {
            utils.insertErrorLog("kaydetProjeSorumlu", ex);
        } 
         finally {
            db.closePSStatement(ps);
            db.closeConnection();
        }
        }
        return i;

    }

    public List listeleProjeSorumlu() {

        DBConnectionManager db = new DBConnectionManager();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProjeSorumlu projesorumlu = null;
        Kullanici kullanici = null;
        Project proje = null;
        List<ProjeSorumlu> projesorumluList = null;
        projesorumluList = new ArrayList<ProjeSorumlu>();

        try {
            connection = db.getConnection();
            String selectSQL = "SELECT *,ps.olusturma_zamani as psolusturma,ps.guncelleme_zamani as psguncelleme "
                    + "FROM proje_sorumlulari ps LEFT JOIN proje p ON ps.ps_proje_id = p.proje_id "
                    + "LEFT JOIN kullanici k on ps.ps_kullanici_id = k.kullanici_id;";
            ps = connection.prepareStatement(selectSQL);
            rs = ps.executeQuery(selectSQL);

            while (rs.next()) {
                projesorumlu = new ProjeSorumlu();
                proje = new Project();
                kullanici = new Kullanici();

                proje.setProje_id(rs.getInt("ps_proje_id"));
                proje.setProje_adi(rs.getString("proje_adi"));
                proje.setProje_tanimi(rs.getString("proje_tanimi"));
                projesorumlu.setProje(proje);
                
                kullanici.setKullanici_id(rs.getInt("kullanici_id"));
                kullanici.setTc(rs.getLong("tc"));
                kullanici.setIsim(rs.getString("isim"));
                kullanici.setSoyisim(rs.getString("soyisim"));
                kullanici.setTelefon(rs.getString("telefon"));
                
                projesorumlu.setKullanici(kullanici);
            }
                
                

        
        } catch (Exception ex) {
            utils.insertErrorLog("listeleProjeSorumlu", ex);
        }
         finally {
           
                db.closeResultSet(rs);
                db.closePSStatement(ps);
                db.closeConnection();
            
        }

        return projesorumluList;
    }

    @Override
    public int guncelleProjeSorumlu(ProjeSorumlu projesorumlu) {
        int i = 0;
        DBConnectionManager db = new DBConnectionManager();
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();
            String selectSQL = "UPDATE proje_sorumlulari SET proje_adi = ?, kullanici_adi = ?, durum = ? ";
            ps = connection.prepareStatement(selectSQL);
            ps.setString(1, projesorumlu.getProje().getProje_adi());
            ps.setString(2, projesorumlu.getKullanici().getIsim());
            ps.setInt(3, projesorumlu.getDurum());
            
            

            i = ps.executeUpdate();
            

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.closePSStatement(ps);
            db.closeConnection();
        }

        return i;
            
            

        

        
    }
    
    @Override
    public Kullanici getirKullanici (String kullaniciAdi){
        
        int i = 0;
        Kullanici kullanici = null;
        KullaniciTuru kullanicitur = null;
        DBConnectionManager db = new DBConnectionManager();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            
            connection = db.getConnection();
            String selectSQL = "SELECT * FROM kullanici as k LEFT JOIN kullanici_turleri as kt on (k.tur_id=kt.tur_id) WHERE kullanici_adi = '"+kullaniciAdi+"'";
            ps = connection.prepareStatement(selectSQL);
            rs = ps.executeQuery(selectSQL);
            

            
            while (rs.next()) {
               kullanici = new Kullanici();
               kullanicitur = new KullaniciTuru();
               kullanici.setKullanici_id(rs.getInt("kullanici_id"));
               kullanici.setKullanici_adi(rs.getString("kullanici_adi"));
               kullanici.setIsim(rs.getString("isim"));
               kullanici.setSoyisim(rs.getString("soyisim"));
               kullanici.setEmail(rs.getString("email"));
               kullanici.setTc(rs.getLong("tc"));
               
               kullanicitur.setTur_id(rs.getInt("tur_id"));
               kullanicitur.setTur_adi(rs.getString("tur_adi"));
               kullanici.setTur(kullanicitur);
               
            }

        } catch (Exception ex) {
            utils.insertErrorLog("getirKullanici", ex);
        } finally {
            db.closeResultSet(rs);
            db.closePSStatement(ps);
            db.closeConnection();
        }
        return kullanici;
    }

    @Override
    public List listeleBaskanlik(String basAdi, String yoneticiAdi, Date olusturmaZamaniBaslangic, Date olusturmaZamaniBitis, int durum){
        Baskanlik baskanlik;
        DBConnectionManager db = new DBConnectionManager();
        int i = 0;
        boolean flag = false;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Baskanlik> baskanlikList = null;
        baskanlikList = new ArrayList<Baskanlik>();

        try {
            connection = db.getConnection();
            StringBuilder selectSQL = new StringBuilder();
            selectSQL.append("SELECT * FROM `baskanlik` ");
            
            if  (
                    (basAdi !=null && !basAdi.equalsIgnoreCase("")) 
                    || (yoneticiAdi !=null && !yoneticiAdi.equalsIgnoreCase("")) 
                    || (durum != -1)
                    || (olusturmaZamaniBaslangic != null)
                    || (olusturmaZamaniBitis != null)
                ){
                selectSQL.append("WHERE ");
            }
            else
            {
                System.out.println("NULL --- OĞUZHAN");
                return null;
            }
            
            if(basAdi !=null && !basAdi.equalsIgnoreCase("")) {
                selectSQL.append(" 'baskanlik_adi' LIKE '%"+basAdi+"%' ");
                flag = true;
            }
            
            if(yoneticiAdi !=null && !yoneticiAdi.equalsIgnoreCase("")) {
                if(flag == true)
                {
                    selectSQL.append(" AND ");
                }
                selectSQL.append(" 'yonetici_adi' LIKE '%"+yoneticiAdi+"%' ");
                flag = true;
            }            
            
            if(durum != -1) {
                if(flag == true)
                {
                    selectSQL.append(" AND ");
                }
                selectSQL.append(" 'durum' LIKE '%"+durum+"%' ");
                flag = true;
            }
            
            if(olusturmaZamaniBaslangic != null) {
                if(flag == true)
                {
                    selectSQL.append(" AND ");
                }                
                selectSQL.append(" 'olusturma_zamani' >= '%"+olusturmaZamaniBaslangic+"%' ");
                flag = true;                
            }
            
            if(olusturmaZamaniBitis != null) {
                if(flag == true)
                {
                    selectSQL.append(" AND ");
                }                
                selectSQL.append(" 'olusturma_zamani' <= '%"+olusturmaZamaniBitis+"%' ");
                flag = true;
            }        
                        
            ps = connection.prepareStatement(selectSQL.toString());
            rs = ps.executeQuery();
            // execute select SQL stetement
            while (rs.next()) {
                baskanlik = new Baskanlik();
                baskanlik.setBaskanlik_id(rs.getInt("baskanlik_id"));
                baskanlik.setBaskanlikDurum(rs.getInt("durum"));
                baskanlik.setBaskanlikAdi(rs.getString("baskanlik_adi"));
                baskanlik.setYoneticiAdi(rs.getString("yonetici_adi"));
                baskanlik.setOlusturma_zamani(rs.getTimestamp("olusturma_zamani"));
                baskanlik.setGuncelleme_zamani(rs.getTimestamp("guncelleme_zamani"));
                baskanlikList.add(baskanlik);  
            }  
        } catch (Exception ex) {
           utils.insertErrorLog("guncelleProjeDurum", ex);
        } finally {
            db.closeResultSet(rs);
            db.closePSStatement(ps);
            db.closeConnection();
        }
        return baskanlikList;
    }
}
