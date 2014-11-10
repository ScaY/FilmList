package fr.isen.android.filmlist.ui;

import java.io.Serializable;
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
import android.widget.ListView;

import com.example.filmlist.R;

import fr.isen.android.filmlist.bdd.Film;

public abstract class FilmListFragment extends Fragment {
	//private ArrayList<String> list;
	//private ArrayAdapter<String> adapter;
	private ArrayList<Film> list;
	private ListView listview;
	private HashMap<String, View> itemSelected;
	private CustomListAdapter adapter;
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

	public void setList(ArrayList<Film> list_) {
		list = list_;
	}

	public ArrayList<Film> getList() {
		return list;
	}

	public void setAdapter(CustomListAdapter adapter_) {
		this.adapter = adapter_;
	}

	public void setListview(ListView listview_) {
		listview = listview_;
	}

	public HashMap<String, View> getItemSelected() {
		return this.itemSelected;
	}

	public ActionMode getActionMode() {
		return this.mActionMode;
	}

	public Drawable getDefaultBackground() {
		return this.defaultBackground;
	}

	public CustomListAdapter getAdapter() {
		return this.adapter;
	}

	public ActionBarCallBack getActionBarCallBack() {
		return this.actionBarCallBack;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// If activity recreated (such as from screen rotate), restore
		// the previous article selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.

		View view = inflater.inflate(R.layout.fragment_film_list, container,
				false);

		Bundle args = getArguments();

		if (list == null) {
			if (args != null && args.containsKey(FilmListFragment.LIST_KEY)) {
				Serializable arg = args.getSerializable(FilmListFragment.LIST_KEY);;
				if(arg instanceof ArrayList<?>){
					list = (ArrayList<Film>) arg;
				}else{
					list = new ArrayList<Film>();
				}
			} else {
				list = new ArrayList<Film>();
			}
		}
		
		adapter = new CustomListAdapter(getActivity(), list);
		listview = (ListView) view.findViewById(R.id.listview);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		defaultBackground = listview.getBackground();
		actionBarCallBack = new ActionBarCallBack((HomeActivity) getActivity(), this);

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
					mActionMode = ((HomeActivity) getActivity())
							.startActionMode(actionBarCallBack);

					// Remove the listener to open the details for a movie
					listview.setOnItemClickListener(null);

				}
			}
		});

		setRetainInstance(true);

		return view;
	}

	public static void additemListener(ListView listFl, final String typeKey_,
			final ArrayList<Film> list, final HomeActivity activity) {
		if (listFl != null) {
			listFl.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					FilmDetailsFragment filmDetailsFragment = new FilmDetailsFragment();
					Bundle args = new Bundle();
					args.putSerializable(FilmDetailsFragment.MOVIE_KEY, (Serializable) list.get(position));
					args.putString(FilmDetailsFragment.TYPE_KEY,
							SearchResultsFragment.class.getSimpleName().toString());
					filmDetailsFragment.setArguments(args);
					activity.setFragment(filmDetailsFragment,
							HomeActivity.STACK_FILMLIST, true);
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
							mActionMode = ((HomeActivity) getActivity())
									.startActionMode(actionBarCallBack);
							getListView().setOnItemClickListener(null);
						}

					} else {
						itemSelected.remove(Integer.toString(position));
						getListView().getChildAt(position).setBackground(
								defaultBackground);
						((HomeActivity) getActivity()).setTitle(Integer
								.toString(position));

						if (itemSelected.isEmpty()) {
							itemSelected.clear();
							mActionMode.finish();
							additemListener(listview, typeKey, getList(),
									(HomeActivity) getActivity());

						}
					}
					return true;
				}
			});
		}
	}

	public void refresh(Film film) {
		list.add(film);
		adapter.notifyDataSetChanged();
	}
}