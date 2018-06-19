package com.saubhagyam.quickfinderplaces;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String databasename = "mydbsqlite1";
    public static final int databaseversion = 2;
    public static final String tablename = "favouriteDetails1";
    public static final String keyid = "id";
    public static final String keyplaceid = "placeid";
    public static final String keyname = "name";
    public static final String keyadd = "address";
    public static final String keystatus = "status";
    public static final String keyrat = "ratting";
    public static final String keykm = "km";

    public DatabaseHandler(Context context) {
        super(context, databasename, null, databaseversion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String Create_Table ="CREATE TABLE "+tablename+"("
                +keyid+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +keyplaceid+" text,"
                +keyname+" text,"
                +keyadd+" text,"
                +keystatus+" text,"
                +keykm+" text,"
                +keyrat+" text"+")";

        db.execSQL(Create_Table);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table If Exists " + tablename);

        onCreate(db);


    }

    public void insertData(Contact_Databse_pojo contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values= new ContentValues();

        values.put(keyname,contact.getUname());
        values.put(keyplaceid,contact.getUplaceid());
        values.put(keyadd,contact.getUadd());
        values.put(keystatus,contact.getUstatus());
        values.put(keyrat,contact.getUrate());
        values.put(keykm,contact.getUkm());


        db.insert(tablename,null,values);
        db.close();
    }

    public Integer deleteData(String placeid)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(tablename,keyplaceid + " = ?",new String[]{String.valueOf(placeid)});
    }

    public Integer deleteData1(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(tablename,keyid + " = ?",new String[]{String.valueOf(id)});
    }




/*
   public Cursor getAllData(String name)
   {
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor c = db.rawQuery("SELECT * FROM mytablesqlite WHERE TRIM(name) = '"+name.trim()+"'", null);

       return c;
   }
*/





    public List<Contact_Databse_pojo> getdata(){
        // DataModel dataModel = new DataModel();
        List<Contact_Databse_pojo> data=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+tablename+" ;",null);
        StringBuffer stringBuffer = new StringBuffer();
        Contact_Databse_pojo contact = null;
        while (cursor.moveToNext()) {
            contact= new Contact_Databse_pojo();
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String placeid = cursor.getString(cursor.getColumnIndexOrThrow("placeid"));
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String ratting = cursor.getString(cursor.getColumnIndexOrThrow("ratting"));
            String km = cursor.getString(cursor.getColumnIndexOrThrow("km"));


            contact.setUname(name);
            contact.setUplaceid(placeid);
           // contact.setUadd(id);
            contact.setUadd(address);
            contact.setUstatus(status);
            contact.setUrate(ratting);
            contact.setUkm(km);

            stringBuffer.append(contact);
            // stringBuffer.append(dataModel);
            data.add(contact);
        }

        for (Contact_Databse_pojo mo:data ) {

            Log.i("Hellomo",""+mo.getUname());
        }

        //

        return data;
    }

    public boolean updateData(String id,String name,String email,String mo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
      //  values.put(keyid,id);
        values.put(keyname,name);
        values.put(keyadd,email);
        values.put(keystatus,mo);

        db.update(tablename,values,"id = ?",new String[]{ id });
        return true;
    }
}

