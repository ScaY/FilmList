package fr.isen.android.filmlist.bdd;

import java.sql.Date;

import org.json.JSONObject;

public class Film {
	// Notez que l'identifiant est un long
	private long id;
	private String name;
	private String year;
	private Date releaseDate;
	private String runtime;
	private String director;
	private String story;
  
	public Film(long id, String name) {
	  this.id = id;
	  this.name = name;
	  this.director = "";
	}
	
	public Film(long id, String name, String year, Date releaseDate, String runtime, String director, String story) {
	  this.id = id;
	  this.name = name;
	  this.year = year;
	  //this.releaseDate = releaseDate;
	  this.runtime = runtime;
	  this.director = director;
	  this.story = story;
	}
	
	public Film(JSONObject jsonFilm) {
		try {
			id = -1;
			name = jsonFilm.getString("Title");
			year = jsonFilm.getString("Year");
			//releaseDate = Date.valueOf(jsonFilm.getString("Released"));
			runtime = jsonFilm.getString("Runtime");
			director = jsonFilm.getString("Director");
			story = jsonFilm.getString("Plot");
		}
		catch(Exception e) {}
	}
	
	public long getId() {
	  return id;
	}
	
	public void setId(long id) {
	  this.id = id;
	}
	
	public String getName() {
	  return name;
	}
	
	public void setName(String name) {
	  this.name = name;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public String getRuntime() {
		return runtime;
	}
	
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	
	public String getDirector() {
		return director;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getStory() {
		return story;
	}
	
	public void setStory(String story) {
		this.story = story;
	}
}