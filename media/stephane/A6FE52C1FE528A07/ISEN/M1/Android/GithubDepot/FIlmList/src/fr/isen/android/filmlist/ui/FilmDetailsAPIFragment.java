package fr.isen.android.filmlist.ui;

import java.io.Serializable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmSearchResult;

public class FilmDetailsAPIFragment extends FilmDetailsFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = super.onCreateView(inflater, container, savedInstanceState);
		FilmSearchResult filmSearchResult = null;
		Bundle args = getArguments();
		Film film = null;
		if (args != null && args.containsKey(MOVIE_KEY)) {
			Serializable arg = args.getSerializable(MOVIE_KEY);
			/*if (arg instanceof FilmSearchResult) {
				filmSearchResult = (FilmSearchResult) arg;
			}*/
			if(arg instanceof Film){
				film = (Film)arg;
			}
		}

		//if (filmSearchResult != null) {
		//	GetFilmDetailsTask task = new GetFilmDetailsTask(this);
		//	task.execute(filmSearchResult.getImdbID());
		//}

		
		if(film != null){
			this.film = film;
			// Pour afficher l'image
			listview.post(new Runnable() {
				
				@Override
				public void run() {
					setFilmView();
					
				}
			});
		}
		
		return view;
	}
}
