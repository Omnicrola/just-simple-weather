package com.omnicrola.justsimpleweather.ui;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.omnicrola.justsimpleweather.R;
import com.omnicrola.justsimpleweather.data.WeatherReport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherDisplayAdapter {
    private final SimpleDateFormat simpleDateFormat;
    private Activity activity;

    public WeatherDisplayAdapter(Activity activity) {
        this.activity = activity;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
    }

    public void setDisplay(WeatherReport weatherReport) {
        if (activity.findViewById(R.id.current_temperature) == null) {
            return;
        }
        Log.i("display-adapter", "Updating weather text views");
        setTemperature(weatherReport);
        setWind(weatherReport);
        setHumidity(weatherReport);
        setTimestamp(weatherReport);
    }

    private void setTimestamp(WeatherReport weatherReport) {
        TextView timestampView = activity.findViewById(R.id.last_update_timestamp);
        String formattedTime = simpleDateFormat.format(new Date(weatherReport.getTimestamp()));
        String display = String.format(Locale.US, "Last updated %s", formattedTime);
        timestampView.setText(display);
    }

    private void setHumidity(WeatherReport weatherReport) {
        TextView humidityView = activity.findViewById(R.id.current_humidity);
        String display = String.format(Locale.US, "Humidity %.1f", weatherReport.getHumidity());
        humidityView.setText(display);
    }

    private void setWind(WeatherReport weatherReport) {
        TextView windView = activity.findViewById(R.id.current_wind);
        float windSpeed = weatherReport.getWindSpeed();
        String windDirection = getWindDirection(weatherReport.getWindDirection());
        String display = String.format(Locale.US, "Wind %.0f %s", windSpeed, windDirection);
        windView.setText(display);
    }

    private String getWindDirection(float windDirection) {
        return String.valueOf(windDirection);
    }

    private void setTemperature(WeatherReport weatherReport) {
        TextView tempView = activity.findViewById(R.id.current_temperature);
        String display = String.format(Locale.US, "%.1fF", weatherReport.getTemperature());
        tempView.setText(display);
    }
}
