package fr.isen.android.filmlist.bdd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class FilmDAO extends DAOBase {
	  public FilmDAO(Context pContext) {
		  super(pContext);
	  }
	  
	  /**
	   * @param m le film � ajouter � la base
	   */
	  public Film insert(String filmName) {
		  ContentValues value = new ContentValues();
		  value.put(DatabaseHandler.FILM_NAME, filmName);
		  long insertId = mDb.insert(DatabaseHandler.FILM_TABLE_NAME, null, value);
		  
		  Cursor cursor = mDb.query(DatabaseHandler.FILM_TABLE_NAME,
		        DatabaseHandler.FILM_ALL_COLUMNS, DatabaseHandler.FILM_KEY + " = " + insertId, null,
		        null, null, null);
		  cursor.moveToFirst();
		  Film film = cursorToFilm(cursor);
		  cursor.close();
		  return film;
	  }
	  
	  public Film insert(Film f) {
		  return insert(f.getName());
	  }

	  /**
	   * @param id l'identifiant du film � supprimer
	   */
	  public void delete(Film film) {
		  delete(film.getName());
	  }
	  
	  public void delete(String filmName) {
		  mDb.delete(DatabaseHandler.FILM_TABLE_NAME, DatabaseHandler.FILM_NAME + " = ?", new String[] {filmName});
	  }

	  /**
	   * @param m le film modifi�
	   */
	  public void edit(Film f) {
		  ContentValues value = filmToValues(f);
		  mDb.update(DatabaseHandler.FILM_TABLE_NAME, value, DatabaseHandler.FILM_KEY  + " = ?", new String[] {String.valueOf(f.getId())});
	  }

	  /**
	   * @param id l'identifiant du film � r�cup�rer
	   */
	  public Film select(long id) {
		  String columns[] = {DatabaseHandler.FILM_NAME};
		  String args[] = {String.valueOf(id)};
		  Cursor c = mDb.query(DatabaseHandler.FILM_TABLE_NAME, columns, DatabaseHandler.FILM_KEY + " = ?", args, "", "", "");
		  c.moveToFirst();
		  return new Film(id, c.getString(0));
	  }
	  
	  public List<Film> getAllFilms() {
		  List<Film> films = new ArrayList<Film>();
		  
		  Cursor c = mDb.query(DatabaseHandler.FILM_TABLE_NAME, DatabaseHandler.FILM_ALL_COLUMNS, null, null, null, null, null);
		  
		  while(c.moveToNext()) {
			  Film film = cursorToFilm(c);
			  films.add(film);
		  }
		  
		  c.close();
		  
		  return films;
	  }
	  
	  private Film cursorToFilm(Cursor cursor) {
		  return new Film(cursor.getLong(0), cursor.getString(1));
	  }
	  
	  private ContentValues filmToValues(Film f) {
		  ContentValues value = new ContentValues();
		  value.put(DatabaseHandler.FILM_NAME, f.getName());
		  value.put(DatabaseHandler.FILM_YEAR, f.getYear());
		  value.put(DatabaseHandler.FILM_RELEASE_DATE, f.getReleaseDate().toString());
		  value.put(DatabaseHandler.FILM_RUNTIME, f.getRuntime());
		  value.put(DatabaseHandler.FILM_DIRECTOR, f.getDirector());
		  value.put(DatabaseHandler.FILM_STORY, f.getStory());
		  
		  return value;
	  }
	}
