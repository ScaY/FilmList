package fr.isen.android.filmlist.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.filmlist.R;

public class ActionBarCallBack implements ActionMode.Callback {

	private ListView listview;
	private Drawable defaultBackground;
	private HashMap<String, View> itemSelected;
	private String typeKey;
	private Home activity;
	private ArrayList<String> list;
	
	
	public ActionBarCallBack(ListView listview,
			HashMap<String, View> itemSelected, Drawable background, Home activity, ArrayList<String> list) {
		this.listview = listview;
		this.defaultBackground = background;
		this.itemSelected = itemSelected;
		this.typeKey = FilmAllListFragment.class.getSimpleName().toString(); // To avoid null reference
		this.activity = activity;
		this.list = list;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_share:
			break;
		case R.id.item_delete:
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
		
		for (String i : itemSelected.keySet()) {
			listview.getChildAt(Integer.parseInt(i)).setBackground(defaultBackground);
		}
		itemSelected.clear();
		FilmListFragment.additemListener(listview, typeKey, list, activity);
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub

		mode.setTitle("Movie is selected");
		return false;
	}

}