package fr.isen.android.filmlist.bdd;

import android.content.ContentValues;
import android.content.Context;

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
}
