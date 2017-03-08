package com.example.userone.mygalleryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 9/22/2016.
 */

public class DatabaseHandler {
    public static final String TABLE_ID = "id";
    public static final String IMG_ID = "image_id";
    public static final String IMAGE_PHOTO = "image_photo";
    public static final String IMPG_COMMENTS = "imagecomments";
    public static final String IMG_TEXTTYPE = "imagetexttype";
    public static final String IMG_DATE = "image_date";


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "MyGalleryDb.db";
    private static final int DATABASE_VERSION = 1;

    private static final String IMAGE_TABLE = "ImageTable";

    private static final String CREATE_EMPLOYEES_TABLE = "create table "
            + IMAGE_TABLE + " (" + IMG_ID
            + " integer primary key autoincrement, " + IMAGE_PHOTO
            + " blob not null, " + IMPG_COMMENTS + " text, "+ IMG_DATE + " text, "
            + IMG_TEXTTYPE + " TEXT );";
    private final Context mCtx;




    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_EMPLOYEES_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE);
            onCreate(db);
        }
    }
    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public DatabaseHandler(Context ctx) {
        mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }
    public DatabaseHandler open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void insertEmpDetails(Views viewData) {
        ContentValues cv = new ContentValues();
        Log.d("insert data db","--->"+viewData.getComments()+"..."+viewData.getImageType());

        cv.put(IMAGE_PHOTO, Utils.getBytes(viewData.getImageBytes()));

        cv.put(IMPG_COMMENTS, viewData.getComments());
        cv.put(IMG_TEXTTYPE, viewData.getImageType());
        cv.put(IMG_DATE, viewData.getDateString());

        mDb.insert(IMAGE_TABLE, null, cv);
    }


    public List<Views> retriveEmpDetails() throws SQLException
    {
        List<Views> contactList= new ArrayList<Views>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + IMAGE_TABLE;
        Cursor cur = mDb.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cur.moveToFirst()) {
            do {
                Views contactdata = new Views();

                contactdata.setImageType(cur.getString(cur.getColumnIndex(IMG_TEXTTYPE)));
                contactdata.setComments(cur.getString(cur.getColumnIndex(IMPG_COMMENTS)));
                contactdata.setDateString(cur.getString(cur.getColumnIndex(IMG_DATE)));
                contactdata.set_id(cur.getInt(cur.getColumnIndex(IMG_ID)));
                byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE_PHOTO));
                contactdata.setImageBytes(Utils.getPhoto(blob));
                // Adding contact to list
                contactList.add(contactdata);
            } while (cur.moveToNext());
        }

        // return contact list
        return contactList;
    }
    //update record
    public boolean updateCommentsRecord(int id,String comments)
    {
        ContentValues cv= new ContentValues();
        cv.put(IMPG_COMMENTS,comments);

        return mDb.update(IMAGE_TABLE, cv, IMG_ID+"="+id, null)>0;

    }

    //update record
    public boolean updateOccationRecord(int id,String occation)
    {
        ContentValues cv= new ContentValues();
        cv.put(IMG_TEXTTYPE,occation);

        return mDb.update(IMAGE_TABLE, cv, IMG_ID+"="+id, null)>0;

    }

    //delete record
    public long deleteRecord(int id) {
        long val =mDb.delete(IMAGE_TABLE, IMG_ID+"="+id,null);
        return val;
    }
    public long deleteNameRecord(String  occation1) {
        long val =  mDb.delete(IMAGE_TABLE, IMG_TEXTTYPE + "=\"" + occation1+"\"", null);
        return val;
    }


}
