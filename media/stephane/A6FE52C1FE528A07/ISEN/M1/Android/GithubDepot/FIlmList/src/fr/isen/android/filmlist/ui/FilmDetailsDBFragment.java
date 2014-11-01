package fr.isen.android.filmlist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.isen.android.filmlist.bdd.FilmDAO;

public class FilmDetailsDBFragment extends FilmDetailsFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = super.onCreateView(inflater, container, savedInstanceState);
		String filmName = null;
		Bundle args = getArguments();

		filmName = retrieveStringArgs(MOVIE_KEY, args);

		if (filmName != null) {
			FilmDAO dao = new FilmDAO(getActivity());
			dao.open();
			film = dao.select(filmName);
			dao.close();
		}

		return view;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		setFilmView();
	}
}
