package fr.isen.android.filmlist.ui;

import com.example.filmlist.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoInternetFragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.no_internet, container, false);
    	
    	return view;
    }
}
