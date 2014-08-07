package com.codebase.locationgpstest;

import com.codebase.locationgpstest.app.LocationApplication;
import com.codebase.locationgpstest.utils.GPSTracker;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
public class SettingsFragment extends Fragment {

	ToggleButton toggleButtonLocationScan;
	private int fragmentNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.settings_main, container,
				false);
		intializeControls(rootView);
		Log.e(LocationConstants.TAG_PROJECT,"onCreateView of Settings fragment");
		return rootView;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void intializeControls(View v) {
		toggleButtonLocationScan = (ToggleButton) v
				.findViewById(R.id.toggle_button_location);
		toggleButtonLocationScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View toggleButton) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Button state is "
						+ ((ToggleButton) toggleButton).isChecked(),
						Toast.LENGTH_SHORT).show();
				
				

			}
		});
	}

	public SettingsFragment() {
		// TODO Auto-generated constructor stub
	}

	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static SettingsFragment newInstance(int sectionNumber) {
		SettingsFragment fragment = new SettingsFragment();
		Bundle args = new Bundle();
		fragment.fragmentNumber = sectionNumber;
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e(LocationConstants.TAG_PROJECT,"onViewCreated");
		System.out.println("Loaded fragment "+this.fragmentNumber);
		super.onViewCreated(view, savedInstanceState);
	
	}
	@Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        
		Log.e(LocationConstants.TAG_PROJECT,"settings fragment is visible "+visible);

        if (visible) {
            GPSTracker tracker = new GPSTracker(LocationApplication.getAppContext());

        }
    }
}
