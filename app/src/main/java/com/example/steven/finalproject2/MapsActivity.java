package com.example.steven.finalproject2;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    static String map_store_add;
    static String map_store_name;
    static double map_store_latitude;
    static double map_store_longitude;

    private GoogleMap mMap;
    private Geocoder geoCoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        geoCoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressLocation = null;
        if( ("" + map_store_latitude).equals("") || ("" + map_store_longitude).equals(""))
        {
            Log.d("Map","no_la_lo");
            try {
                addressLocation = geoCoder.getFromLocationName(map_store_add, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            double latitude = addressLocation.get(0).getLatitude();
            double longitude = addressLocation.get(0).getLongitude();
            LatLng store_add = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(store_add).title(map_store_name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude) ,100));
        }
        else {
            LatLng store_add = new LatLng(map_store_latitude, map_store_longitude);
            mMap.addMarker(new MarkerOptions().position(store_add).title(map_store_name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(map_store_latitude, map_store_longitude), 100));
        }
    }
}
