package fr.isen.android.filmlist.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filmlist.R;

public class FilmDetailsFragment extends Fragment {

	public static final String LIST_KEY = "keyFilmDetails";

	public FilmDetailsFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_film_details, container,
				false);

		return view;
	}

}
