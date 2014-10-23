package fr.isen.android.filmlist.bdd;

import java.sql.Date;

import org.json.JSONObject;

public class Film {
	// Notez que l'identifiant est un long
	private long id;
	private String name;
	private int year;
	private Date releaseDate;
	private int runtime;
	private String director;
	private String story;
  
	public Film(long id, String name) {
	  super();
	  this.id = id;
	  this.name = name;
	}
	
	public Film(JSONObject jsonFIlm) {
		
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
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public int getRuntime() {
		return runtime;
	}
	
	public void setRuntime(int runtime) {
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