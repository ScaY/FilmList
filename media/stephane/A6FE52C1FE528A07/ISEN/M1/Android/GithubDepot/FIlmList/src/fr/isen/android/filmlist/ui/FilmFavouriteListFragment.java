package fr.isen.android.filmlist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilmFavouriteListFragment extends FilmListFragment {
	public static final int position = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setTitle("Favourite films");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
