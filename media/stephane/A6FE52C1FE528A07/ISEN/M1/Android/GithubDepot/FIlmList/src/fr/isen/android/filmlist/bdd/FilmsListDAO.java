package fr.isen.android.filmlist.bdd;

import java.sql.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public abstract class FilmsListDAO extends DAOBase{

	public FilmsListDAO(Context pContext) {
		super(pContext);
	}

	public abstract List<Film> getAllFilms();

	protected Film cursorToFilm(Cursor cursor) {
	  return new Film(cursor.getLong(0), cursor.getString(1), cursor.getString(2), new Date(0), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
	}
}
