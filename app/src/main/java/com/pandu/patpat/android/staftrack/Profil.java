package com.pandu.patpat.android.staftrack;

import java.io.Serializable;

/**
 * Created by Eka Pandu Winata on 5/27/2016.
 */
public class Profil implements Serializable {

    private static final String TAG = "profil";
    private static final long serialVersionUID = -7406082437623008161L;

    private int idprofil;
    private String nama_profil;
    private String jabatan_profil;
    private String foto;
    private String nip;
    private String unit;
    private String no_hp;
    private String email;
    private String alamat;

    public Profil() {

    }

    public Profil(int idprofil,
            String nama_profil,
            String jabatan_profil,
            String foto,
            String nip,
            String unit,
            String no_hp,
            String email,
            String alamat)
    {
        this.idprofil = idprofil;
        this.nama_profil = nama_profil;
        this.jabatan_profil = jabatan_profil;
        this.foto = foto;
        this.nip = nip;
        this.unit = unit;
        this.no_hp = no_hp;
        this.email = email;
        this.alamat = alamat;
    }

    public void setIdprofil(int idprofil) { this.idprofil = idprofil; }

    public void setNama_profil(String nama_profil) { this.nama_profil = nama_profil; }

    public void setJabatan_profil(String jabatan_profil) { this.jabatan_profil = jabatan_profil; }

    public void setFoto(String foto) { this.foto = foto; }

    public void setNip(String nip) { this.nip = nip; }

    public void setUnit(String unit) { this.unit = unit; }

    public void setNo_hp(String no_hp) { this.no_hp = no_hp; }

    public void setEmail(String email) { this.email = email; }

    public void setAlamat(String alamat) { this.alamat = alamat; }

    public int getIdprofil()
    {
        return this.idprofil;
    }

    public String getNama_profil()
    {
        return this.nama_profil;
    }

    public String getJabatan_profil()
    {
        return this.jabatan_profil;
    }

    public String getFoto()
    {
        return this.foto;
    }

    public String getNip()
    {
        return this.nip;
    }

    public String getUnit()
    {
        return this.unit;
    }

    public String getNo_hp()
    {
        return this.no_hp;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getAlamat()
    {
        return this.alamat;
    }

}