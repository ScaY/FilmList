package fr.isen.android.filmlist.bdd;

import java.util.List;

import android.content.Context;

public abstract class FilmsListDAO extends DAOBase{

	public FilmsListDAO(Context pContext) {
		super(pContext);
	}

	public abstract List<Film> getAllFilms();

}
