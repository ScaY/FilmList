package fr.isen.android.filmlist.bdd;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ToSeeFilmsDAO extends DAOBase {

	public ToSeeFilmsDAO(Context pContext) {
		super(pContext);
	}

	public void insert(long filmId) {
		ContentValues value = new ContentValues();
		value.put(DatabaseHandler.TOSEE_FILM_KEY, filmId);
		mDb.insert(DatabaseHandler.TOSEE_TABLE_NAME, null, value);
	}
	
	public void insert(Film film) {
		insert(film.getId());
	}
	
	public void delete(long filmId) {
		mDb.delete(DatabaseHandler.TOSEE_TABLE_NAME, DatabaseHandler.TOSEE_FILM_KEY + " = ?", new String[] {Long.toString(filmId)});
	}
	
	public void delete(Film film) {
		delete(film.getId());
	}
	
	public Film select(long id) {
		Film film = null;
		String selection = DatabaseHandler.TOSEE_FILM_KEY + " = ?";
		String[] selectionArgs = {Long.toString(id)};
		Cursor c = mDb.query(DatabaseHandler.TOSEE_TABLE_NAME, DatabaseHandler.TOSEE_ALL_COLUMNS, selection, selectionArgs, null, null, null);
		
		if(c.moveToFirst()) {
			selection = DatabaseHandler.FILM_KEY + " = ?";
			Cursor c2 = mDb.query(DatabaseHandler.FILM_TABLE_NAME, DatabaseHandler.FILM_ALL_COLUMNS, selection, selectionArgs, null, null, null);
			
			if(c2.moveToFirst()) {
				film = cursorToFilm(c2);
			}
		}
		
		return film;
	}
	
	public List<Film> getAllFilms() {
		List<Film> films = new ArrayList<Film>();
		  
		Cursor c = mDb.query(DatabaseHandler.TOSEE_TABLE_NAME, DatabaseHandler.TOSEE_ALL_COLUMNS, null, null, null, null, null);
		  
		while(c.moveToNext()) {
			long filmId = c.getLong(0);
			String selection = DatabaseHandler.FILM_KEY + " = ?";
			String[] selectionArgs = {Long.toString(filmId)};
			Cursor c2 = mDb.query(DatabaseHandler.FILM_TABLE_NAME, DatabaseHandler.FILM_ALL_COLUMNS, selection, selectionArgs, null, null, null);
			c2.moveToFirst();
			Film film = cursorToFilm(c2);
			films.add(film);
		}
		  
		c.close();
		  
		return films;
	}

	private Film cursorToFilm(Cursor cursor) {
	  return new Film(cursor.getLong(0), cursor.getString(1), cursor.getString(2), new Date(0), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
  }
}
