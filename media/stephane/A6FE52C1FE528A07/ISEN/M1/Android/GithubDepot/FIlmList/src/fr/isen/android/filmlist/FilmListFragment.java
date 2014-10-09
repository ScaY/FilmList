package fr.isen.android.filmlist;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	
	public static final String LIST_KEY = "key";
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_film_list, container, false);
    	
    	Bundle args = getArguments();
    	
    	list = new ArrayList<String>();//args.getStringArrayList(FilmListFragment.LIST_KEY);
    	
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
		listview = (ListView) view.findViewById(R.id.listview);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
		 
			@Override public void onItemClick(AdapterView<?> parent, final View
			 view, int position, long id) { final String item = (String)
			 parent.getItemAtPosition(position);
			 	
			}
		 });
    	
        return view;
    }
}