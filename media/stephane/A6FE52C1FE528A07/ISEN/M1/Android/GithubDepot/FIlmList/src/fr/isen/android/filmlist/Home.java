package fr.isen.android.filmlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ShareActionProvider;

import com.example.filmlist.R;

public class Home extends Activity {
	private String[] navigationArray;
	private DrawerLayout drawerLayout;
	private ListView drawerList;

	private ActionBarDrawerToggle drawerToggle;
	private CharSequence drawerTitle;
	private CharSequence mTitle;

	private FilmDAO dao;
	private ArrayList<String> list;
	private ArrayAdapter adapter;

	private ShareActionProvider mShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		this.dao = new FilmDAO(this);

		// Initialisation du "tiroir"
		navigationArray = getResources().getStringArray(
				R.array.navigation_array);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		drawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, navigationArray));
		// Set the list's click listener
		drawerList.setOnItemClickListener(new DrawerItemClickListener());

		mTitle = drawerTitle = getTitle();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu();
			}
		};

		// Set the drawer toggle as the DrawerListener
		drawerLayout.setDrawerListener(drawerToggle);

		dao.open();
		final List<Film> films = dao.getAllFilms();
		dao.close();

		final ListView listview = (ListView) findViewById(R.id.listview);

		list = new ArrayList<String>();

		for (Film film : films) {
			list.add(film.getName());
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
		 
			@Override public void onItemClick(AdapterView<?> parent, final View
			 view, int position, long id) { final String item = (String)
			 parent.getItemAtPosition(position);
			 	// Create a new fragment and specify the planet to show based on
				// position
				Fragment fragment = new FilmDetails.PlaceholderFragment();

				// Insert the fragment by replacing any existing fragment
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
				setTitle(item);
			}
		 });

	}

	public void handleSearch(String query) {
		selectItem(2);
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
				R.id.action_search).getActionView();
		final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {

				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				dao.open();
				Film film = dao.insert(query);
				dao.close();
				list.add(film.getName());
				adapter.notifyDataSetChanged();
				searchMenuItem.collapseActionView();
				searchView.setQuery("", false);
				return true;
			}
		});

		// Localisation du menu item avec  ShareActionProvider
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

		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment = new APropos.PlaceholderFragment();

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		drawerList.setItemChecked(position, true);
		setTitle(navigationArray[position]);
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
}
