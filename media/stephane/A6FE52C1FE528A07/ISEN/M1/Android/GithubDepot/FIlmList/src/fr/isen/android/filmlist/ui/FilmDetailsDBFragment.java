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
		
		if (args != null && args.containsKey(MOVIE_KEY)) {
			filmName = args.getString(MOVIE_KEY);
		}
			
		if(filmName != null) {
			FilmDAO dao = new FilmDAO(getActivity());
			dao.open();
			film = dao.select(filmName);
			dao.close();
			setFilmView(view);
		}
		
		return view;
	}
}
