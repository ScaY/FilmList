package fr.isen.android.filmlist.ui;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.FilmSearchResult;
import fr.isen.android.filmlist.utils.FilmSearchResultAdapter;

public class SearchResultsFragment extends Fragment implements OnItemClickListener {
	public static final int position = 4;
	public static final String LIST_KEY = "FILMS_RESULTS_LIST";
	private ArrayList<FilmSearchResult> list;
	private FilmSearchResultAdapter adapter;
	private ListView listview;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setTitle("Search results");
		View view = inflater.inflate(R.layout.fragment_film_list, container, false);
		
		Bundle args = getArguments();

		if (args != null && args.containsKey(LIST_KEY)) {
			Serializable arg = args.getSerializable(LIST_KEY);
			if(arg instanceof ArrayList<?>) {
				list = (ArrayList<FilmSearchResult>)arg;
			}
			else {
				list = new ArrayList<FilmSearchResult>();
			}
		}
		else {
			list = new ArrayList<FilmSearchResult>();
		}

		adapter = new FilmSearchResultAdapter(getActivity(), list);
		listview = (ListView) view.findViewById(R.id.listview);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		adapter.notifyDataSetChanged();
		
		return view;
	}
	
	public int getPosition() {
		return position;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FilmDetailsFragment filmDetailsFragment = new FilmResultDetailsFragment();
		Bundle args = new Bundle();
		args.putSerializable(FilmDetailsFragment.MOVIE_KEY,
				list.get(position));
		filmDetailsFragment.setArguments(args);
		((Home) getActivity()).setFragment(filmDetailsFragment,
				Home.fragmentStack, true);		
	}
}
