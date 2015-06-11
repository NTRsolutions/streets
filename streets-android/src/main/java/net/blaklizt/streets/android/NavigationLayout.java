package net.blaklizt.streets.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import net.blaklizt.streets.android.location.navigation.Steps;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tkaviya
 * Date: 6/22/14
 * Time: 12:05 AM
 */
public class NavigationLayout extends Fragment
{
    private static final String TAG = Streets.TAG + "_" + NavigationLayout.class.getSimpleName();
	private static NavigationLayout navigationLayout;
	ExpandableListView navigation_steps;
	TextView nav_location_name;
	TextView nav_location_address;
	TextView nav_location_categories;
	NavigationListAdapter navStepsAdapter;
	ArrayList directions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "+++ ON CREATE VIEW +++");

	    navigationLayout = this;

        // Inflate the layout for this fragment
		    View view = inflater.inflate(R.layout.nav_layout, container, false);
	    navigation_steps = (ExpandableListView)view.findViewById(R.id.nav_location_steps);
	    nav_location_name = (TextView)view.findViewById(R.id.nav_location_name);
	    nav_location_address = (TextView)view.findViewById(R.id.nav_location_address);
	    nav_location_categories = (TextView)view.findViewById(R.id.nav_location_categories);

		nav_location_name.setText("This page will show directions to any location/person you select on the MAP page");

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "+++ ON CREATE +++");
        super.onCreate(savedInstanceState);
    }

	public void setDirections(String placeName, String address, String type, ArrayList<Steps> directions)
	{
		Log.d(TAG, "Setting directions");
		this.directions = directions;

		SparseArray<Group> directionsList = new SparseArray<>();

		nav_location_name.setText(placeName);
		nav_location_address.setText(address);
		nav_location_categories.setText(type);
		String header = "NAVIGATION [Click on item for speech]";

		directionsList.put(0, new Group(header));

		for (Steps step : directions)
		{
			directionsList.get(0).children.add(Html.fromHtml(step.getStepInstructions()).toString());
		}

		navStepsAdapter = new NavigationListAdapter(getActivity(), directionsList);

		navigation_steps.setAdapter(navStepsAdapter);
	}

	public static NavigationLayout getInstance() { return navigationLayout; }
}