package fr.isen.android.filmlist.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.FavouriteFilmsDAO;
import fr.isen.android.filmlist.bdd.Film;
import fr.isen.android.filmlist.bdd.FilmDAO;
import fr.isen.android.filmlist.bdd.FilmsListDAO;
import fr.isen.android.filmlist.bdd.ToSeeFilmsDAO;
import fr.isen.android.filmlist.utils.SearchFilmTask;

public class HomeActivity extends FragmentActivity {

	public static final String LIST_KEY = "keyHomeActivity";
	public static final String STACK_FILMLIST = "filmListStack";
	public static final String STACK_SEARCHRESULT = "searchResultStack";

	private String[] navigationArray;
	private DrawerLayout drawerLayout;
	private ListView drawerList;

	private ActionBarDrawerToggle drawerToggle;
	private CharSequence drawerTitle;
	private CharSequence mTitle;

	private Fragment fragment;
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
			public void onItemClick(AdapterView<?> parent, View view,
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

		// Case where there is a screen rotation
		if (savedInstanceState != null) {

			// Retrieve the previous fragment
			fragment = getFragmentManager().findFragmentByTag(STACK_FILMLIST);

			// Case where the fragment is the detail about a film
			if (fragment instanceof FilmDetailsFragment) {
				FilmDetailsFragment fd = (FilmDetailsFragment) fragment;

				// Check where the fragment has been lunched (From FilmFavorite
				// or FilmToSee)
				if (fd.getType() != "" && fd.getType() != null) {
					if (fd.getType().equals(
							FilmToSeeListFragment.class.getSimpleName()
									.toString())) {
						// Initialize the stack for the filmToSee
						// setFragment(fgl.getFragment(FilmToSeeListFragment.class
						// .getSimpleName().toString()), fragmentStack,
						// true);

						setFragment(
								createFilmListFragment(toSeeDAO,
										new FilmToSeeListFragment()),
								STACK_FILMLIST, true);
					} else if (fd.getType().equals(
							FilmFavouriteListFragment.class.getSimpleName()
									.toString())) {
						// Initialize the stack for the movies favorite
						// setFragment(
						// fgl.getFragment(FilmFavouriteListFragment.class
						// .getSimpleName().toString()),
						// fragmentStack, true);
						setFragment(
								createFilmListFragment(favouriteDAO,
										new FilmFavouriteListFragment()),
								STACK_FILMLIST, true);
					} else if (fd.getType().equals(
							FilmAllListFragment.class.getSimpleName()
									.toString())) {
						// Initialize the stack for the list of all the film
						// setFragment(fgl.getFragment(FilmAllListFragment.class
						// .getSimpleName().toString()), fragmentStack,
						// true);
						setFragment(
								createFilmListFragment(filmDAO,
										new FilmAllListFragment()),
								STACK_FILMLIST, true);
					} else if (fd.getType().equals(
							SearchResultsFragment.class.getSimpleName()
									.toString())) {
						// Initialize the stack for the results of the search
						// setFragment(fgl.getFragment(SearchResultsFragment.class
						// .getSimpleName().toString()), FRAGMENTSTACK,
						// true);
						// getFragmentManager().popBackStack();
						// Fragment previous = getFragmentManager()
						// .findFragmentByTag(STACK_FILMLIST);
						SearchResultsFragment fragmentPrevious = GetSearchResult
								.getInstance().getSearchResultFragment();
						if (fragmentPrevious != null) {
							setFragment(fragmentPrevious, STACK_FILMLIST, true);
						}
					}
					setFragment(fragment, STACK_FILMLIST, true);
				} // End if test fd.getType()

			} else if (fragment instanceof FilmListFragment) {
				setFragment(fragment, STACK_FILMLIST, false);
			}
		} else {
			// Case where it is the first time that the application is
			// initialized
			// setFragment(fgl.getFragment(FilmToSeeListFragment.class
			// .getSimpleName().toString()), fragmentStack, false);
			setFragment(
					createFilmListFragment(toSeeDAO,
							new FilmToSeeListFragment()), STACK_FILMLIST, false);

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

		// Set the listener for the search action
		final SearchView searchView = (SearchView) menu.findItem(
				R.id.action_new).getActionView();
		final MenuItem searchMenuItem = menu.findItem(R.id.action_new);
		final HomeActivity home = this;

		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo netInfo = cm.getActiveNetworkInfo();
				if (netInfo != null && netInfo.isConnectedOrConnecting()) {
					SearchFilmTask retriever = new SearchFilmTask(home);
					retriever.execute(query);
				} else {
					setFragment(new NoInternetFragment(), STACK_FILMLIST, false);
				}
				searchMenuItem.collapseActionView();
				searchView.setQuery("", false);
				return true;
			}
		});
		return true;
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
		case FilmToSeeListFragment.POSTION:
			fragment = createFilmListFragment(toSeeDAO,
					new FilmToSeeListFragment());
			break;

		case FilmAllListFragment.POSITION:
			fragment = createFilmListFragment(filmDAO,
					new FilmAllListFragment());
			break;

		case FilmFavouriteListFragment.POSITION:
			fragment = createFilmListFragment(favouriteDAO,
					new FilmFavouriteListFragment());
			break;
		}

		if (fragment != null) {
			setFragment(fragment, STACK_FILMLIST, false);
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

	private FilmListFragment createFilmListFragment(FilmsListDAO daoBase,
			FilmListFragment fragment) {

		daoBase.open();
		List<Film> films = daoBase.getAllFilms();
		daoBase.close();

		Bundle args = null;

		ArrayList<Film> list = new ArrayList<Film>();

		for (Film film : films) {
			list.add(film);
		}

		args = new Bundle();
		args.putSerializable(FilmListFragment.LIST_KEY, list);
		fragment.setArguments(args);
		fragment.setList(list);

		return fragment;
	}
}
