package com.pandu.patpat.android.staftrack;

import java.io.Serializable;

/**
 * Created by Eka Pandu Winata on 5/27/2016.
 */
public class Login implements Serializable {

    private static final String TAG = "login";
    private static final long serialVersionUID = -7406082437623008161L;

    private int idlogin;
    private int login;

    public Login() {

    }

    public Login(int idlogin, int login)
    {
        this.idlogin = idlogin;
        this.login = login;

    }

    public void setIdlogin(int idlogin)
    {
        this.idlogin = idlogin;
    }

    public void setLogin(int login)
    {
        this.login = login;
    }

    public int getIdlogin()
    {
        return this.idlogin;
    }

    public int getLogin()
    {
        return this.login;
    }

}
