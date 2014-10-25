package fr.isen.android.filmlist.utils;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.FilmSearchResult;

public class FilmSearchResultAdapter extends ArrayAdapter<FilmSearchResult> {
	public FilmSearchResultAdapter(Context context, ArrayList<FilmSearchResult> films) {
       super(context, 0, films);
    }
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       FilmSearchResult film = getItem(position);    
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_film_search_result, parent, false);
       }
       // Lookup view for data population
       TextView filmTitle = (TextView) convertView.findViewById(R.id.filmTitle);
       TextView filmYear = (TextView) convertView.findViewById(R.id.filmYear);
       TextView filmType = (TextView) convertView.findViewById(R.id.filmType);
       // Populate the data into the template view using the data object
       filmTitle.setText(film.getTitle());
       filmYear.setText(Integer.toString(film.getYear()));
       filmType.setText(film.getType());
       // Return the completed view to render on screen
       return convertView;
   }
}
