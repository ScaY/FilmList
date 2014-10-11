package fr.isen.android.filmlist.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.filmlist.R;

public class FilmListFragment extends Fragment {
	private ArrayList<String> list;
	private ArrayAdapter<String> adapter;
	private ListView listview;

	public static final String LIST_KEY = "keyFilmList";

	public FilmListFragment() {
		super();
	}
	
	public ListView getListView(){
		return listview;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_film_list, container,
				false);

		Bundle args = getArguments();

		if (args != null && args.containsKey(FilmListFragment.LIST_KEY)) {
			list = args.getStringArrayList(FilmListFragment.LIST_KEY);
		} else {
			list = new ArrayList<String>();
		}


		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		listview = (ListView) view.findViewById(R.id.listview);
		listview.setAdapter(adapter);
		//adapter.notifyDataSetChanged();

		return view;
	}
	
	public void refresh(String film) {
		list.add(film);
		adapter.notifyDataSetChanged();
	}
}