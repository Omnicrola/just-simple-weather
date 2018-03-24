package com.omnicrola.justsimpleweather.services;

import android.util.Log;

import com.omnicrola.justsimpleweather.api.ResultHandler;
import com.omnicrola.justsimpleweather.api.WeatherApi;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.data.WeatherReport;
import com.omnicrola.justsimpleweather.data.storage.DataStorageService;
import com.omnicrola.justsimpleweather.events.WeatherUpdateFailEvent;
import com.omnicrola.justsimpleweather.events.WeatherUpdateSuccessEvent;
import com.omnicrola.justsimpleweather.util.Possible;

import org.greenrobot.eventbus.EventBus;

import java.util.TimerTask;

class WeatherRetrievalTask extends TimerTask {
    private WeatherApi weatherApi;
    private DataStorageService dataStorageService;
    private NotificationWrapper notificationWrapper;

    public WeatherRetrievalTask(WeatherApi weatherApi,
                                DataStorageService dataStorageService,
                                NotificationWrapper notificationWrapper) {
        this.weatherApi = weatherApi;
        this.dataStorageService = dataStorageService;
        this.notificationWrapper = notificationWrapper;
    }

    @Override
    public void run() {
        Log.i("get-weather-task", "Running weather retrieve task");
        WeatherSettings settings = dataStorageService.getSettings();
        weatherApi.getCurrentWeather(settings, new ResultHandler<WeatherReport>() {
            @Override
            public void handle(WeatherReport weatherReport) {
                dataStorageService.saveWeather(weatherReport);
                Log.i("get-weather-task", "sending message : SUCCESS");
                EventBus.getDefault().post(WeatherUpdateSuccessEvent.instance());
                notificationWrapper.setDisplay(weatherReport);
            }

            @Override
            public void reject(String reason) {
                Log.i("get-weather-task", "sending message : FAIL");
                EventBus.getDefault().post(WeatherUpdateFailEvent.instance());
            }
        });
    }
}
