package fr.isen.android.filmlist.ui;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import fr.isen.android.filmlist.bdd.FavouriteFilmsDAO;
import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmDAO;
import fr.isen.android.filmlist.bdd.ToSeeFilmsDAO;

public class AllFragments {
	private static AllFragments instance = null;
	private Hashtable<String, Fragment> fragments;
	private FilmDAO filmDAO;
	private FavouriteFilmsDAO favouriteDAO;
	private ToSeeFilmsDAO toSeeDAO;

	private AllFragments() {
		fragments = new Hashtable<String, Fragment>();
	}

	public static AllFragments getInstance() {
		if (instance == null) {
			instance = new AllFragments();
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

	public Hashtable<String, Fragment> getFragments() {
		return fragments;
	}

	public Fragment getFragment(String fragmentWanted) {

		if (fragments == null || fragmentWanted == "" || fragmentWanted == null) {
			return null;
		}

		if (!fragments.containsKey(fragmentWanted)) {
			// Initialize the fragment and add it to the list of fragment
			Fragment frag = initMoviesFragment(fragmentWanted);
			fragments.put(fragmentWanted, frag);
		} else {
			refreshFragment(fragments.get(fragmentWanted));
		}

		return fragments.get(fragmentWanted);
	}

	public Fragment get(String id) {
		if (this.fragments.containsKey(id)) {
			return this.fragments.get(id);
		} else {
			return null;
		}
	}

	public void putFragment(Fragment f, String id) {
		if (this.fragments.contains(id)) {
			this.fragments.remove(id);
		}

		this.fragments.put(id, f);
	}

	public Fragment initMoviesFragment(String fragmentName) {
		Fragment fragmentWanted = null;

		if (fragmentName.equals(FilmToSeeListFragment.class.getSimpleName()
				.toString())) {
			fragmentWanted = new FilmToSeeListFragment();
		} else if (fragmentName.equals(FilmAllListFragment.class
				.getSimpleName().toString())) {
			fragmentWanted = new FilmAllListFragment();
		} else if (fragmentName.equals(FilmFavouriteListFragment.class
				.getSimpleName().toString())) {
			fragmentWanted = new FilmFavouriteListFragment();
		} else if (fragmentName.equals(SearchResultsFragment.class
				.getSimpleName().toString())) {
			fragmentWanted = new SearchResultsFragment();
		}

		if (fragmentWanted instanceof FilmListFragment) {

			FilmListFragment flf = (FilmListFragment) fragmentWanted;

			List<Film> films = getFilms(fragmentWanted);
			Bundle args = null;

			ArrayList<String> list = new ArrayList<String>();

			for (Film film : films) {
				list.add(film.getName());
			}

			args = new Bundle();
			args.putStringArrayList(FilmListFragment.LIST_KEY, list);
			flf.setArguments(args);
			flf.setList(list);
			fragmentWanted = flf;
		}

		return fragmentWanted;
	}

	public void refreshFragment(Fragment fragmentWanted) {
		if (fragmentWanted instanceof FilmListFragment) {

			List<Film> films = getFilms(fragmentWanted);

			ArrayList<String> list = new ArrayList<String>();

			for (Film film : films) {
				list.add(film.getName());
			}

			((FilmListFragment) fragmentWanted).setList(list);
		}

	}

	public List<Film> getFilms(Fragment fragmentWanted) {
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
