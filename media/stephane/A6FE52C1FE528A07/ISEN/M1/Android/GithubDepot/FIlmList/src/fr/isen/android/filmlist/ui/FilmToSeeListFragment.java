package fr.isen.android.filmlist.ui;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.filmlist.R;

public class FilmToSeeListFragment extends Fragment {
	private ArrayList<String> list;
	private ArrayAdapter<String> adapter;
	private ListView listview;

	public static final String LIST_KEY = "keyFilmList";
	public static final int position = 1;
	
	public FilmToSeeListFragment() {
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

		if (args != null && args.containsKey(FilmToSeeListFragment.LIST_KEY)) {
			list = args.getStringArrayList(FilmToSeeListFragment.LIST_KEY);
		} else {
			list = new ArrayList<String>();
		}


		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		listview = (ListView) view.findViewById(R.id.listview);
		listview.setAdapter(adapter);
		// Add the listeners on the item (Does not work)
		additemListener(listview);
		adapter.notifyDataSetChanged();
		
		return view;
	}
	
	public void additemListener(ListView listFl) {
		if (listFl != null) {
			listFl.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					FilmDetailsFragment filmDetailsFragment = new FilmDetailsFragment();
					Bundle args = new Bundle();
					args.putString(FilmDetailsFragment.MOVIE_KEY, list.get(position));
					filmDetailsFragment.setArguments(args);
					((Home)getActivity()).setFragment(filmDetailsFragment,
							"filmDetailsFragment", true);
				}
			});
		}
	}
	
	public void refresh(String film) {
		list.add(film);
		adapter.notifyDataSetChanged();
	}
}