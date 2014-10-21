package fr.isen.android.filmlist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilmToSeeListFragment extends FilmListFragment {
	public static final int position = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setTitle("Films to see");
		View view = super.onCreateView(inflater, container, savedInstanceState);
		additemListener(getListView(), this.getClass().getSimpleName().toString());
		return view;
	}
	
	@Override
	public int getPosition() {
		return position;
	}
}
