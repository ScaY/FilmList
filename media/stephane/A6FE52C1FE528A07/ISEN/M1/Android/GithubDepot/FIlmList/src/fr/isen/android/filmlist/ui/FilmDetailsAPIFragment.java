package fr.isen.android.filmlist.ui;

import java.io.Serializable;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmSearchResult;
import fr.isen.android.filmlist.utils.GetFilmDetailsTask;

public class FilmDetailsAPIFragment extends FilmDetailsFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View view = super.onCreateView(inflater, container, savedInstanceState);
		FilmSearchResult filmSearchResult = null;
		Bundle args = getArguments();
		
		if (args != null && args.containsKey(MOVIE_KEY)) {
			Serializable arg = args.getSerializable(MOVIE_KEY);
			if(arg instanceof FilmSearchResult) {
				filmSearchResult = (FilmSearchResult)arg;
			}
		}
			
		if(filmSearchResult != null) {
			GetFilmDetailsTask task = new GetFilmDetailsTask(this);
			task.execute(filmSearchResult.getImdbID());
		}
		return view;
	}
}
