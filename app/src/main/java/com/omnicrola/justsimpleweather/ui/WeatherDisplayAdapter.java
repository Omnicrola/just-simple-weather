package com.omnicrola.justsimpleweather.ui;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.omnicrola.justsimpleweather.R;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.api.WeatherUnits;
import com.omnicrola.justsimpleweather.data.WeatherReport;
import com.omnicrola.justsimpleweather.util.UnitConversion;
import com.omnicrola.justsimpleweather.util.WindConverter;

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

    public void setDisplay(WeatherReport weatherReport, WeatherSettings settings) {
        if (activity.findViewById(R.id.current_temperature) == null) {
            return;
        }
        Log.i("display-adapter", "Updating weather text views");
        setTemperature(weatherReport, settings);
        setWind(weatherReport, settings);
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

    private void setWind(WeatherReport weatherReport, WeatherSettings settings) {
        TextView windView = activity.findViewById(R.id.current_wind);
        String windDirection = WindConverter.directionFromDegrees(weatherReport.getWindDirection());
        WeatherUnits units = settings.getUnits();
        String windUnit = units.getWindUnit();
        float windSpeed = UnitConversion.speed(units, weatherReport.getWindSpeed());
        String display = String.format(Locale.US, "Wind %.0f%s %s", windSpeed, windUnit, windDirection);
        windView.setText(display);
    }

    private void setTemperature(WeatherReport weatherReport, WeatherSettings settings) {
        TextView tempView = activity.findViewById(R.id.current_temperature);
        WeatherUnits units = settings.getUnits();
        String tempUnit = units.getTemperatureLetter();
        float temperature = UnitConversion.temperature(units, weatherReport.getTemperature());

        String display = String.format(Locale.US, "%.1f%s", temperature, tempUnit);
        tempView.setText(display);
    }
}
