package fr.isen.android.filmlist.utils;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import fr.isen.android.filmlist.bdd.Film;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	  ImageView bmImage;
	  Film film;
	  
	  public DownloadImageTask(ImageView bmImage, Film film) {
	      this.bmImage = bmImage;
	      this.film = film;
	  }
	
	  protected Bitmap doInBackground(String... urls) {
	      String urldisplay = urls[0];
	      Bitmap mIcon11 = null;
	      try {
	        InputStream in = new java.net.URL(urldisplay).openStream();
	        mIcon11 = BitmapFactory.decodeStream(in);
	      } catch (Exception e) {
	          Log.e("Error", e.getMessage());
	          e.printStackTrace();
	      }
	      return mIcon11;
	  }
	
	  protected void onPostExecute(Bitmap result) {
		  if(bmImage != null && result != null){
			  bmImage.setImageBitmap(result);
			  film.setImage(result);
		  }
	  }
	}