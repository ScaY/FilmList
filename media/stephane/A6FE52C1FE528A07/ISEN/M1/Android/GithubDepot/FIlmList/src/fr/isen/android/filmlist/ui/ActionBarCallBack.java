package fr.isen.android.filmlist.ui;

import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.FavouriteFilmsDAO;
import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmDAO;
import fr.isen.android.filmlist.bdd.ToSeeFilmsDAO;

public class ActionBarCallBack implements ActionMode.Callback {

	private String typeKey;
	private HomeActivity activity;
	private FilmListFragment filmListFragment;
	private MenuItem itemShare;
	private ShareActionProvider shareActionProvider;
	
	public ActionBarCallBack(HomeActivity activity,
			FilmListFragment filmListFragment) {
		this.typeKey = FilmAllListFragment.class.getSimpleName().toString();
		this.activity = activity;
		this.filmListFragment = filmListFragment;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_delete:

			for (String i : filmListFragment.getItemSelected().keySet()) {
				filmListFragment.getListView().getChildAt(Integer.parseInt(i))
						.setBackgroundColor(Color.WHITE);
				String nameFilm = filmListFragment.getList()
						.remove(Integer.parseInt(i)).getName();

				List<Film> films = null;
				if (typeKey.equals(FilmFavouriteListFragment.class
						.getSimpleName().toString())) {
					FavouriteFilmsDAO bdd = new FavouriteFilmsDAO(activity);
					bdd.open();
					films = bdd.getAllFilms();
					long idFilm = getIdFilm(nameFilm, films);
					bdd.delete(idFilm);
					bdd.close();
				} else if (typeKey.equals(FilmAllListFragment.class
						.getSimpleName().toString())) {
					FilmDAO bdd = new FilmDAO(activity);
					bdd.open();
					films = bdd.getAllFilms();
					long idFilm = getIdFilm(nameFilm, films);
					bdd.delete(idFilm);
					bdd.close();
				} else if (typeKey.equals(FilmToSeeListFragment.class
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
		return true;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);

		itemShare = menu.findItem(R.id.item_share);
		
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {

		for (String i : filmListFragment.getItemSelected().keySet()) {
			filmListFragment.getListView().getChildAt(Integer.parseInt(i))
					.setBackgroundColor(Color.WHITE);
		}
		filmListFragment.getItemSelected().clear();
		FilmListFragment.additemListener(filmListFragment.getListView(),
				typeKey, filmListFragment.getList(), activity);
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

		// Set the action for the share button
		shareActionProvider = (ShareActionProvider) itemShare
				.getActionProvider();
		shareActionProvider.setShareIntent(getDefaultShareIntent());
		return false;
	}

	public ShareActionProvider getShareActionProvider() {
		return shareActionProvider;
	}

	public void setShareActionProvider(ShareActionProvider shareActionProvider) {
		this.shareActionProvider = shareActionProvider;
	}

	public long getIdFilm(String nameFilm, List<Film> films) {
		boolean found = false;
		long result = 0;
		int i = 0;
		while (!found && i < films.size()) {
			Film tmp = films.get(i);
			if (tmp.getName().equals(nameFilm)) {
				result = tmp.getId();
			}
			i++;
		}

		return result;

	}

	public Intent getDefaultShareIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Movies List");
		String msg = "";
		Set<String> nameFilms = filmListFragment.getItemSelected().keySet();

		if (typeKey.equals(FilmToSeeListFragment.class.getSimpleName()
				.toString())) {
			msg = "I'm going to see: ";
		} else if (typeKey.equals(FilmFavouriteListFragment.class
				.getSimpleName().toString())) {
			if (nameFilms.size() == 1) {
				msg = "My favorite movie: ";
			} else {
				msg = "My favorite movie: ";
			}
		} else if (typeKey.equals(FilmAllListFragment.class.getSimpleName()
				.toString())) {
			if (nameFilms.size() == 1) {
				msg = "My movie from FilmList: ";
			} else {
				msg = "My movies from FilmList: ";
			}
		}

		for (String i : filmListFragment.getItemSelected().keySet()) {

			String filmName = filmListFragment.getList()
					.get(Integer.parseInt(i)).getName();
			if (nameFilms.size() == 1) {
				msg += filmName + " ";
			} else if (Integer.parseInt(i) == nameFilms.size() - 1) {
				msg += filmName + " ";
			} else {
				msg += filmName + ", ";
			}

		}

		intent.putExtra(Intent.EXTRA_TEXT, msg + "!");
		return intent;
	}

}