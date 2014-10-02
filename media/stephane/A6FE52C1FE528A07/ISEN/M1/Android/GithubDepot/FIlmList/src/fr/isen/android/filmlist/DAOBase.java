package fr.isen.android.filmlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAOBase {
	  // Nous sommes à la première version de la base
	  // Si je décide de la mettre à jour, il faudra changer cet attribut
	  protected final static int VERSION = 1;
	  // Le nom du fichier qui représente ma base
	  protected final static String NOM = "film_list.db";
	    
	  protected SQLiteDatabase mDb = null;
	  protected DatabaseHandler mHandler = null;
	    
	  public DAOBase(Context pContext) {
	    this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
	  }
	    
	  public void openWriteMode() {
	    // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
	    mDb = mHandler.getWritableDatabase();
	  }
	  
	  public void openReadMode() {
	    // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
	    mDb = mHandler.getReadableDatabase();
	  }
	    
	  public void close() {
	    mDb.close();
	  }
	    
	  public SQLiteDatabase getDb() {
	    return mDb;
	  }
	}