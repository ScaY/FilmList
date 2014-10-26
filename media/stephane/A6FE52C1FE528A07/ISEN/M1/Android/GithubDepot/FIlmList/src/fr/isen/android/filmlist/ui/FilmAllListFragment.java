package fr.isen.android.filmlist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FilmAllListFragment extends FilmListFragment {
	public static final int position = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setTitle("All films");
		View view = super.onCreateView(inflater, container, savedInstanceState);
		additemListener(getListView(), this.getClass().getSimpleName().toString());
		addItemLongClick(getListView(), this.getClass().getSimpleName().toString());
		return view;
	}

	@Override
	public int getPosition() {
		return position;
	}

}
