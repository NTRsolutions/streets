package net.blaklizt.streets.android.activity.helpers;

import android.support.design.widget.Snackbar;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import net.blaklizt.streets.android.activity.AppContext;
import net.blaklizt.streets.android.activity.MapLayout;
import net.blaklizt.streets.android.activity.MenuLayout;
import net.blaklizt.streets.android.common.StreetsCommon;
import net.blaklizt.streets.android.location.places.Place;
import net.blaklizt.streets.android.location.places.PlaceTypes;
import net.blaklizt.streets.android.location.places.PlacesService;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Arrays.asList;
import static net.blaklizt.streets.android.common.StreetsCommon.showSnackBar;
import static net.blaklizt.streets.android.common.enumeration.TASK_TYPE.BG_PLACES_TASK;

/******************************************************************************
 * *
 * Created:     29 / 10 / 2015                                             *
 * Platform:    Red Hat Linux 9                                            *
 * Author:      Tich de Blak (Tsungai Kaviya)                              *
 * Copyright:   Blaklizt Entertainment                                     *
 * Website:     http://www.blaklizt.net                                    *
 * Contact:     blaklizt@gmail.com                                         *
 * *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License as published by    *
 * the Free Software Foundation; either version 2 of the License, or       *
 * (at your option) any later version.                                     *
 * *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.    See the         *
 * GNU General Public License for more details.                            *
 * *
 ******************************************************************************/
public class PlacesTask extends StreetsAbstractTask {

    private static final String TAG = StreetsCommon.getTag(PlacesTask.class);

    public PlacesTask() {
        processDependencies = new ArrayList<>(asList(GoogleMapTask.class.getSimpleName(), LocationUpdateTask.class.getSimpleName()));
        viewDependencies = new ArrayList<>(Collections.singletonList(MapLayout.class));
        allowOnlyOnce = false;
        allowMultiInstance = false;
        taskType = BG_PLACES_TASK;
    }

    public static void displayPlaces() {
        Log.i(TAG, "Places task completed with nearbyPlaces = " +
                (AppContext.getAppContextInstance().getNearbyPlaces() != null ? AppContext.getAppContextInstance().getNearbyPlaces().size() : 0));
        if (AppContext.getAppContextInstance().getNearbyPlaces() != null) {
            Log.i(TAG, "Processing nearby places");
            if (AppContext.getAppContextInstance().getGoogleMap() != null) {
                AppContext.getAppContextInstance().getGoogleMap().clear();
                for (Place place : AppContext.getAppContextInstance().getNearbyPlaces()) {
                    drawPlaceMarker(place);
                }
            }
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Log.i(TAG, "+++ doInBackground +++");

        if (AppContext.getAppContextInstance().getCurrentLocation() != null) {
            AppContext.getAppContextInstance().getMarkerPlaces().clear();
            AppContext.getAppContextInstance().setNearbyPlaces(PlacesService.nearby_search(
                    AppContext.getAppContextInstance().getCurrentLocation().getLatitude(), AppContext.getAppContextInstance().getCurrentLocation().getLongitude(),
                    5000, PlaceTypes.getDefaultPlaces()));
        }
        else {
            showSnackBar(MenuLayout.getInstance(), TAG, "Current location unknown. Check location settings.", Snackbar.LENGTH_SHORT);
        }
        return null;
    }

    @Override
    protected void onPostExecuteRelay(Object result) {
        displayPlaces();
    }

    public static void drawPlaceMarker(Place place){
        Log.i(TAG, "Drawing place marker for " + place.name + " at location " + place.latitude + " : " + place.longitude);
        if (AppContext.getAppContextInstance().getGoogleMap() != null) {
            LatLng currentPosition = new LatLng(place.latitude, place.longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentPosition);
            markerOptions.snippet(place.formatted_address);
            markerOptions.icon(place.icon);
            markerOptions.alpha(0.7f);
            markerOptions.title(place.name);
            Marker placeMarker = AppContext.getAppContextInstance().getGoogleMap().addMarker(markerOptions);
            AppContext.getAppContextInstance().getMarkerPlaces().put(placeMarker.getId(), place);
        } else {
            Log.i(TAG, "Map not ready for ");
        }
    }
}
