package com.pandu.patpat.android.staftrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Eka Pandu Winata on 5/27/2016.
 */
public class ProfilDAO {

    public static final String TAG = "ProfilDAO";

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.PROFIL_ID,
            DBHelper.NAMA_PROFIL,
            DBHelper.JABATAN_PROFIL,
            DBHelper.FOTO,
            DBHelper.NIP,
            DBHelper.UNIT,
            DBHelper.NO_HP,
            DBHelper.EMAIL,
            DBHelper.ALAMAT,

    };

    public ProfilDAO(Context context) {
        mDbHelper = new DBHelper(context);
        this.mContext = context;
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

//    public Profil createProfil(int login) {
//        ContentValues values = new ContentValues();
//        //values.put(DBHelper.BAHAN_ID, idbahan);
//        values.put(DBHelper.LOGIN, login);
//
//
//        int insertId = (int) mDatabase
//                .insert(DBHelper.TABLE_LOGIN, null, values);
//        Cursor cursor = mDatabase.query(DBHelper.TABLE_LOGIN, mAllColumns,
//                DBHelper.LOGIN_ID + " = " + insertId, null, null,
//                null, null);
//        cursor.moveToFirst();
//        Login newLogin = cursorToBahan(cursor);
//        cursor.close();
//        return newLogin;
//    }

    public Profil getTopProfil()
    {
        String countQuery = "SELECT  * FROM " + DBHelper.TABLE_PROFIL + " LIMIT 1";
        Cursor cursor = mDatabase.rawQuery(countQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        Profil profil = cursorToProfil(cursor);
        cursor.close();
        return profil;
    }

    public void addProfilJson(Profil profil) {
        try{
            ContentValues values = new ContentValues();
            values.put(DBHelper.PROFIL_ID, profil.getIdprofil());
            values.put(DBHelper.NAMA_PROFIL, profil.getNama_profil());
            values.put(DBHelper.JABATAN_PROFIL, profil.getJabatan_profil());
            values.put(DBHelper.FOTO, profil.getFoto());
            values.put(DBHelper.NIP, profil.getNip());
            values.put(DBHelper.UNIT, profil.getUnit());
            values.put(DBHelper.NO_HP, profil.getNo_hp());
            values.put(DBHelper.EMAIL, profil.getEmail());
            values.put(DBHelper.ALAMAT, profil.getAlamat());

            mDatabase.insert(DBHelper.TABLE_PROFIL, null, values);

            mDatabase.update(DBHelper.TABLE_PROFIL, values, DBHelper.PROFIL_ID + " = " + profil.getIdprofil(), null);
            //mDatabase.close();
        }catch (Exception e){
            Log.e("problem",e+"");
        }
    }

    public void deleteProfil(Profil profil) {
        int id = profil.getIdprofil();
        System.out.println("the deleted ingredient has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_PROFIL, DBHelper.PROFIL_ID
                + " = " + id, null);
    }

    public Profil getProfilById(int id) {

        String countQuery = "SELECT  * FROM " + DBHelper.TABLE_PROFIL + " WHERE " + DBHelper.PROFIL_ID +
                " = " + id;
        Cursor cursor = mDatabase.rawQuery(countQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        Profil newProfil = cursorToProfil(cursor);
        cursor.close();
        return newProfil;

    }

    public long getProfilCount() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, DBHelper.TABLE_PROFIL);
        //db.close();
        return cnt;
    }

    public void updateProfilFromId(int id, Profil profil) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAMA_PROFIL, profil.getNama_profil());
        values.put(DBHelper.JABATAN_PROFIL, profil.getJabatan_profil());
        values.put(DBHelper.FOTO, profil.getFoto());
        values.put(DBHelper.NIP, profil.getNip());
        values.put(DBHelper.UNIT, profil.getUnit());
        values.put(DBHelper.NO_HP, profil.getNo_hp());
        values.put(DBHelper.EMAIL, profil.getEmail());
        values.put(DBHelper.ALAMAT, profil.getAlamat());
        mDatabase.update(DBHelper.TABLE_LOGIN, values, DBHelper.LOGIN_ID + " = " + id, null);

    }

    private Profil cursorToProfil(Cursor cursor) {
        Profil profil = new Profil();

        profil.setIdprofil(cursor.getInt(0));
        profil.setNama_profil(cursor.getString(1));
        profil.setJabatan_profil(cursor.getString(2));
        profil.setFoto(cursor.getString(3));
        profil.setNip(cursor.getString(4));
        profil.setUnit(cursor.getString(5));
        profil.setNo_hp(cursor.getString(6));
        profil.setEmail(cursor.getString(7));
        profil.setAlamat(cursor.getString(8));

        return profil;
    }
}