package com.omnicrola.justsimpleweather.ui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.data.WeatherReport;
import com.omnicrola.justsimpleweather.data.storage.DataStorageService;
import com.omnicrola.justsimpleweather.util.Possible;


public class MainActivityMessageHandler {
    private static final String LOG_TAG = "main-msg-handler";
    private Context applicationContext;
    private WeatherDisplayAdapter weatherDisplayAdapter;
    private DataStorageService dataStorageService;

    public MainActivityMessageHandler(Context applicationContext, WeatherDisplayAdapter weatherDisplayAdapter, DataStorageService dataStorageService) {
        this.applicationContext = applicationContext;
        this.weatherDisplayAdapter = weatherDisplayAdapter;
        this.dataStorageService = dataStorageService;
    }

    public void updateWeatherSuccess() {
        Possible<WeatherReport> latestWeather = dataStorageService.getLatestWeather();
        WeatherSettings settings = dataStorageService.getSettings();
        if (latestWeather.isPresent()) {
            WeatherReport weatherReport = latestWeather.get();
            weatherDisplayAdapter.setDisplay(weatherReport, settings);
        } else {
            Log.i(LOG_TAG, "No data retrieved, display not updated");
        }
    }

    public void updateFailed() {
        Toast.makeText(this.applicationContext, "Problem getting weather", Toast.LENGTH_SHORT).show();
    }

}
