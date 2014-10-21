package fr.isen.android.filmlist.fragments;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import fr.isen.android.filmlist.bdd.FavouriteFilmsDAO;
import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmDAO;
import fr.isen.android.filmlist.bdd.ToSeeFilmsDAO;
import fr.isen.android.filmlist.ui.FilmAllListFragment;
import fr.isen.android.filmlist.ui.FilmFavouriteListFragment;
import fr.isen.android.filmlist.ui.FilmListFragment;
import fr.isen.android.filmlist.ui.FilmToSeeListFragment;
import fr.isen.android.filmlist.ui.Home;

public class FragFilmList {
	private static FragFilmList instance = null;
	private Hashtable<String, FilmListFragment> fragments;
	private FilmDAO filmDAO;
	private FavouriteFilmsDAO favouriteDAO;
	private ToSeeFilmsDAO toSeeDAO;

	private FragFilmList() {
		fragments = new Hashtable<String, FilmListFragment>();
	}

	public static FragFilmList getInstance() {
		if (instance == null) {
			instance = new FragFilmList();
		}

		return instance;
	}

	public void setFilmDAO(FilmDAO f) {
		this.filmDAO = f;
	}

	public void setFavouriteFIlmDAO(FavouriteFilmsDAO f) {
		this.favouriteDAO = f;
	}

	public void setToSeeFilmDAO(ToSeeFilmsDAO f) {
		this.toSeeDAO = f;
	}

	public Hashtable<String, FilmListFragment> getFragments() {
		return fragments;
	}

	public FilmListFragment getFragment(String fragmentWanted) {

		if (fragments == null) {
			return null;
		}

		String key = "";

		if (fragmentWanted.equals(FilmToSeeListFragment.class.getSimpleName()
				.toString())) {
			key = FilmToSeeListFragment.class.getSimpleName().toString();
		} else if (fragmentWanted.equals(FilmAllListFragment.class
				.getSimpleName().toString())) {
			key = FilmAllListFragment.class.getSimpleName().toString();
		} else if (fragmentWanted.equals(FilmFavouriteListFragment.class
				.getSimpleName().toString())) {
			key = FilmFavouriteListFragment.class.getSimpleName().toString();
		}

		if (fragments.contains(key)) {
			// Refresh the fragment
			refreshFragment(fragments.get(key));

		} else {
			// Initialize the fragment and add it to the list of fragment
			FilmListFragment frag = initMoviesFragment(fragmentWanted);
			fragments.put(key, frag);

		}

		return fragments.get(key);
	}

	public FilmListFragment initMoviesFragment(String fragmentName) {
		FilmListFragment fragmentWanted = null;

		if (fragmentName.equals(FilmToSeeListFragment.class.getSimpleName()
				.toString())) {
			fragmentWanted = new FilmToSeeListFragment();
		} else if (fragmentName.equals(FilmAllListFragment.class
				.getSimpleName().toString())) {
			fragmentWanted = new FilmAllListFragment();
		} else if (fragmentName.equals(FilmFavouriteListFragment.class
				.getSimpleName().toString())) {
			fragmentWanted = new FilmFavouriteListFragment();
		}

		List<Film> films = getFilms(fragmentWanted);
		Bundle args = null;

		ArrayList<String> list = new ArrayList<String>();

		for (Film film : films) {
			list.add(film.getName());
		}

		args = new Bundle();
		args.putStringArrayList(FilmListFragment.LIST_KEY, list);
		fragmentWanted.setArguments(args);
		fragmentWanted.setList(list);
		fragmentWanted.setAdapter(Home.adapter);
		fragmentWanted.setListview(Home.listview);
		return fragmentWanted;
	}

	public void refreshFragment(FilmListFragment fragmentWanted) {
		List<Film> films = getFilms(fragmentWanted);
		Bundle args = null;

		ArrayList<String> list = new ArrayList<String>();

		for (Film film : films) {
			list.add(film.getName());
		}

		args = new Bundle();
		args.putStringArrayList(FilmListFragment.LIST_KEY, list);
		fragmentWanted.setArguments(args);

	}

	public List<Film> getFilms(FilmListFragment fragmentWanted) {
		List<Film> films = null;
		if (fragmentWanted instanceof FilmFavouriteListFragment) {
			favouriteDAO.open();
			films = favouriteDAO.getAllFilms();
			favouriteDAO.close();
		} else if (fragmentWanted instanceof FilmAllListFragment) {
			filmDAO.open();
			films = filmDAO.getAllFilms();
			filmDAO.close();
		} else if (fragmentWanted instanceof FilmToSeeListFragment) {
			toSeeDAO.open();
			films = toSeeDAO.getAllFilms();
			toSeeDAO.close();
		}

		return films;
	}
	
}
