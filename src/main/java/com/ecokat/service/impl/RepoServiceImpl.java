
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.service.impl;

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
import com.ecokat.db.DBConnectionManager;
import com.ecokat.db.service.RepoService;
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
public class RepoServiceImpl implements RepoService, Serializable {

    @Override
    public int registerUser(User user) {
        
        DBConnectionManager db = new DBConnectionManager();
        Date date = new java.util.Date();
        Timestamp olusturma_zamani = new Timestamp(date.getTime());
        int i = 0;

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = db.getConnection();

            ps = connection.prepareStatement("INSERT INTO `user` (first_name,last_name,adress,telephone,country,password,mail,birth_date) VALUES(?,?,?,?,?,?,?,?,?)");

            ps.setString(1, user.getName());
            ps.setString(2, user.getSurName());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getTelNo());
            ps.setString(5, user.getCountry());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getMail());
            ps.setString(8, user.getBirthDay());
            ps.setTimestamp(9, olusturma_zamani);
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
    public List listUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int updateUser(User user, User user2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   /* @Override
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
    }*/
}
