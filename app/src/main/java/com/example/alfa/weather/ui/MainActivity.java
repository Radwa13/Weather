package com.example.alfa.weather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alfa.weather.WeatherAdapter;
import com.example.alfa.weather.WeatherInterface;
import com.example.alfa.weather.data.DBHelper;
import com.example.alfa.weather.*;
import com.example.alfa.weather.R;
import com.example.alfa.weather.NetworkUtiles.RetrofitClient;
import com.example.alfa.weather.model.Example;

import java.util.ArrayList;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements WeatherAdapter.ListItemClickListner {
    //    @BindView(R.id.recyler)
    RecyclerView recyclerView;
    TextView noCity;

    static ArrayList<Example> data;
    static ArrayList<Example> data2;

    WeatherAdapter weatherAdapter;
    WeatherInterface mInterface;
    int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        weatherAdapter = new WeatherAdapter(this);
        DBHelper dbHelper = new DBHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyler);
        noCity=(TextView)findViewById(R.id.noCity);
        data = new ArrayList<Example>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (dbHelper.numberOfRows() > 0) {
            length = dbHelper.numberOfRows();
            data2=dbHelper.getAllWeather();
            getData(dbHelper.numberOfRows());
        }
        else {
            noCity.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }




    private void getData(int j) {
        for (int i = 0; i < j; i++) {
            loadBakingData(data2.get(i).getCoord().getLat(), data2.get(i).getCoord().getLon());
        }

       weatherAdapter.loadData(data);
        recyclerView.setAdapter(weatherAdapter);
    }


    private void loadBakingData(double lat, double lon) {
        mInterface = RetrofitClient.getClient().create(WeatherInterface.class);
        mInterface.getWeather(lat, lon, BuildConfig.API_KEY).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(this::handleResponse, this::handleError);

    }

    private void handleResponse(Example list) {

        data.add(list);

        weatherAdapter.loadData(data);
        recyclerView.setAdapter(weatherAdapter);
    }

    private void handleError(Throwable error) {

        Toast.makeText(this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public static void addCity(Example example) {
        data.add(example);

    }

    @Override
    protected void onResume() {
        super.onResume();
        weatherAdapter.loadData(data);
        recyclerView.setAdapter(weatherAdapter);
        if(data.size()>0){
            noCity.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(int position) {
        data.remove(position);
        weatherAdapter.loadData(data);
        recyclerView.setAdapter(weatherAdapter);
    }
}
