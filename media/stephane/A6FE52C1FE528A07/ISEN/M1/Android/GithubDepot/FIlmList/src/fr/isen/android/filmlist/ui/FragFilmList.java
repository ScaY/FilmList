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

public class FragFilmList {
	private static FragFilmList instance = null;
	private Hashtable<String, Fragment> fragments;
	private FilmDAO filmDAO;
	private FavouriteFilmsDAO favouriteDAO;
	private ToSeeFilmsDAO toSeeDAO;

	private FragFilmList() {
		fragments = new Hashtable<String, Fragment>();
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

	public Hashtable<String, Fragment> getFragments() {
		return fragments;
	}

	public Fragment getFragment(String fragmentWanted) {

		if (fragments == null || fragmentWanted == "" || fragmentWanted == null) {
			return null;
		}

		if (fragments.contains(fragmentWanted)) {
			// Refresh the fragment
			refreshFragment(fragments.get(fragmentWanted));

		} else {
			// Initialize the fragment and add it to the list of fragment
			Fragment frag = initMoviesFragment(fragmentWanted);
			fragments.put(fragmentWanted, frag);

		}

		return fragments.get(fragmentWanted);
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
			flf.setAdapter(Home.adapter);
			flf.setListview(Home.listview);
			fragmentWanted = flf;
		} else if (fragmentWanted instanceof SearchResultsFragment) {
			
		}

		return fragmentWanted;
	}

	public void refreshFragment(Fragment fragmentWanted) {
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
