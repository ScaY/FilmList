package fr.isen.android.filmlist.bdd;

import org.json.JSONObject;

public class FilmSearchResult {
	private String title;
	private int year;
	private String imdbID;
	private String type;
	
	public FilmSearchResult() {
		title = "";
		year = 0;
		imdbID = "";
		type = "";
	}
	
	public FilmSearchResult(String _title, int _year, String _imdbID, String _type) {
		title = _title;
		year = _year;
		imdbID = _imdbID;
		type = _type;
	}
	
	public FilmSearchResult(JSONObject jsonFilm) {
		try {
			title = jsonFilm.getString("Title");
			year = jsonFilm.getInt("Year");
			imdbID = jsonFilm.getString("imdbID");
			type = jsonFilm.getString("Type");
		}
		catch(Exception e) {}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getImdbID() {
		return imdbID;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
