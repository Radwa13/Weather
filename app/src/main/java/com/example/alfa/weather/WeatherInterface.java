package com.example.alfa.weather;

import com.example.alfa.weather.model.Example;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Alfa on 5/10/2018.
 */

public interface WeatherInterface {

    @GET("/data/2.5/weather?")
    Observable<Example>getWeather(@Query("lat")double lat, @Query("lon")double lon, @Query("appid")String api);

}
