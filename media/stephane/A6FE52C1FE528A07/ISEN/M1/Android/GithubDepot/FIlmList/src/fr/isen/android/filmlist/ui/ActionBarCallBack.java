package fr.isen.android.filmlist.ui;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.example.filmlist.R;


public class ActionBarCallBack implements ActionMode.Callback {
	 
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // TODO Auto-generated method stub

        mode.setTitle("Movie is selected");
        return false;
    }

}