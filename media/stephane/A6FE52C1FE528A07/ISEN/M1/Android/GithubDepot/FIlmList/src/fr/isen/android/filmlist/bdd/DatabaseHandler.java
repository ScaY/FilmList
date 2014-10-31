package fr.isen.android.filmlist.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	  public static final String FILM_KEY = "id";
	  public static final String FILM_NAME = "name";
	  public static final String FILM_YEAR = "year";
	  public static final String FILM_RELEASE_DATE = "releaseDate";
	  public static final String FILM_RUNTIME = "runtime";
	  public static final String FILM_DIRECTOR = "director";
	  public static final String FILM_STORY = "story";
	  public static final String FILM_IMAGE = "image";
	  public static final String FILM_ALL_COLUMNS[] = {DatabaseHandler.FILM_KEY, DatabaseHandler.FILM_NAME, FILM_YEAR, FILM_RELEASE_DATE, FILM_RUNTIME, FILM_DIRECTOR, FILM_STORY, FILM_IMAGE};
	  public static final String FILM_TABLE_NAME = "film";
	  public static final String FILM_TABLE_CREATE =
	    "CREATE TABLE " + FILM_TABLE_NAME + " (" +
	    		FILM_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	    		FILM_NAME + " TEXT NOT NULL, " +
	    		FILM_YEAR + " TEXT, " +
	    		FILM_RELEASE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
	    		FILM_RUNTIME + " TEXT, " +
	    		FILM_DIRECTOR + " TEXT, " +
	    		FILM_STORY + " TEXT, " +
	    		FILM_IMAGE + ");";
	  public static final String FILM_TABLE_DROP = "DROP TABLE IF EXISTS " + FILM_TABLE_NAME + ";";
	  
	  public static final String FAVOURITE_FILM_KEY = "filmId";
	  public static final String FAVOURITE_ALL_COLUMNS[] = {DatabaseHandler.FAVOURITE_FILM_KEY};
	  public static final String FAVOURITE_TABLE_NAME = "favouriteFilms";
	  public static final String FAVOURITE_TABLE_CREATE =
			    "CREATE TABLE " + FAVOURITE_TABLE_NAME + " (" +
			    		DatabaseHandler.FAVOURITE_FILM_KEY + " INTEGER," +
			    		"FOREIGN KEY(" + DatabaseHandler.FAVOURITE_FILM_KEY + ") REFERENCES " + DatabaseHandler.FILM_TABLE_NAME + "(" + DatabaseHandler.FILM_KEY + "));";
	  public static final String FAVOURITE_TABLE_DROP = "DROP TABLE IF EXISTS " + FAVOURITE_TABLE_NAME + ";";
	  
	  public static final String TOSEE_FILM_KEY = "filmId";
	  public static final String TOSEE_ALL_COLUMNS[] = {DatabaseHandler.TOSEE_FILM_KEY};
	  public static final String TOSEE_TABLE_NAME = "toSeeFilms";
	  public static final String TOSEE_TABLE_CREATE =
			    "CREATE TABLE " + TOSEE_TABLE_NAME + " (" +
			    		DatabaseHandler.TOSEE_FILM_KEY + " INTEGER," +
			    		"FOREIGN KEY(" + DatabaseHandler.TOSEE_FILM_KEY + ") REFERENCES " + DatabaseHandler.FILM_TABLE_NAME + "(" + DatabaseHandler.FILM_KEY + "));";
	  public static final String TOSEE_TABLE_DROP = "DROP TABLE IF EXISTS " + TOSEE_TABLE_NAME + ";";

	  public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {
	    super(context, name, factory, version);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase db) {
	    db.execSQL(FILM_TABLE_CREATE);
	    db.execSQL(FAVOURITE_TABLE_CREATE);
	    db.execSQL(TOSEE_TABLE_CREATE);
	  }
	  
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL(FILM_TABLE_DROP);
	    db.execSQL(FAVOURITE_TABLE_DROP);
	    db.execSQL(TOSEE_TABLE_DROP);
	    onCreate(db);
	  }
	}