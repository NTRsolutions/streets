package net.blaklizt.streets.android;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import net.blaklizt.streets.android.location.navigation.Directions;
import net.blaklizt.streets.android.location.navigation.Navigator;
import net.blaklizt.streets.android.location.places.Place;
import net.blaklizt.streets.android.location.places.PlaceTypes;
import net.blaklizt.streets.android.location.places.PlacesService;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * User: tkaviya
 * Date: 6/21/14
 * Time: 5:58 PM
 */
public class MapLayout extends SherlockFragment implements LocationListener, OnMarkerClickListener, Navigator.OnPathSetListener {

    public static final String TAG = "MapLayout";
    protected static MapLayout mapLayout = null;
    protected GoogleMap googleMap;
    protected Navigator navigator;
    protected Location currentLocation;
    protected HashMap<Marker, Place> map = new HashMap<>();
    protected boolean firstLocationUpdate = true;
    protected ImageView location_image;
    protected TextView location_name_text_view;
    protected TextView location_address_text_view;
    protected TextView location_categories_text_view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.map_layout, container, false);
        // do your view initialization here
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "+++ ON CREATE +++");
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.map_layout);

        mapLayout = this;

        location_image = (ImageView)getView().findViewById(R.id.location_image_view);
        location_name_text_view = (TextView)getView().findViewById(R.id.location_name_text_view);
        location_address_text_view = (TextView)getView().findViewById(R.id.location_address_text_view);
        location_categories_text_view = (TextView)getView().findViewById(R.id.location_categories_text_view);

        try
        {
            //Create global configuration and initialize ImageLoader with this configuration
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getView().getContext()).build();
            ImageLoader.getInstance().init(config);

            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getView().getContext());
            // Showing status
            if (status != ConnectionResult.SUCCESS) // Google Play Services are not available
            {
                Log.i(TAG, "Google Play Services are not available");
                int requestCode = 10;
//                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
//                dialog.show();
            }
            else // Google Play Services are available
            {
                Log.i(TAG, "Google Play Services are available");
                // Getting reference to the SupportMapFragment of activity_main.xml
                SupportMapFragment fm = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

                // Getting GoogleMap object from the fragment
                googleMap = fm.getMap();
                Log.i(TAG, "Got Google map");
                // Enabling MyLocation Layer of Google Map
                googleMap.setMyLocationEnabled(true);

                googleMap.setOnMarkerClickListener(this);

                // Getting LocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getView().getContext().getSystemService(Context.LOCATION_SERVICE);
                Log.i(TAG, "Got LocationManager");
                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Getting the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                //            // Getting Current Location
                //            Location location = locationManager.getLastKnownLocation(provider);
                //
                //             if(location!=null){
                //                //PLACE THE INITIAL MARKER
                //                 drawMarker(location);
                //            }
                locationManager.requestLocationUpdates(provider, 20000, 0, this);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Failed to execute streets on start", ex);
        }

    }

    public static MapLayout getInstance() { return mapLayout; }

    private void drawMarker(Location location){
//        Log.i(TAG, "Found current location at " + location.getLatitude() + " : " + location.getLongitude());
//        googleMap.clear();
//        LatLng currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
//        googleMap.addMarker(new MarkerOptions()
//                .position(currentPosition)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                .title("Your current location"));
    }

    private void drawPlaceMarker(Place place){
        try
        {
            Log.i(TAG, "Drawing place marker for " + place.name + " at location " + place.latitude + " : " + place.longitude);
            LatLng currentPosition = new LatLng(place.latitude, place.longitude);
            map.put(googleMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .snippet(place.formatted_address)
                    .icon(place.icon)
                    .alpha(0.7f)
                    .title(place.name)), place);
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Failed to draw place marker for place " + place.name, ex);
        }
    }

    @Override
    public void onStart() {
        Log.i(TAG, "+++ ON START +++");
        super.onStart();
    }


    @Override
    public void onResume() {
        Log.i(TAG, "+++ ON RESUME +++");
        super.onResume();

        try
        {
            if (currentLocation != null) {
                LinkedList<Place> nearbyPlaces = PlacesService.nearby_search(
                        currentLocation.getLatitude(), currentLocation.getLongitude(), 5000, PlaceTypes.getDefaultPlaces());

                for (Place place : nearbyPlaces) { drawPlaceMarker(place); }
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Failed to resume streets map layout", ex);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "+++ ON LOCATION CHANGED +++");

        try
        {
            currentLocation = location;


            // Getting latitude of the current location
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            Log.i(TAG, "New location " + latitude + ":" + longitude);

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            if (firstLocationUpdate)
            {
                // Showing the current location in Google Map
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                Log.i(TAG, "Camera moved to new location");

                // Zoom in the Google Map
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                Log.i(TAG, "Camera zoomed to view");

                location_image.setImageDrawable(getResources().getDrawable(R.drawable.default_icon));
                location_name_text_view.setText("Current Location");
                location_address_text_view.setText("Latitude: " + latitude);
                location_categories_text_view.setText("Longitude: " + longitude);

                drawMarker(location);

                LinkedList<Place> nearbyPlaces = PlacesService.nearby_search(
                        currentLocation.getLatitude(), currentLocation.getLongitude(), 5000, PlaceTypes.getDefaultPlaces());

                for (Place place : nearbyPlaces) { drawPlaceMarker(place); }

                firstLocationUpdate = false;
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Failed to update to new location", ex);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        try
        {
            Place clickedPlace = map.get(marker);
            if (clickedPlace != null)
            {
                try {
                    if (clickedPlace.image.startsWith("http")) {
                        location_image.setImageBitmap(ImageLoader.getInstance().loadImageSync(clickedPlace.image));
                    } else {
                        location_image.setImageDrawable(getResources().getDrawable(R.drawable.friend));
                    }
                }
                catch (Exception e) { e.printStackTrace(); }
                location_name_text_view.setText(clickedPlace.name);
                location_address_text_view.setText(clickedPlace.formatted_address);
                location_categories_text_view.setText(clickedPlace.type);
            }

            if (navigator != null) {
                for (Polyline polyline : navigator.getPathLines()) {
                    polyline.remove();
                }
            }
            navigator = new Navigator(googleMap,
                new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                new LatLng(clickedPlace.latitude, clickedPlace.longitude));

            navigator.findDirections(false);

            navigator.setOnPathSetListener(this);

            return true;
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Failed to execute marker click event", ex);
            return false;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPathSetListener(Directions directions) {
        //displace route paths
    }
}
