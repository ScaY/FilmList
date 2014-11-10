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
import fr.isen.android.filmlist.utils.DownloadImageTask;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Film> movieItems;

	public CustomListAdapter(Activity activity, List<Film> movieItems) {
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

		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView rating = (TextView) convertView.findViewById(R.id.rating);
		TextView genre = (TextView) convertView.findViewById(R.id.genre);
		TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

		// getting movie data for the row
		Film f = movieItems.get(position);

		// image ImageView image = (ImageView) activity
		ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
		DownloadImageTask task = new DownloadImageTask(image);
		if (f.getImageUrl() != null && f.getImageUrl() != "") {
			task.execute(f.getImageUrl());
		}
		
		title.setText(f.getName());
		genre.setText(f.getDirector());
		year.setText(String.valueOf(f.getYear()));
		rating.setText(f.toStringRate());

		return convertView;
	}

}