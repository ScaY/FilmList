package fr.isen.android.filmlist.bdd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class FilmDAO extends FilmsListDAO {
	  public FilmDAO(Context pContext) {
		  super(pContext);
	  }
	  
	  public long insert(Film f) {
		  ContentValues value = filmToValues(f);
		  return mDb.insert(DatabaseHandler.FILM_TABLE_NAME, null, value);
	  }

	  public void delete(Film film) {
		  delete(film.getId());
	  }
	  
	  public void delete(long id) {
		  String[] args = {Long.toString(id)};
		  mDb.delete(DatabaseHandler.TOSEE_TABLE_NAME, DatabaseHandler.TOSEE_FILM_KEY + " = ?", args);
		  mDb.delete(DatabaseHandler.FAVOURITE_TABLE_NAME, DatabaseHandler.FAVOURITE_FILM_KEY + " = ?", args);
		  mDb.delete(DatabaseHandler.FILM_TABLE_NAME, DatabaseHandler.FILM_KEY + " = ?", args);
	  }

	  public void edit(Film f) {
		  ContentValues value = filmToValues(f);
		  mDb.update(DatabaseHandler.FILM_TABLE_NAME, value, DatabaseHandler.FILM_KEY  + " = ?", new String[] {String.valueOf(f.getId())});
	  }

	  public Film select(long id) {
		  String columns[] = DatabaseHandler.FILM_ALL_COLUMNS;
		  String args[] = {String.valueOf(id)};
		  Film film = null;
		  Cursor c = mDb.query(DatabaseHandler.FILM_TABLE_NAME, columns, DatabaseHandler.FILM_KEY + " = ?", args, "", "", "");
		  if(c.moveToFirst()) {
			  film =  cursorToFilm(c);
		  }
		  return film;
	  }
	  
	  public long getFilmId(Film film) {
		  long id = -1;
		  
		  String columns[] = {DatabaseHandler.FILM_KEY};
		  String selectArgs = DatabaseHandler.FILM_NAME + " = ?";
		  String args[] = {film.getName()};
		  Cursor c = mDb.query(DatabaseHandler.FILM_TABLE_NAME, columns, selectArgs, args, "", "", "");
		  if(c.moveToFirst()) {
			  id =  c.getLong(0);
		  }
		  return id;
	  }
	  
	  public Film select(String name) {
		  String columns[] = DatabaseHandler.FILM_ALL_COLUMNS;
		  String args[] = {name};
		  Film film = null;
		  Cursor c = mDb.query(DatabaseHandler.FILM_TABLE_NAME, columns, DatabaseHandler.FILM_NAME + " = ?", args, "", "", "");
		  if(c.moveToFirst()) {
			  film =  cursorToFilm(c);
		  }
		  return film;
	  }
	  
	  @Override
	  public List<Film> getAllFilms() {
		  List<Film> films = new ArrayList<Film>();
		  
		  Cursor c = mDb.query(DatabaseHandler.FILM_TABLE_NAME, DatabaseHandler.FILM_ALL_COLUMNS, null, null, null, null, null);
		  
		  while(c.moveToNext()) {
			  Film film = cursorToFilm(c);
			  films.add(film);
		  }
		  
		  c.close();
		  
		  return films;
	  }
	  
	  private ContentValues filmToValues(Film f) {
		  ContentValues value = new ContentValues();
		  value.put(DatabaseHandler.FILM_NAME, f.getName());
		  value.put(DatabaseHandler.FILM_YEAR, f.getYear());
		  //value.put(DatabaseHandler.FILM_RELEASE_DATE, new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.sss").format(f.getReleaseDate()));
		  value.put(DatabaseHandler.FILM_RUNTIME, f.getRuntime());
		  value.put(DatabaseHandler.FILM_DIRECTOR, f.getDirector());
		  value.put(DatabaseHandler.FILM_STORY, f.getStory());
		  value.put(DatabaseHandler.FILM_IMAGE, f.getImageUrl());
		  value.put(DatabaseHandler.FILM_RATING, f.getImdbRating());
		  value.put(DatabaseHandler.FILM_VOTES, f.getImdbVotes());
		  value.put(DatabaseHandler.FILM_ACTORS, f.getActors());
		  
		  return value;
	  }
	}
