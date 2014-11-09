package fr.isen.android.filmlist.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmSearchResult;
import fr.isen.android.filmlist.utils.DownloadImageTask;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<FilmSearchResult> movieItems;

	public CustomListAdapter(Activity activity, List<FilmSearchResult> movieItems) {
		this.activity = activity;
		this.movieItems = movieItems;
	}

	@Override
	public int getCount() {
		return movieItems.size();
	}

	@Override
	public Object getItem(int location) {
		return movieItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		// NetworkImageView thumbNail = (NetworkImageView) convertView
		// .findViewById(R.id.thumbnail);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView rating = (TextView) convertView.findViewById(R.id.rating);
		TextView genre = (TextView) convertView.findViewById(R.id.genre);
		TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

		// getting movie data for the row
		FilmSearchResult f = movieItems.get(position);

		// image ImageView image = (ImageView) activity
		//ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
		//DownloadImageTask task = new DownloadImageTask(image);
		//task.execute(f.getImageUrl());

		// title
		title.setText(f.getImdbID());

		// rating
		rating.setText("Rating: " + String.valueOf(5));

		// genre
		/*String genreStr = "";
		for (String str : m.getGenre()) {
			genreStr += str + ", ";
		}
		genreStr = genreStr.length() > 0 ? genreStr.substring(0,
				genreStr.length() - 2) : genreStr;*/
		genre.setText(f.getTitle());

		// release year
		year.setText(String.valueOf(f.getYear()));

		return convertView;
	}

}