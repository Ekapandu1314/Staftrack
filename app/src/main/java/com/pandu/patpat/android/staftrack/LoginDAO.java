package com.pandu.patpat.android.staftrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eka Pandu Winata on 5/27/2016.
 */
public class LoginDAO {

    public static final String TAG = "LoginDAO";

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.LOGIN_ID,
            DBHelper.LOGIN,

    };

    public LoginDAO(Context context) {
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

    public Login createLogin(int login) {
        ContentValues values = new ContentValues();
        //values.put(DBHelper.BAHAN_ID, idbahan);
        values.put(DBHelper.LOGIN, login);


        int insertId = (int) mDatabase
                .insert(DBHelper.TABLE_LOGIN, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_LOGIN, mAllColumns,
                DBHelper.LOGIN_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Login newLogin = cursorToBahan(cursor);
        cursor.close();
        return newLogin;
    }

    public void addLoginJson(Login login) {
        try{
            ContentValues values = new ContentValues();
            values.put(DBHelper.LOGIN_ID, login.getIdlogin());
            values.put(DBHelper.LOGIN, login.getLogin());

            mDatabase.insert(DBHelper.TABLE_LOGIN, null, values);

            mDatabase.update(DBHelper.TABLE_LOGIN, values, DBHelper.LOGIN_ID + " = " + login.getIdlogin(), null);
            //mDatabase.close();
        }catch (Exception e){
            Log.e("problem",e+"");
        }
    }

    public void deleteLogin(Login bahan) {
        int id = bahan.getIdlogin();
        System.out.println("the deleted ingredient has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_LOGIN, DBHelper.LOGIN_ID
                + " = " + id, null);
    }

    public Login getBahanById(int id) {

        String countQuery = "SELECT  * FROM " + DBHelper.TABLE_LOGIN + " WHERE " + DBHelper.LOGIN_ID +
                " = " + id;
        Cursor cursor = mDatabase.rawQuery(countQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        Login newBahan = cursorToBahan(cursor);
        cursor.close();
        return newBahan;

    }

    public long getLoginCount() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, DBHelper.TABLE_LOGIN);
        //db.close();
        return cnt;
    }

    public void updateLoginFromId(int id, int login) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.LOGIN, login); //These Fields should be your String values of actual column names
        mDatabase.update(DBHelper.TABLE_LOGIN, cv, DBHelper.LOGIN_ID + " = " + id, null);

    }

    private Login cursorToBahan(Cursor cursor) {
        Login login = new Login();

        login.setIdlogin(cursor.getInt(0));
        login.setLogin(cursor.getInt(1));
        return login;
    }
}