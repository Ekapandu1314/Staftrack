package com.pandu.patpat.android.staftrack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Eka Pandu Winata on 5/27/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";

    // Table Name
    public static final String TABLE_LOGIN = "TABLE_LOGIN";
    public static final String TABLE_PROFIL = "TABLE_PROFIL";
    //public static final String TABLE_VERSION = "TABLE_VERSION";
    //public static final String TABLE_HEWAN = "TABLE_HEWAN";
    //public static final String TABLE_RECORD = "TABLE_RECORD";

    public static final String LOGIN_ID = "idlogin";
    public static final String LOGIN = "login";

    public static final String PROFIL_ID = "idprofil";
    public static final String NAMA_PROFIL = "nama_profil";
    public static final String JABATAN_PROFIL = "jabatan_profil";
    public static final String FOTO = "foto";
    public static final String NIP = "nip";
    public static final String UNIT = "unit";
    public static final String NO_HP = "no_hp";
    public static final String EMAIL = "email";
    public static final String ALAMAT = "alamat";

    // columns of the version table
    public static final String TANGGAL = "tanggal";
    public static final String VERSION = "version";

    // columns of the record table
    public static final String RECORD_ID = "idrecord";
    public static final String NAMA_RECORD = "namarecord";
    public static final String RTANGGAL = "rtanggal";
    public static final String RHEWAN = "rhewan";
    public static final String RTUJUAN = "rtujuan";
    public static final String RBERAT1 = "rberat1";
    public static final String RBERAT2 = "rberat2";
    public static final String RJTERNAK = "rjternak";
    public static final String RLAMA = "rlama";
    public static final String PBAHAN = "pbahan";

    private static final String DATABASE_NAME = "pakanku.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_LOGIN = "create table " + TABLE_LOGIN + "(" + LOGIN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LOGIN + " INTEGER NOT NULL );";

    private static final String CREATE_TABLE_PROFIL = "create table " + TABLE_PROFIL + "(" + PROFIL_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAMA_PROFIL + " TEXT NOT NULL, " + JABATAN_PROFIL + " TEXT NOT NULL, " + FOTO + " TEXT NOT NULL, " + NIP + " TEXT NOT NULL, " + UNIT + " TEXT NOT NULL, " + NO_HP + " TEXT NOT NULL, " + EMAIL + " TEXT NOT NULL, " + ALAMAT + " TEXT NOT NULL);";

//    private static final String CREATE_TABLE_HEWAN = "create table " + TABLE_HEWAN + "(" + HEWAN_ID
//            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HEWAN + " TEXT NOT NULL, "+ TUJUAN + " TEXT NOT NULL, " + HIJAU + " DOUBLE NOT NULL," + KONSENTRAT + " DOUBLE NOT NULL, " + BK_HEWAN + " DOUBLE NOT NULL, " + PK_HEWAN + " DOUBLE NOT NULL, " + HARGA_JUAL + " INTEGER);";

//    private static final String CREATE_TABLE_RECORD = "create table " + TABLE_RECORD + "(" + RECORD_ID
//            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAMA_RECORD + " TEXT NOT NULL, " + RTANGGAL + " TEXT NOT NULL, "+ RHEWAN + " TEXT NOT NULL, " + RTUJUAN + " TEXT NOT NULL, " + RBERAT1 + " DOUBLE NOT NULL, " + RBERAT2 + " DOUBLE, "  + RJTERNAK + " INTEGER NOT NULL, " + RLAMA + " INTEGER NOT NULL, "  + PBAHAN + " TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_LOGIN);
        database.execSQL(CREATE_TABLE_PROFIL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading the database from version " + oldVersion + " to " + newVersion);
        // clear all data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFIL);

        // recreate the tables
        onCreate(db);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

}