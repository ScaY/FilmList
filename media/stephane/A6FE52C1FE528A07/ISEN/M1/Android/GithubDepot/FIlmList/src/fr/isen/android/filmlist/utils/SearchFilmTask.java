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
import org.json.JSONObject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import fr.isen.android.filmlist.bdd.FilmSearchResult;
import fr.isen.android.filmlist.ui.FilmListFragment;
import fr.isen.android.filmlist.ui.Home;
import fr.isen.android.filmlist.ui.LoadingFragment;
import fr.isen.android.filmlist.ui.SearchResultsFragment;

public class SearchFilmTask extends AsyncTask<String, Void, JSONObject> {
	private static String URL = "http://www.omdbapi.com/";
	private HttpClient client;
	private Home home;
	
	public SearchFilmTask(Home _home) {
		client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
		home = _home;
	}
	
	public JSONObject doInBackground(String... filmNames) {
		JSONObject jsonResponse = null;
		String urlWithArguments = URL + "?s=" + filmNames[0];
		
		try {
			HttpGet request = new HttpGet(urlWithArguments);
	        request.setHeader("Content-type", "application/json");
	        request.setURI(new URI(urlWithArguments));
	        HttpResponse response = client.execute(request);
	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	        StringBuffer result = new StringBuffer();
	        String line = "";
	        while ((line = rd.readLine()) != null) {
	            result.append(line);
	        }

	        jsonResponse = new JSONObject(result.toString());
		}
		catch(Exception e) {
			int i = 1;
		}
        
        return jsonResponse;
	}
	
	protected void onPreExecute() {
		home.setFragment(new LoadingFragment(), Home.fragmentStack, false);
    }

    protected void onPostExecute(JSONObject result) {		
		try {
			Fragment fragment = new SearchResultsFragment();
	    	ArrayList<String> list;
			Bundle args = null;
			JSONArray searchArray = result.getJSONArray("Search");
			list = new ArrayList<String>();

			for(int i = 0; i < searchArray.length(); i++) {
				JSONObject searchResult = searchArray.getJSONObject(i);
				FilmSearchResult film = new FilmSearchResult(searchResult);
				list.add(film.getTitle());
			}
			
			args = new Bundle();
			args.putStringArrayList(FilmListFragment.LIST_KEY, list);
			fragment.setArguments(args);
			
			if (fragment != null) {
				home.setFragment(fragment, Home.fragmentStack, false);
			}
		}
		catch(Exception e) {}
    }
}
