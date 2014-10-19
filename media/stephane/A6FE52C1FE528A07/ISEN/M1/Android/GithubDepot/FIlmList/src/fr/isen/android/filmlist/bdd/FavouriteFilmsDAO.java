package fr.isen.android.filmlist.bdd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class FavouriteFilmsDAO extends DAOBase {

	public FavouriteFilmsDAO(Context pContext) {
		super(pContext);
	}

	public void insert(long filmId) {
		ContentValues value = new ContentValues();
		value.put(DatabaseHandler.FAVOURITE_FILM_KEY, filmId);
		mDb.insert(DatabaseHandler.FAVOURITE_TABLE_NAME, null, value);
	}
	
	public void insert(Film film) {
		insert(film.getId());
	}
	
	public void delete(long filmId) {
		mDb.delete(DatabaseHandler.FAVOURITE_TABLE_NAME, DatabaseHandler.FAVOURITE_FILM_KEY + " = ?", new String[] {Long.toString(filmId)});
	}
	
	public void delete(Film film) {
		delete(film.getId());
	}
	
	public List<Film> getAllFilms() {
	  List<Film> films = new ArrayList<Film>();
	  
	  Cursor c = mDb.query(DatabaseHandler.FAVOURITE_TABLE_NAME, DatabaseHandler.FAVOURITE_ALL_COLUMNS, null, null, null, null, null);
	  
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
}
