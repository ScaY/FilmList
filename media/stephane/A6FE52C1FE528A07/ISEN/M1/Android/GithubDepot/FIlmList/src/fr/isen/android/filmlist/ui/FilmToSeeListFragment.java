package fr.isen.android.filmlist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilmToSeeListFragment extends FilmListFragment {
	public static final int POSTION = 1;
	public static final String NAME_CLASS = "fr.isen.android.filmlist.filmtoseelistfragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setTitle("Wtach movies");
		String typeKey = this.getClass().getSimpleName().toString();
		View view = super.onCreateView(inflater, container, savedInstanceState);
		additemListener(getListView(), typeKey.toString(), getList(),
				(HomeActivity) getActivity());
		addItemLongClick(getListView(), typeKey);
		getActionBarCallBack().setTypeKey(typeKey);
		return view;
	}


}
