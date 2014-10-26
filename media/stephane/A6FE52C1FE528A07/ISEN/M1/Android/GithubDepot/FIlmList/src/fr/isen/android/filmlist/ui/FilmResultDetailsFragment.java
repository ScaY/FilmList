package fr.isen.android.filmlist.ui;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmSearchResult;
import fr.isen.android.filmlist.utils.GetFilmDetailsTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FilmResultDetailsFragment extends FilmDetailsFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_film_details, container,
				false);
		
		FilmSearchResult filmSearchResult = null;
		Bundle args = getArguments();
		
		if (args != null && args.containsKey(MOVIE_KEY)) {
			Serializable arg = args.getSerializable(MOVIE_KEY);
			if(arg instanceof FilmSearchResult) {
				filmSearchResult = (FilmSearchResult)arg;
			}
		}
			
		if(filmSearchResult != null) {
			((TextView) view.findViewById(R.id.film_title)).setText(filmSearchResult.getTitle());
			GetFilmDetailsTask task = new GetFilmDetailsTask();
			AsyncTask<String, Void, JSONObject> result = task.execute(filmSearchResult.getImdbID());
			try {
				film = new Film(result.get());
			}
			catch(Exception e) {}
		}
 		
		return view;
	}
}
