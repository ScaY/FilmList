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
		String typeKey = this.getClass().getSimpleName().toString();
		View view = super.onCreateView(inflater, container, savedInstanceState);
		additemListener(getListView(), typeKey.toString(), getList(),
				(Home) getActivity());
		addItemLongClick(getListView(), typeKey);
		getActionBarCallBack().setTypeKey(typeKey);
		return view;
	}

	@Override
	public int getPosition() {
		return position;
	}
}
