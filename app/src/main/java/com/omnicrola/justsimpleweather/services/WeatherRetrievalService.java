package com.omnicrola.justsimpleweather.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.omnicrola.justsimpleweather.api.openWeather.OpenWeatherWrapper;
import com.omnicrola.justsimpleweather.data.storage.DataStorageService;
import com.omnicrola.justsimpleweather.events.WeatherRefreshRequestEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;

public class WeatherRetrievalService extends Service {

    private static final long ONE_MINUTE = 1000 * 60;
    private static final long FIFTEEN_MINUTES = ONE_MINUTE * 15;
    private static final long REFRESH_THRESHOLD_LIMIT = 1000;

    private Timer serviceTimer;
    private OpenWeatherWrapper weatherApi = new OpenWeatherWrapper();
    private WeatherRetrievalTask weatherRetrievalTask;
    private long lastRefresh = 0;

    @Override
    public void onCreate() {
        DataStorageService dataStorageService = new DataStorageService(getApplicationContext());
        if (serviceTimer != null) {
            serviceTimer.cancel();
        } else {
            serviceTimer = new Timer();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationWrapper notificationWrapper = new NotificationWrapper(getApplicationContext(), notificationManager);
        weatherRetrievalTask = new WeatherRetrievalTask(weatherApi, dataStorageService, notificationWrapper);
        serviceTimer.scheduleAtFixedRate(weatherRetrievalTask, 0, FIFTEEN_MINUTES);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribeEvent(WeatherRefreshRequestEvent refreshRequest) {
        if (System.currentTimeMillis() - lastRefresh > REFRESH_THRESHOLD_LIMIT) {
            weatherRetrievalTask.run();
            lastRefresh = System.currentTimeMillis();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
