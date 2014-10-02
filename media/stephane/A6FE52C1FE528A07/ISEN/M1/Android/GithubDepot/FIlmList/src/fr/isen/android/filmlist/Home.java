package fr.isen.android.filmlist;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.filmlist.R;

public class Home extends Activity {
	private String[] navigationArray;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		navigationArray = getResources().getStringArray(R.array.navigation_array);
	        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        drawerList = (ListView) findViewById(R.id.left_drawer);

	        // Set the adapter for the list view
	        drawerList.setAdapter(new ArrayAdapter<String>(this,
	                R.layout.drawer_list_item, navigationArray));
	        // Set the list's click listener
	        drawerList.setOnItemClickListener(new DrawerItemClickListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}
	

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
	    // Create a new fragment and specify the planet to show based on position
	    Fragment fragment = new PlaceholderFragment2();
	
	    // Insert the fragment by replacing any existing fragment
	    FragmentManager fragmentManager = getFragmentManager();
	    fragmentManager.beginTransaction()
	                   .replace(R.id.content_frame, fragment)
	                   .commit();
	
	    // Highlight the selected item, update the title, and close the drawer
	    drawerList.setItemChecked(position, true);
	    setTitle(navigationArray[position]);
	    drawerLayout.closeDrawer(drawerList);
	}
	
	public static class PlaceholderFragment2 extends Fragment {

		public PlaceholderFragment2() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_apropos,
					container, false);
			return rootView;
		}
	}

}
