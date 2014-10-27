package fr.isen.android.filmlist.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.filmlist.R;

public abstract class FilmListFragment extends Fragment {
	private ArrayList<String> list;
	private ArrayAdapter<String> adapter;
	private ListView listview;
	private HashMap<String, View> itemSelected;

	private Drawable defaultBackground;

	private ActionMode mActionMode;
	private ActionBarCallBack actionBarCallBack;

	public static final String LIST_KEY = "fr.isen.android.filmlist.ui.filmlistfragment.filmlistkey";

	public FilmListFragment() {
		itemSelected = new HashMap<String, View>();
		mActionMode = null;
	}

	public ListView getListView() {
		return listview;
	}

	
	public void setList(ArrayList<String> list_) {
		list = list_;
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setAdapter(ArrayAdapter<String> adapter_) {
		this.adapter = adapter_;
	}

	public void setListview(ListView listview_) {
		listview = listview_;
	}
	
	public HashMap<String, View> getItemSelected(){
		return this.itemSelected;
	}

	public ActionMode getActionMode() {
		return this.mActionMode;
	}

	public Drawable getDefaultBackground(){
		return this.defaultBackground;
	}
	
	public ArrayAdapter<String> getAdapter(){
		return this.adapter;
	}
	
	public ActionBarCallBack getActionBarCallBack(){
		return this.actionBarCallBack;
	}
	
	public abstract int getPosition();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// If activity recreated (such as from screen rotate), restore
		// the previous article selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.

		View view = inflater.inflate(R.layout.fragment_film_list, container,
				false);

		Bundle args = getArguments();

		if (args != null && args.containsKey(FilmListFragment.LIST_KEY)) {
			list = args.getStringArrayList(FilmListFragment.LIST_KEY);
		} else {
			list = new ArrayList<String>();
		}

		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		listview = (ListView) view.findViewById(R.id.listview);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		defaultBackground = listview.getBackground();
		actionBarCallBack = new ActionBarCallBack((Home)getActivity(), this);

		// Check for the rotation screen
		listview.post(new Runnable() {
			@Override
			public void run() {

				// The selection mode was enabled
				if (itemSelected.size() >= 1) {

					// Highlight the item selected
					for (String i : itemSelected.keySet()) {
						listview.getChildAt(Integer.parseInt(i))
								.setBackgroundColor(Color.GRAY);
					}

					// Activate the selection mode
					mActionMode = ((Home) getActivity())
							.startActionMode(actionBarCallBack);

					// Remove the listener to open the details for a movie
					listview.setOnItemClickListener(null);

				}
			}
		});

		setRetainInstance(true);

		return view;
	}

	public static void additemListener(ListView listFl, final String typeKey_, final ArrayList<String> list, final Home activity) {
		if (listFl != null) {
			listFl.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					FilmDetailsFragment filmDetailsFragment = new FilmDetailsDBFragment();
					Bundle args = new Bundle();
					args.putString(FilmDetailsFragment.MOVIE_KEY,
							list.get(position));
					args.putString(FilmDetailsFragment.TYPE_KEY, typeKey_);
					filmDetailsFragment.setArguments(args);
					activity.setFragment(filmDetailsFragment,
							Home.fragmentStack, true);
				}
			});
		}
	}

	public void addItemLongClick(final ListView list, final String typeKey) {
		if (list != null) {
			list.setOnItemLongClickListener(new OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					if (!itemSelected.containsKey(Integer.toString(position))) {
						itemSelected.put(Integer.toString(position), arg1);
						getListView().getChildAt(position).setBackgroundColor(
								Color.GRAY);

						if (itemSelected.size() == 1) {
							mActionMode = ((Home) getActivity())
									.startActionMode(actionBarCallBack);
							getListView().setOnItemClickListener(null);
						}

					} else {
						itemSelected.remove(Integer.toString(position));
						getListView().getChildAt(position).setBackground(
								defaultBackground);
						((Home) getActivity()).setTitle(Integer
								.toString(position));

						if (itemSelected.isEmpty()) {
							itemSelected.clear();
							mActionMode.finish();
							additemListener(listview, typeKey, getList(), (Home)getActivity());

						}
					}
					return true;
				}
			});
		}
	}

	public void refresh(String film) {
		list.add(film);
		adapter.notifyDataSetChanged();
	}
}