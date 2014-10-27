package fr.isen.android.filmlist.ui;

import java.util.List;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.FavouriteFilmsDAO;
import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmDAO;
import fr.isen.android.filmlist.bdd.ToSeeFilmsDAO;

public class ActionBarCallBack implements ActionMode.Callback {
	
	private String typeKey;
	private Home activity;
	private FilmListFragment filmListFragment;

	public ActionBarCallBack(Home activity, FilmListFragment filmListFragment) {
		this.typeKey = FilmAllListFragment.class.getSimpleName().toString();
		this.activity = activity;
		this.filmListFragment = filmListFragment;
	}

	public void setTypeKey(String typeKey){
		this.typeKey = typeKey;
	}
	
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_share:
			break;
		case R.id.item_delete:
			for (String i : filmListFragment.getItemSelected().keySet()) {
				filmListFragment.getListView().getChildAt(Integer.parseInt(i))
						.setBackground(filmListFragment.getDefaultBackground());
				String nameFilm = filmListFragment.getList().remove(
						Integer.parseInt(i));

				List<Film> films = null;
				if (typeKey.equals(FilmFavouriteListFragment.class
						.getSimpleName().toString())) {
					FavouriteFilmsDAO bdd = new FavouriteFilmsDAO(activity);
					bdd.open();
					films = bdd.getAllFilms();
					long idFilm = getIdFilm(nameFilm, films);
					bdd.delete(idFilm);
					bdd.close();
				}else if(typeKey.equals(FilmAllListFragment.class
						.getSimpleName().toString())) {
					FilmDAO bdd = new FilmDAO(activity);
					bdd.open();
					films = bdd.getAllFilms();
					long idFilm = getIdFilm(nameFilm, films);
					bdd.delete(idFilm);
					bdd.close();
				}else  if(typeKey.equals(FilmToSeeListFragment.class
						.getSimpleName().toString())) {
					ToSeeFilmsDAO bdd = new ToSeeFilmsDAO(activity);
					bdd.open();
					films = bdd.getAllFilms();
					long idFilm = getIdFilm(nameFilm, films);
					bdd.delete(idFilm);
					bdd.close();
				}

				filmListFragment.getAdapter().notifyDataSetChanged();
			}
			filmListFragment.getItemSelected().clear();
			filmListFragment.getActionMode().finish();
			break;
		}
		return false;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {

		for (String i : filmListFragment.getItemSelected().keySet()) {
			filmListFragment.getListView().getChildAt(Integer.parseInt(i))
					.setBackground(filmListFragment.getDefaultBackground());
		}
		filmListFragment.getItemSelected().clear();
		FilmListFragment.additemListener(filmListFragment.getListView(),
				typeKey, filmListFragment.getList(), activity);
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub

		mode.setTitle("Movie is selected");
		return false;
	}

	public long getIdFilm(String nameFilm, List<Film> films) {
		boolean found = false;
		long result = 0;
		int i = 0;
		while (!found && i < films.size()) {
			Film tmp = films.get(i);
			if(tmp.getName().equals(nameFilm)){
				result = tmp.getId();
			}
			i++;
		}

		return result;

	}

}