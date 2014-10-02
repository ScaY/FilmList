package fr.isen.android.filmlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class FilmDAO extends DAOBase {
	  public static final String TABLE_NAME = "film";
	  public static final String KEY = "id";
	  public static final String NAME = "name";

	  public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT);";

	  public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

	  public FilmDAO(Context pContext) {
		  super(pContext);
	  }
	  
	  /**
	   * @param m le film à ajouter à la base
	   */
	  public void insert(Film f) {
		  ContentValues value = new ContentValues();
		  value.put(FilmDAO.NAME, f.getName());
		  mDb.insert(FilmDAO.TABLE_NAME, null, value);
	  }

	  /**
	   * @param id l'identifiant du film à supprimer
	   */
	  public void delete(long id) {
		  mDb.delete(TABLE_NAME, KEY + " = ?", new String[] {String.valueOf(id)});
	  }

	  /**
	   * @param m le film modifié
	   */
	  public void edit(Film f) {
		  ContentValues value = new ContentValues();
		  value.put(NAME, m.getName());
		  mDb.update(TABLE_NAME, value, KEY  + " = ?", new String[] {String.valueOf(f.getId())});
	  }

	  /**
	   * @param id l'identifiant du film à récupérer
	   */
	  public Film select(long id) {
		  String columns[] = {NAME};
		  String args[] = {String.valueOf(id)};
		  Cursor c = mDb.query(TABLE_NAME, columns, KEY + " = ?", args, "", "", "");
		  c.moveToFirst();
		  return new Film(id, c.getString(0));
	  }
	}
