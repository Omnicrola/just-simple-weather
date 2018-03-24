package com.omnicrola.justsimpleweather.data.storage;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.api.WeatherUnits;
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
    private static final Type REPORT_LIST_TYPE = new TypeToken<ArrayList<WeatherReport>>() {
    }.getType();
    private static final int MAX_REPORT_HISTORY = 100;
    private static final String LOG_TAG = "file-io";

    private final Context context;
    private final Gson gson;

    public DataStorageService(Context context) {
        this.context = context;
        this.gson = new Gson();
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
        if (fileIsMissing(DATA_STORAGE)) {
            createEmptyFile(DATA_STORAGE);
            return new ArrayList<>();
        }
        try {
            Log.i(LOG_TAG, "Reading weather data from local storage..");
            FileInputStream fileInputStream = context.openFileInput(DATA_STORAGE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            List<WeatherReport> weatherReports = gson.fromJson(bufferedReader, REPORT_LIST_TYPE);
            fileInputStream.close();
            Log.i(LOG_TAG, "Successfully retrieved weather from local storage.");
            if(weatherReports==null){
                return new ArrayList<>();
            }
            return weatherReports;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while reading from weather data file", e);
            return new ArrayList<>();
        }
    }

    public void saveWeather(WeatherReport weatherReport) {
        List<WeatherReport> allWeather = getAllWeather();
        Log.i(LOG_TAG, "Saving weather...");
        try {
            updateWeatherList(weatherReport, allWeather);

            String json = gson.toJson(allWeather);
            FileOutputStream fileOutputStream = context.openFileOutput(DATA_STORAGE, Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
            Log.i(LOG_TAG, "Saved.");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while saving weather data file", e);
        }
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

    private boolean fileIsMissing(String filename) {
        File file = new File(context.getFilesDir(), filename);
        return !file.exists();
    }

    private void createEmptyFile(String filename) {
        File file = new File(context.getFilesDir(), filename);
        try {
            boolean newFile = file.createNewFile();
            if (newFile) {
                Log.i(LOG_TAG, "New data file created.");
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating data file", e);
        }
    }

    public WeatherSettings getSettings() {
        if (fileIsMissing(SETTINGS_STORAGE)) {
            createDefaultSettings();
        }
        WeatherSettings settings = readSettings();
        Log.i(LOG_TAG, "Got settings from storage");
        return settings;
    }

    public void saveSettings(WeatherSettings settings) {
        try {
            String json = gson.toJson(settings);
            FileOutputStream fileOutputStream = context.openFileOutput(SETTINGS_STORAGE, Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
            Log.i(LOG_TAG, "Settings saved");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating settings file", e);
        }
    }

    private void createDefaultSettings() {
        WeatherSettings settings = WeatherSettings.builder()
                .country("us")
                .zipCode("48198")
                .units(WeatherUnits.IMPERIAL)
                .build();
        Log.i(LOG_TAG, "Creating default settings...");
        saveSettings(settings);
    }

    private WeatherSettings readSettings() {
        try {
            FileInputStream fileInputStream = context.openFileInput(SETTINGS_STORAGE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            WeatherSettings weatherReports = gson.fromJson(bufferedReader, WeatherSettings.class);
            fileInputStream.close();
            return weatherReports;
        } catch (IOException e) {
            Log.e("", "Error reading settings", e);
        }
        return null;
    }
}
