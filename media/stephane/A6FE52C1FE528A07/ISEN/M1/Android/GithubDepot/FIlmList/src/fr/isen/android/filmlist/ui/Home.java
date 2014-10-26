package fr.isen.android.filmlist.ui;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import fr.isen.android.filmlist.fragments.FragFilmList;

public class Home extends FragmentActivity {
	private String[] navigationArray;
	private DrawerLayout drawerLayout;
	private ListView drawerList;

	private ActionBarDrawerToggle drawerToggle;
	private CharSequence drawerTitle;
	private CharSequence mTitle;

	private ShareActionProvider mShareActionProvider;

	private ArrayList<String> list;
	public static ArrayAdapter<String> adapter;
	public static ListView listview;

	private android.app.Fragment fragment;
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

		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		listview = (ListView) findViewById(R.id.listview);
		
		// Set the DAO for the FragListFragment singleton
		FragFilmList fgl = FragFilmList.getInstance();
		fgl.setFavouriteFIlmDAO(favouriteDAO);
		fgl.setFilmDAO(filmDAO);
		fgl.setToSeeFilmDAO(toSeeDAO);

		// Initialize the fragment of movies to see
		FilmToSeeListFragment filmToSeeFragment = (FilmToSeeListFragment) fgl
				.getFragment(FilmToSeeListFragment.class.getSimpleName()
						.toString());
		filmToSeeFragment.setList(list);
		filmToSeeFragment.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list));
		filmToSeeFragment.setListview((ListView) findViewById(R.id.listview));

		// Initialize the fragment of all movies
		FilmAllListFragment filmAllListFragment = (FilmAllListFragment) fgl
				.getFragment(FilmAllListFragment.class.getSimpleName()
						.toString());

		// Case where there is a screen rotation
		if (savedInstanceState != null) {
			// Retrieve the previous fragment
			fragment = getFragmentManager()
					.findFragmentByTag(fragmentStack);

			// Case where the fragment is the detail about a film
			if (fragment instanceof FilmDetailsFragment) {
				FilmDetailsFragment fd = (FilmDetailsFragment) fragment;

				// Check where the fragment has been lunched (From FilmFavorite
				// or
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
			} else if(fragment instanceof FilmListFragment){
				((FilmListFragment) fragment).checkSelectionMode();
				setFragment(fragment, fragmentStack, false);
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
		final Home home = this;
		
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

				if (fragment instanceof FilmAllListFragment) {
					FilmListFragment fl = (FilmListFragment) fragment;
					fl.refresh(film.getName());
				}
				else {
					fragment = FragFilmList.getInstance().getFragment(
							FilmAllListFragment.class.getSimpleName().toString());
					
					if (fragment != null) {
						setFragment(fragment, fragmentStack, false);
					}
				}
				/*
				SearchFilmTask retriever = new SearchFilmTask(home);
				retriever.execute(query);
				searchMenuItem.collapseActionView();
				searchView.setQuery("", false);
*/
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

		switch (position) {

		case About.position:
			fragment = new About();
			break;
		case FilmToSeeListFragment.position:
			/*
			 * setFragment( FragFilmList.getInstance().getFragment(
			 * FilmToSeeListFragment.class.getSimpleName() .toString()),
			 * fragmentStack, false);
			 */
			fragment = FragFilmList.getInstance().getFragment(
					FilmToSeeListFragment.class.getSimpleName().toString());
			break;

		case FilmAllListFragment.position:
			/*
			 * setFragment( FragFilmList.getInstance().getFragment(
			 * FilmAllListFragment.class.getSimpleName() .toString()),
			 * fragmentStack, false);
			 */
			fragment = FragFilmList.getInstance().getFragment(
					FilmAllListFragment.class.getSimpleName().toString());
			break;

		case FilmFavouriteListFragment.position:
			/*
			 * setFragment( FragFilmList.getInstance().getFragment(
			 * FilmFavouriteListFragment.class.getSimpleName() .toString()),
			 * fragmentStack, false);
			 */
			fragment = FragFilmList.getInstance().getFragment(
					FilmFavouriteListFragment.class.getSimpleName().toString());
			break;
		}

		if (fragment != null) {
			setFragment(fragment, fragmentStack, false);
		}

		// Highlight the selected item and close the drawer
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
