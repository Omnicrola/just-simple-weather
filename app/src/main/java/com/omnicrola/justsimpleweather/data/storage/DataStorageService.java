package com.omnicrola.justsimpleweather.data.storage;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.api.WeatherUnits;
import com.omnicrola.justsimpleweather.data.WeatherForecasts;
import com.omnicrola.justsimpleweather.data.WeatherReport;
import com.omnicrola.justsimpleweather.data.WeatherReportTimeComparator;
import com.omnicrola.justsimpleweather.util.Possible;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataStorageService {
    private static final String DATA_STORAGE = "weather-data.json";
    private static final String SETTINGS_STORAGE = "weather-settings.json";
    private static final String FORECAST_STORAGE = "weather-forecast.json";

    private static final Type REPORT_LIST_TYPE = new TypeToken<ArrayList<WeatherReport>>() {
    }.getType();
    private static final int MAX_REPORT_HISTORY = 100;
    private static final String LOG_TAG = "file-io";

    private final DataStorageManager storageManager;

    public DataStorageService(Context context) {
        this.storageManager = new DataStorageManager(context);
    }

    public Possible<WeatherReport> getLatestWeather() {
        Log.i(LOG_TAG, "Retrieving latest weather report...");
        List<WeatherReport> weatherReport = getAllWeather();
        if (weatherReport.size() > 0) {
            Log.i(LOG_TAG, "Got last report.");
            return Possible.of(weatherReport.get(weatherReport.size() - 1));
        } else {
            Log.i(LOG_TAG, "No weather reports available.");
            return Possible.empty();
        }
    }

    private List<WeatherReport> getAllWeather() {
        if (storageManager.fileIsMissing(DATA_STORAGE)) {
            storageManager.createEmptyFile(DATA_STORAGE);
            return new ArrayList<>();
        }
        Possible<List<WeatherReport>> weatherReports = storageManager.read(DATA_STORAGE, REPORT_LIST_TYPE);
        if(weatherReports.isPresent()){
            return weatherReports.get();
        } else {
            return new ArrayList<>();
        }
    }

    public void saveWeather(WeatherReport weatherReport) {
        List<WeatherReport> allWeather = getAllWeather();
        Log.i(LOG_TAG, "Saving weather...");
        updateWeatherList(weatherReport, allWeather);
        storageManager.write(DATA_STORAGE, allWeather);
    }


    public WeatherSettings getSettings() {
        if (storageManager.fileIsMissing(SETTINGS_STORAGE)) {
            createDefaultSettings();
        }
        WeatherSettings settings = storageManager.read(SETTINGS_STORAGE, WeatherSettings.class);
        Log.i(LOG_TAG, "Got settings from storage");
        return settings;
    }

    public void saveSettings(WeatherSettings settings) {
        storageManager.write(SETTINGS_STORAGE, settings);
    }

    public void saveForecast(WeatherForecasts forecast) {
        storageManager.write(FORECAST_STORAGE, forecast);
    }

    public Possible<WeatherForecasts> getForecast() {
        if (storageManager.fileIsMissing(FORECAST_STORAGE)) {
            return Possible.empty();
        }
        WeatherForecasts forecasts = storageManager.read(FORECAST_STORAGE, WeatherForecasts.class);
        return Possible.of(forecasts);
    }

    private void createDefaultSettings() {
        WeatherSettings settings = WeatherSettings.builder()
                .country("us")
                .city("48198")
                .units(WeatherUnits.IMPERIAL)
                .build();
        Log.i(LOG_TAG, "Creating default settings...");
        saveSettings(settings);
    }


    private List<WeatherReport> updateWeatherList(WeatherReport weatherReport, List<WeatherReport> allWeather) {
        allWeather.add(weatherReport);
        Collections.sort(allWeather, new WeatherReportTimeComparator());
        int count = allWeather.size();
        if (count > MAX_REPORT_HISTORY) {
            return allWeather.subList(count - 1 - MAX_REPORT_HISTORY, count - 1);
        } else {
            return allWeather;
        }
    }
}
