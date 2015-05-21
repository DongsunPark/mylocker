package com.coursera.mobile.selfie;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "coursera_selfie.db";
   public static final String TABLE_NAME = "photo";
   public static final String COLUMN_ID = "id";
   public static final String COLUMN_NAME = "name";
   public static final String COLUMN_PATH = "path";
   public static final String COLUMN_CREATED_DATE = "created_date";

   public DBHelper(Context context) {
      super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table photo " +"(id integer primary key, name text, path text, created_date text)");
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS photo");
      onCreate(db);
   }

   public boolean insertContact(String name, String path, String createdDate) {
	   
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();

      contentValues.put("name", name);
      contentValues.put("path", path);
      contentValues.put("created_date", createdDate);	
      db.insert("photo", null, contentValues);
      return true;
   }
   
   public boolean insertContact(Photo photo) {
	   String createdDate = DateFormat.getDateInstance(DateFormat.FULL).format(photo.getCreatedDate());
	   return insertContact(photo.getName(), photo.getPath(), createdDate);
   }
   
   public Cursor getData(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from photo where id="+id+"", null );
      return res;
   }
   
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
      return numRows;
   }
   
   public ArrayList<Photo> getAllPhotos() throws Exception {
      ArrayList<Photo> photos = new ArrayList<Photo>();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from photo", null );
      res.moveToFirst();
      while(res.isAfterLast() == false){
    	  Photo photo = new Photo();
    	  photo.setName(res.getString(res.getColumnIndex(COLUMN_NAME)));
    	  photo.setPath(res.getString(res.getColumnIndex(COLUMN_PATH)));
    	  String strCreateDate = res.getString(res.getColumnIndex(COLUMN_CREATED_DATE));
    	  Date createdDate = DateFormat.getDateInstance(DateFormat.FULL).parse(strCreateDate);
    	  photo.setCreatedDate(createdDate);
    	  photos.add(photo);
    	  res.moveToNext();
      }
      return photos;
   }
}