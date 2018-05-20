package com.example.alfa.weather.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alfa.weather.WeatherInterface;
import com.example.alfa.weather.data.DBHelper;
import com.example.alfa.weather.*;
import com.example.alfa.weather.R;
import com.example.alfa.weather.NetworkUtiles.RetrofitClient;
import com.example.alfa.weather.model.Example;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    @BindView(R.id.number)
    protected TextView mTextView;
    private GoogleMap mMap;
    WeatherInterface mInterface;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        dbHelper = new DBHelper(this);
        ButterKnife.bind(this,this);

        setSavedCities();
        mapFragment.getMapAsync(this);
    }

    private void loadBakingData(double lat, double lon) {
        mInterface = RetrofitClient.getClient().create(WeatherInterface.class);
        mInterface.getWeather(lat, lon, BuildConfig.API_KEY).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(this::handleResponse, this::handleError);

    }

    private void handleResponse(Example list) {

        String name = list.getName();
        double lon = list.getCoord().getLon();
        double lat = list.getCoord().getLat();
        int id = list.getId();
        if (dbHelper.getData(list.getId()).getCount() == 0) {
            dbHelper.insertWeather(id, name, lon, lat);
            setSavedCities();
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
            MainActivity.addCity(list);//        SQLiteDatabase sqLiteDatabase;

        } else {
            Toast.makeText(this, "City already exists", Toast.LENGTH_LONG).show();
        }


    }


    private void handleError(Throwable error) {

        Toast.makeText(this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }
    private void setSavedCities(){
        String savedCities = getString(R.string.saved_cities) + dbHelper.numberOfRows();
        mTextView.setText(savedCities);
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
        // Add a marker in Sydney and move the camera
        LatLng alexandria = new LatLng(31.205753, 29.924526);
        float zoomLevel = (float) 7.0;


        DBHelper dbHelper = new DBHelper(this);

        if (dbHelper.numberOfRows() > 0) {
            for (int i = 0; i < dbHelper.numberOfRows(); i++)
                mMap.addMarker(new MarkerOptions().position(new LatLng(dbHelper.getAllWeather().get(i).getCoord().getLat(), dbHelper.getAllWeather().get(i).getCoord().getLon())));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(alexandria, zoomLevel));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double lat = latLng.latitude;
                double lon = latLng.longitude;
                loadBakingData(lat, lon);

            }
        });
    }
}
