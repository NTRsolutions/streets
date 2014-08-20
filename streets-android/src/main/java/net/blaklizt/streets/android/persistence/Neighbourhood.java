package net.blaklizt.streets.android.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * User: tkaviya
 * Date: 7/5/14
 * Time: 12:24 PM
 */
public class Neighbourhood extends SQLiteOpenHelper {
    /**
     * Created by Tsungai on 2014/03/31.
     */
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Neighbourhood.db";

    public static final String PLACE_TABLE = "Place";

    public static final String USER_TABLE = "User";

    public static final String FRIEND_TABLE = "Friend";

    public Neighbourhood(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE + " (" +
                "SymbiosisID INT(11)," +
                "Username VARCHAR(50)," +
                "Password VARCHAR(50)," +
                "Email VARCHAR(50)," +
                "LastPlaceID INT(11)," +
                "HomePlaceID INT(11)," +
                "Type VARCHAR(50))");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + FRIEND_TABLE + " (" +
                "SymbiosisID INT(11)," +
                "Username VARCHAR(50)," +
                "UserGroupID VARCHAR(50)," +
                "Email VARCHAR(50)," +
                "LastPlaceID INT(11)," +
                "HomePlaceID INT(11))");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + PLACE_TABLE + " (" +
                "PlaceID INT(11)," +
                "Name VARCHAR(50)," +
                "Reference VARCHAR(100)," +
                "Latitude DOUBLE," +
                "Longitude DOUBLE," +
                "Type VARCHAR(50))");

//        db.execSQL("INSERT INTO " + PLACE_TABLE + " VALUES (0,'Tich de Blak',NULL,-26.092154565,28.216708950,'Friend')");
        db.execSQL("INSERT INTO " + PLACE_TABLE + " VALUES (0,'Home',NULL,-26.092154565,28.216708950,'ME')");
        db.execSQL("INSERT INTO " + USER_TABLE + " VALUES (0,'tkaviya','ImTheStreets','tsungai.kaviya@gmail.com',0,0,'ME')");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLACE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FRIEND_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLACE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FRIEND_TABLE);
        onCreate(db);
    }
}
