package com.example.alfa.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alfa on 5/12/2018.
 */
public class Main {

    @SerializedName("temp")
    @Expose
    private double temp;
    @SerializedName("pressure")
    @Expose
    private double pressure=0;
    @SerializedName("humidity")
    @Expose
    private Integer humidity=0;
    @SerializedName("temp_min")
    @Expose
    private double tempMin=0;
    @SerializedName("temp_max")
    @Expose
    private double tempMax=0;
    @SerializedName("sea_level")
    @Expose
    private double seaLevel;
    @SerializedName("grnd_level")
    @Expose
    private double grndLevel;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(double seaLevel) {
        this.seaLevel = seaLevel;
    }

    public double getGrndLevel() {
        return grndLevel;
    }

    public void setGrndLevel(double grndLevel) {
        this.grndLevel = grndLevel;
    }

}
