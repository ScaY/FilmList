package fr.isen.android.filmlist.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	  public static final String FILM_KEY = "id";
	  public static final String FILM_NAME = "name";
	  public static final String FILM_ALL_COLUMNS[] = {DatabaseHandler.FILM_KEY, DatabaseHandler.FILM_NAME};
	    
	  public static final String FILM_TABLE_NAME = "film";
	  public static final String FILM_TABLE_CREATE =
	    "CREATE TABLE " + FILM_TABLE_NAME + " (" +
	    		FILM_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	    		FILM_NAME + " TEXT NOT NULL);";

	  public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {
	    super(context, name, factory, version);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase db) {
	    db.execSQL(FILM_TABLE_CREATE);
	  }
	  
	  public static final String FILM_TABLE_DROP = "DROP TABLE IF EXISTS " + FILM_TABLE_NAME + ";";
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL(FILM_TABLE_DROP);
	    onCreate(db);
	  }
	}