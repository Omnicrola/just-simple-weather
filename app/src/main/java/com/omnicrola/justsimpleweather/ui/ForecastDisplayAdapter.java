package com.omnicrola.justsimpleweather.ui;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.omnicrola.justsimpleweather.R;
import com.omnicrola.justsimpleweather.data.WeatherForecasts;
import com.omnicrola.justsimpleweather.util.WindConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForecastDisplayAdapter {
    private final SimpleDateFormat simpleDateFormat;
    private Activity activity;

    public ForecastDisplayAdapter(Activity activity) {
        this.activity = activity;
        simpleDateFormat = new SimpleDateFormat("MM/dd hh:mm", Locale.getDefault());
    }

    public void setDisplay(WeatherForecasts forecasts) {
        if (activity.findViewById(R.id.forecast_temp) == null) {
            return;
            // display is not available yet
        }
        Log.i("display-adapter", "Updating forecast text views");

        if (forecasts.getForecasts().size() > 0) {
            WeatherForecasts.WeatherForecast forecast = forecasts.getForecasts().get(0);

            TextView timeView = activity.findViewById(R.id.forecast_time);
            String formattedTime = simpleDateFormat.format(new Date(forecast.getTimeInFuture()));
            String display = String.format(Locale.US, "%s", formattedTime);
            timeView.setText(display);


            TextView tempView = activity.findViewById(R.id.forecast_temp);
            String temperature = String.format(Locale.US, "%.1f - %.1fF", forecast.getMinTemperature(), forecast.getMaxTemperature());
            tempView.setText(temperature);

            TextView windView = activity.findViewById(R.id.forecast_wind);
            float windSpeed = forecast.getWindSpeed();
            String windDirection = WindConverter.directionFromDegrees(forecast.getWindDirection());
            String wind = String.format(Locale.US, "Wind %.0f %s", windSpeed, windDirection);
            windView.setText(wind);

        }

    }
}
