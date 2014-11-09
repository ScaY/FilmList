package fr.isen.android.filmlist.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAOBase {
	//à modifier pour mettre à jour la table (la fonction onUpgrade de DatabaseHandler sera exécutée)
	protected final static int VERSION = 8;
	protected final static String NOM = "film_list.db";

	protected SQLiteDatabase mDb = null;
	protected DatabaseHandler mHandler = null;

	public DAOBase(Context pContext) {
		this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
	}

	public void open() {
		mDb = mHandler.getWritableDatabase();
	}

	public void close() {
		mDb.close();
	}

	public SQLiteDatabase getDb() {
		return mDb;
	}
}