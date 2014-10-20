package fr.isen.android.filmlist.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ShareActionProvider;

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.FavouriteFilmsDAO;
import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmDAO;
import fr.isen.android.filmlist.bdd.ToSeeFilmsDAO;

public class Home extends FragmentActivity {
	private String[] navigationArray;
	private DrawerLayout drawerLayout;
	private ListView drawerList;

	private ActionBarDrawerToggle drawerToggle;
	private CharSequence drawerTitle;
	private CharSequence mTitle;

	private ShareActionProvider mShareActionProvider;

	private android.app.Fragment fragment;
	private android.app.Fragment filmToSeeFragment;

	private ArrayList<String> list;

	public static final String LIST_KEY = "keyHomeActivity";

	public static final String fragmentStack = "fragmentStack";
	private FilmDAO filmDAO;
	private FavouriteFilmsDAO favouriteDAO;
	private ToSeeFilmsDAO toSeeDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		this.filmDAO = new FilmDAO(this);
		this.favouriteDAO = new FavouriteFilmsDAO(this);
		this.toSeeDAO = new ToSeeFilmsDAO(this);

		// Initialization of the drawer
		navigationArray = getResources().getStringArray(
				R.array.navigation_array);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		drawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, navigationArray));
		// Set the list's click listener
		drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {
				selectItem(position);
			}
		});

		mTitle = drawerTitle = getTitle();

		// Set the up button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. **/
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. **/
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu();
			}
		};

		// Set the drawer toggle as the DrawerListener
		drawerLayout.setDrawerListener(drawerToggle);

		filmDAO.open();
		final List<Film> films = filmDAO.getAllFilms();
		filmDAO.close();

		list = new ArrayList<String>();

		for (Film film : films) {
			list.add(film.getName());
		}

		// Initialize filmToSeeFragment
		fragment = new FilmToSeeListFragment();
		Bundle args = new Bundle();
		args.putStringArrayList(FilmListFragment.LIST_KEY, list);
		fragment.setArguments(args);

		filmToSeeFragment = new FilmToSeeListFragment();

		FilmToSeeListFragment fl = (FilmToSeeListFragment) filmToSeeFragment;
		fl.setList(list);
		fl.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list));
		fl.setListview((ListView) findViewById(R.id.listview));

		android.app.Fragment filmAllListFragment = new FilmAllListFragment();

		FilmAllListFragment fa = (FilmAllListFragment) filmAllListFragment;
		fa.setList(list);
		fa.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list));
		fa.setListview((ListView) findViewById(R.id.listview));

		if (savedInstanceState != null) {
			// Retrieve the previous fragment
			android.app.Fragment fragment = getFragmentManager()
					.findFragmentByTag(fragmentStack);

			// Case where the fragment is the detail about a film
			if (fragment instanceof FilmDetailsFragment) {
				FilmDetailsFragment fd = (FilmDetailsFragment) fragment;

				// Check where the fragment has been lunched (From FilmVarite or
				// FilmToSee)
				if (fd.getType().equals(
						FilmToSeeListFragment.class.getSimpleName().toString())) {
					// Initialize the stack
					setFragment(filmToSeeFragment, fragmentStack, true);
					setFragment(fragment, fragmentStack, true);
				} else if (fd.getType().equals(
						FilmAllListFragment.class.getSimpleName().toString())) {
					// Initialize the stack
					setFragment(filmAllListFragment, fragmentStack, true);
					setFragment(fragment, fragmentStack, true);
				}
				setTitle(fd.getType());
			} else {
				setFragment(fragment, fragmentStack, false);
				setTitle("TestTest");
			}
		} else {
			// Case where it is the first time that the application is
			// initialized
			setFragment(filmToSeeFragment, fragmentStack, false);

		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);

		// Ajout du listener sur la barre de recherche
		final SearchView searchView = (SearchView) menu.findItem(
				R.id.action_new).getActionView();
		final MenuItem searchMenuItem = menu.findItem(R.id.action_new);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {

				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				filmDAO.open();
				Film film = filmDAO.insert(query);
				filmDAO.close();

				if (fragment instanceof FilmListFragment) {
					FilmListFragment fl = (FilmListFragment) fragment;
					fl.refresh(film.getName());
				}

				searchMenuItem.collapseActionView();
				searchView.setQuery("", false);

				return true;
			}
		});

		// Localisation du menu item avec ShareActionProvider
		MenuItem item = menu.findItem(R.id.action_share);
		// Ajout du listener
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		mShareActionProvider.setShareIntent(getDefaultShareIntent());

		return true;
	}

	private Intent getDefaultShareIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
		intent.putExtra(Intent.EXTRA_TEXT, "Extra Text");
		return intent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item.getItemId() == android.R.id.home) {
			getFragmentManager().popBackStack();
			drawerToggle.setDrawerIndicatorEnabled(true);
		}
		return super.onOptionsItemSelected(item);
	}

	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on
		// position

		List<Film> films = null;
		Bundle args = null;

		switch (position) {

		case About.position:
			fragment = new About();
			break;

		case FilmToSeeListFragment.position:
			fragment = new FilmToSeeListFragment();
			toSeeDAO.open();
			films = toSeeDAO.getAllFilms();
			toSeeDAO.close();

			list = new ArrayList<String>();

			for (Film film : films) {
				list.add(film.getName());
			}

			// Initialize filmToSeeFragment
			fragment = new FilmToSeeListFragment();
			args = new Bundle();
			args.putStringArrayList(FilmListFragment.LIST_KEY, list);
			fragment.setArguments(args);
			break;

		case FilmAllListFragment.position:
			fragment = new FilmAllListFragment();
			filmDAO.open();
			films = filmDAO.getAllFilms();
			filmDAO.close();

			list = new ArrayList<String>();

			for (Film film : films) {
				list.add(film.getName());
			}

			// Initialize filmToSeeFragment
			fragment = new FilmAllListFragment();
			args = new Bundle();
			args.putStringArrayList(FilmListFragment.LIST_KEY, list);
			fragment.setArguments(args);
			break;

		case FilmFavouriteListFragment.position:
			fragment = new FilmFavouriteListFragment();
			favouriteDAO.open();
			films = favouriteDAO.getAllFilms();
			favouriteDAO.close();

			list = new ArrayList<String>();

			for (Film film : films) {
				list.add(film.getName());
			}

			// Initialize filmToSeeFragment
			fragment = new FilmFavouriteListFragment();
			args = new Bundle();
			args.putStringArrayList(FilmListFragment.LIST_KEY, list);
			fragment.setArguments(args);
			break;
		}

		setFragment(fragment, fragmentStack, false);

		// Highlight the selected item, update the title, and close the
		// drawer
		drawerList.setItemChecked(position, true);
		drawerLayout.closeDrawer(drawerList);

	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	public void setFragment(android.app.Fragment fragment, String name,
			boolean disableDrawer) {
		if (disableDrawer) {
			drawerToggle.setDrawerIndicatorEnabled(false);
		}
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.addToBackStack(name);
		ft.replace(R.id.film_list_fragment, fragment, name);
		ft.commit();
	}
}
