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

public class GetFilmDetailsSearch extends AsyncTask<String, Void, JSONObject> {
	private static final String URL = "http://www.omdbapi.com/?i=";
	private HttpClient client;
	private Film film;

	public GetFilmDetailsSearch() {
		client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
				"android");
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
		this.film = new Film(result);
	}

	public Film getFilm(String id) {
		this.film = new Film(doInBackground(id));
		return film;
	}
}
