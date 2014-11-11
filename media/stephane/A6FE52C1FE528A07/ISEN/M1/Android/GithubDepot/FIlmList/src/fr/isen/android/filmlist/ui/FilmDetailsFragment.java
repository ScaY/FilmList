package fr.isen.android.filmlist.ui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.FavouriteFilmsDAO;
import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmDAO;
import fr.isen.android.filmlist.bdd.ToSeeFilmsDAO;
import fr.isen.android.filmlist.utils.DownloadImageTask;

public class FilmDetailsFragment extends Fragment {

	public static final String MOVIE_KEY = "fr.isen.android.filmlist.ui.filmdetailsfragment.moviekey";
	public static final String TYPE_KEY = "fr.isen.android.filmlist.ui.filmdetailsfragment.typekey";

	public Film film;
	public String type;

	public FilmDetailsFragment() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type_) {
		this.type = type_;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_film_details, container,
				false);

		Bundle args = getArguments();
		if (args != null && args.containsKey(MOVIE_KEY)) {
			Serializable arg = args.getSerializable(MOVIE_KEY);
			if (arg instanceof Film) {
				film = (Film) arg;
			}
		}

		// Set the button to add the movie to the calendar
		final Button button = (Button) view
				.findViewById(R.id.button_add_film_calendar);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addFilmToDB();
				ToSeeFilmsDAO toSeeDAO = new ToSeeFilmsDAO(getActivity());
				toSeeDAO.open();

				if (toSeeDAO.select(film.getId()) != null) {
					toSeeDAO.delete(film);
					button.setText("Add to planning");
					button.getBackground().setColorFilter(Color.rgb(204, 204, 204),
							Mode.SRC);
				} else {
					toSeeDAO.insert(film);
					button.setText("Remove from planning");
					button.getBackground().setColorFilter(Color.rgb(204, 204, 153),
							Mode.SRC);

					Intent intent = new Intent(Intent.ACTION_INSERT);
					intent.setType("vnd.android.cursor.item/event");
					intent.putExtra(Events.TITLE, film.getName());
					intent.putExtra(Events.EVENT_LOCATION, "Home");
					intent.putExtra(Events.DESCRIPTION, "Watch this movie.");

					// Setting dates
					GregorianCalendar calDate = new GregorianCalendar();
					intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
							calDate.getTimeInMillis());
					intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
							calDate.getTimeInMillis());

					// Making it private and shown as busy
					intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);

					intent.setData(CalendarContract.Events.CONTENT_URI);
					startActivity(intent);
				}

				toSeeDAO.close();

			}
		});

		final Button favourite = (Button) view
				.findViewById(R.id.button_add_film_favourites);

		favourite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFilmToDB();
				FavouriteFilmsDAO favouriteDAO = new FavouriteFilmsDAO(
						getActivity());
				favouriteDAO.open();

				if (favouriteDAO.select(film.getId()) != null) {
					favouriteDAO.delete(film);
					favourite.setText("Add to favourites");
					favourite.getBackground().setColorFilter(Color.rgb(204, 204, 204),
							Mode.SRC);
				} else {
					favouriteDAO.insert(film);
					favourite.setText("Remove from favourites");
					favourite.getBackground().setColorFilter(Color.rgb(204, 204, 153),
							Mode.SRC);
				}

				favouriteDAO.close();
			}
		});

		args = getArguments();
		setRetainInstance(true);
		setType(retrieveStringArgs(TYPE_KEY, args));

		return view;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		setFilmView();
	}

	public String retrieveStringArgs(String key, Bundle args) {
		String value = null;
		if (args != null && args.containsKey(key)) {
			value = args.getString(key);
		}
		return value;
	}

	private void addFilmToDB() {
		if (film != null && film.getId() == -1) {
			FilmDAO dao = new FilmDAO(getActivity());
			dao.open();
			long id = dao.getFilmId(film);
			if (id == -1) {
				id = dao.insert(film);
			}
			film.setId(id);
			dao.close();

			if (film.getImage() != null) {
				saveImageToFile(film.getImage(), film.getName() + ".png");
			}
		}
	}

	public void setFilmView() {
		Activity activity = getActivity();
		if (film != null) {

			activity.setTitle(film.getName());
			((TextView) activity.findViewById(R.id.film_details_title))
					.setText(film.getName());
			((TextView) activity.findViewById(R.id.film_details_director))
					.setText(film.getDirector());
			((TextView) activity.findViewById(R.id.film_details_actors))
					.setText(film.getActors());
			((TextView) activity.findViewById(R.id.film_details_year))
					.setText(film.getYear());
			((TextView) activity.findViewById(R.id.film_details_duration))
					.setText(film.getRuntime());
			((TextView) activity.findViewById(R.id.film_details_rating))
					.setText(film.getImdbRating() + "/10 from "
							+ film.getImdbVotes() + " users");
			((TextView) activity.findViewById(R.id.film_details_story))
					.setText(film.getStory());

			ImageView image = (ImageView) activity
					.findViewById(R.id.imageView1);
			if (film.getImage() == null) {
				DownloadImageTask task = new DownloadImageTask(image, film);
				task.execute(film.getImageUrl());
			} else {
				image.setImageBitmap(film.getImage());
			}

			final Button button = (Button) activity
					.findViewById(R.id.button_add_film_calendar);
			ToSeeFilmsDAO toSeeDAO = new ToSeeFilmsDAO(activity);
			toSeeDAO.open();
			if (toSeeDAO.select(film.getId()) != null) {

				button.setText("Remove from planning");
				button.getBackground().setColorFilter(Color.rgb(204, 204, 153),
						Mode.SRC);
			}else{
				button.getBackground().setColorFilter(Color.rgb(204, 204, 204),
						Mode.SRC);
			}

			toSeeDAO.close();

			final Button favourite = (Button) getActivity().findViewById(
					R.id.button_add_film_favourites);
			FavouriteFilmsDAO favouriteDAO = new FavouriteFilmsDAO(
					getActivity());
			favouriteDAO.open();
			if (favouriteDAO.select(film.getId()) != null) {
				favourite.setText("Remove from favourites");
				favourite.getBackground().setColorFilter(
						Color.rgb(204, 204, 153), Mode.SRC);
			}else{
				favourite.getBackground().setColorFilter(Color.rgb(204, 204, 204),
						Mode.SRC);
			}
			favouriteDAO.close();
		}
	}

	public void saveImageToFile(Bitmap image, String fileName) {
		FileOutputStream out = null;
		try {
			out = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
			image.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (Exception e) {
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
	}
}
