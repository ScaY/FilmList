package fr.isen.android.filmlist.bdd;

import java.io.Serializable;
import java.sql.Date;

import org.json.JSONObject;

import android.graphics.Bitmap;

public class Film implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Notez que l'identifiant est un long
	private long id;
	private String name;
	private String year;
	private Date releaseDate;
	private String runtime;
	private String director;
	private String story;
	private String imageUrl;
	private String imdbRating;
	private String imdbVotes;
	private String actors;
	private Bitmap image;
  
	public Film(long id, String name) {
	  this.id = id;
	  this.name = name;
	  this.director = "";
	  this.image = null;
	}
	
	public Film(long id, String name, String year, Date releaseDate, String runtime, String director, String story, String imageUrl, String imdbRating, String imdbVotes, String actors) {
	  this.id = id;
	  this.name = name;
	  this.year = year;
	  //this.releaseDate = releaseDate;
	  this.runtime = runtime;
	  this.director = director;
	  this.story = story;
	  this.imageUrl = imageUrl;
	  this.imdbRating = imdbRating;
	  this.imdbVotes = imdbVotes;
	  this.actors = actors;
	  this.image = null;
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
			imageUrl = jsonFilm.getString("Poster");
			imdbRating = jsonFilm.getString("imdbRating");
			imdbVotes = jsonFilm.getString("imdbVotes");
			actors = jsonFilm.getString("Actors");
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}

	public String getImdbVotes() {
		return imdbVotes;
	}

	public void setImdbVotes(String imdbVotes) {
		this.imdbVotes = imdbVotes;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}
	
	public String toStringRate(){
		return "Rating : " + this.imdbRating + "/10";
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
}