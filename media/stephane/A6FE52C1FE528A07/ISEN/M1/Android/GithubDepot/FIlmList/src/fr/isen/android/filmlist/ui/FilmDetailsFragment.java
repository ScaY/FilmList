package fr.isen.android.filmlist.ui;

import java.util.GregorianCalendar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.filmlist.R;

public class FilmDetailsFragment extends Fragment {

	public static final String MOVIE_KEY = "fr.isen.android.filmlist.ui.filmdetailsfragment.moviekey";
	public String filmName;

	public FilmDetailsFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_film_details, container, false);
		
		Bundle args = getArguments();
		
		if(args != null && args.containsKey(MOVIE_KEY)){
			filmName = args.getString(MOVIE_KEY);
		}
		else{
			filmName = "FilmList";
		}
		
		getActivity().setTitle(filmName);
		((TextView) view.findViewById(R.id.film_title)).setText(filmName);
		
		final Button button = (Button) view.findViewById(R.id.button_add_film_calendar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(Events.TITLE, filmName);
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
        });

		
		return view;
	}
}
