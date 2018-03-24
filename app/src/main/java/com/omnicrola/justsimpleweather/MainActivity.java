package com.omnicrola.justsimpleweather;

import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.omnicrola.justsimpleweather.data.WeatherReport;
import com.omnicrola.justsimpleweather.data.storage.DataStorageService;
import com.omnicrola.justsimpleweather.events.WeatherRefreshRequestEvent;
import com.omnicrola.justsimpleweather.events.WeatherUpdateFailEvent;
import com.omnicrola.justsimpleweather.events.WeatherUpdateSuccessEvent;
import com.omnicrola.justsimpleweather.services.NotificationWrapper;
import com.omnicrola.justsimpleweather.services.WeatherRetrievalService;
import com.omnicrola.justsimpleweather.ui.MainActivityMessageHandler;
import com.omnicrola.justsimpleweather.ui.WeatherDisplayAdapter;
import com.omnicrola.justsimpleweather.util.Possible;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements WeatherFragment.OnFragmentInteractionListener {

    private static final String LOG_TAG = "main-activity";
    private MainActivityMessageHandler messageHandler;
    private NotificationWrapper notificationWrapper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_future:
                    return true;
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        }
    };
    private DataStorageService dataStorageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "Main activity start!");

        NotificationManager systemService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationWrapper = new NotificationWrapper(getApplicationContext(), systemService);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        WeatherDisplayAdapter weatherDisplayAdapter = new WeatherDisplayAdapter(this);
        dataStorageService = new DataStorageService(getApplicationContext());
        messageHandler = new MainActivityMessageHandler(getApplicationContext(), weatherDisplayAdapter, dataStorageService);

        transitionToCurrentWeather();
        startService(new Intent(this, WeatherRetrievalService.class));
    }

    public void onRefreshButton(View view) {
        EventBus.getDefault().post(WeatherRefreshRequestEvent.instance());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeatherUpdateSuccessEvent event) {
        Log.i(LOG_TAG, "WeatherUpdateSuccessEvent");
        messageHandler.updateWeatherSuccess();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeatherUpdateFailEvent event) {
        Log.i(LOG_TAG, "WeatherUpdateFailEvent");
        messageHandler.updateFailed();
    }

    private void transitionToCurrentWeather() {
        Log.i(LOG_TAG, "Transitioning to current weather fragment");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        WeatherFragment weatherFragment = WeatherFragment.newInstance();
        fragmentTransaction.replace(R.id.main_container, weatherFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
