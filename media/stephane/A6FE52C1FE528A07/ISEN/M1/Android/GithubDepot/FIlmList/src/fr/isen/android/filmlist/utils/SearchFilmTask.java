package fr.isen.android.filmlist.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmSearchResult;
import fr.isen.android.filmlist.ui.GetSearchResult;
import fr.isen.android.filmlist.ui.HomeActivity;
import fr.isen.android.filmlist.ui.LoadingFragment;
import fr.isen.android.filmlist.ui.SearchResultsFragment;

public class SearchFilmTask extends AsyncTask<String, Void, JSONObject> {
	private static final String URL = "http://www.omdbapi.com/?s=";
	private HttpClient client;
	private HomeActivity home;
	private ArrayList<Film> listFilm;

	public SearchFilmTask(HomeActivity _home) {
		client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
				"android");
		home = _home;
	}

	public JSONObject doInBackground(String... filmNames) {
		JSONObject jsonResponse = null;
		String urlWithArguments = URL + filmNames[0].replace(" ", "+");

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
		} catch (Exception e) {
			int i = 1;
		}

		try {
			JSONArray searchArray = jsonResponse.getJSONArray("Search");
			if (listFilm == null) {
				listFilm = new ArrayList<Film>();

				for (int i = 0; i < searchArray.length(); i++) {
					JSONObject searchResult = searchArray.getJSONObject(i);
					FilmSearchResult filmSearch = new FilmSearchResult(
							searchResult);

					JSONObject jsonResponse2 = null;
					urlWithArguments = "http://www.omdbapi.com/?i="
							+ filmSearch.getImdbID();

					try {
						HttpGet request = new HttpGet(urlWithArguments);
						request.setHeader("Content-type", "application/json");
						request.setURI(new URI(urlWithArguments));
						HttpResponse response = client.execute(request);
						BufferedReader rd = new BufferedReader(
								new InputStreamReader(response.getEntity()
										.getContent()));

						StringBuffer result = new StringBuffer();
						String line = "";
						while ((line = rd.readLine()) != null) {
							result.append(line);
						}

						jsonResponse2 = new JSONObject(result.toString());
						listFilm.add(new Film(jsonResponse2));
					} catch (Exception e) {
						int erreur = 1;
					}
				}
			}

		} catch (Exception e) {
			int erreur = 0;
		}
		return jsonResponse;
	}

	protected void onPreExecute() {
		home.setFragment(new LoadingFragment(), HomeActivity.STACK_FILMLIST,
				false);
	}

	protected void onPostExecute(JSONObject result) {
		try {
			Fragment fragment = new SearchResultsFragment();

			Bundle args = null;
			/*
			 * ArrayList<FilmSearchResult> list = new
			 * ArrayList<FilmSearchResult>(); JSONArray searchArray =
			 * result.getJSONArray("Search");
			 * 
			 * for (int i = 0; i < searchArray.length(); i++) { JSONObject
			 * searchResult = searchArray.getJSONObject(i); FilmSearchResult
			 * film = new FilmSearchResult(searchResult); list.add(film); }
			 */

			args = new Bundle();
			args.putSerializable(SearchResultsFragment.LIST_KEY, listFilm);
			fragment.setArguments(args);
			((SearchResultsFragment) fragment).setList(listFilm);

			if (fragment != null) {
				home.setFragment(fragment, HomeActivity.STACK_FILMLIST, false);
				GetSearchResult.getInstance().setSearchResultFragment(
						(SearchResultsFragment) fragment);
			}
		} catch (Exception e) {
		}
	}
}
