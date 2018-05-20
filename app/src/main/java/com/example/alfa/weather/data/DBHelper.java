package com.example.alfa.weather.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alfa.weather.model.Coord;
import com.example.alfa.weather.model.Example;

import java.util.ArrayList;

/**
 * Created by Alfa on 5/12/2018.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String WEATHER_TABLE_NAME = "weather";
    public static final String WEATHER_COLUMN_NAME = "name";
    public static final String WEATHER_COLUMN_LON = "lon";
    public static final String WEATHER_COLUMN_LAT = "lat";
    private SQLiteDatabase db;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table weather " + "(id integer primary key, name text,lon real,lat real)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS weather");
        onCreate(db);
    }

    public boolean insertWeather(int id, String name, double lon, double lat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("lon", lon);
        contentValues.put("lat", lat);

        db.insert("weather", null, contentValues);
        db.close();
        return true;
    }


    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int retVl = (int) DatabaseUtils.queryNumEntries(db, WEATHER_TABLE_NAME);
        db.close();
        return retVl;
    }

    public Integer deleteCity(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();


        int idDeleted = db.delete("weather",
                "id = ? ", new String[]{Integer.toString(id)});
        db.close();
        return idDeleted;
    }

    public ArrayList<Example> getAllWeather() {
        ArrayList<Example> array_list = new ArrayList<Example>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from weather", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            Example contact = new Example();


            Coord coord = new Coord();
            coord.setLat(res.getDouble(res.getColumnIndex(WEATHER_COLUMN_LAT)));
            coord.setLon(res.getDouble(res.getColumnIndex(WEATHER_COLUMN_LON)));
            contact.setName(res.getString(res.getColumnIndex(WEATHER_COLUMN_NAME)));

            contact.setCoord(coord);
            array_list.add(contact);

            res.moveToNext();
        }
        db.close();
        return array_list;
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from weather where id=" + id + "", null);
        return res;
    }
}
