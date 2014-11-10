package fr.isen.android.filmlist.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONObject;

import android.os.AsyncTask;
import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.ui.FilmDetailsFragment;

public class GetFilmDetailsTask extends AsyncTask<String, Void, JSONObject> {
	private static final String URL = "http://www.omdbapi.com/?i=";
	private HttpClient client;
	private FilmDetailsFragment fragment;

	public GetFilmDetailsTask(FilmDetailsFragment _fragment) {
		client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
				"android");
		fragment = _fragment;
	}

	public JSONObject doInBackground(String... imdbId) {
		JSONObject jsonResponse = null;
		String urlWithArguments = URL + imdbId[0];

		try {
			HttpGet request = new HttpGet(urlWithArguments);
			request.setHeader("Content-type", "application/json");
			request.setURI(new URI(urlWithArguments));
			HttpResponse response = client.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			jsonResponse = new JSONObject(result.toString());
		} catch (Exception e) {}

		return jsonResponse;
	}

	public void onPostExecute(JSONObject result) {
		fragment.film = new Film(result);
		fragment.setFilmView();
	}
}
