package com.omnicrola.justsimpleweather.ui;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
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
        simpleDateFormat = new SimpleDateFormat("EEE dd ha", Locale.getDefault());
    }

    public void setDisplay(WeatherForecasts forecasts) {
        TableLayout tableLayout = activity.findViewById(R.id.forecast_table);
        if (tableLayout == null) {
            return;
            // display is not available yet
        }
        Log.i("display-adapter", "Updating forecast text views");

        if (forecasts.getForecasts().size() > 0) {
            for (WeatherForecasts.WeatherForecast weatherForecast : forecasts.getForecasts()) {
                TableRow newRow = createNewRow(weatherForecast);
                tableLayout.addView(newRow);
            }
        }

    }

    private TableRow createNewRow(WeatherForecasts.WeatherForecast forecast) {
        TableRow newRow = (TableRow) LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.forecast_table_row, null);
        TextView timeView = newRow.findViewById(R.id.forecast_time);
        String formattedTime = simpleDateFormat.format(new Date(forecast.getTimeInFuture()));
        String display = String.format(Locale.US, "%s", formattedTime);
        timeView.setText(display);


        TextView tempView = newRow.findViewById(R.id.forecast_temp);
        String temperature = String.format(Locale.US, "%.0fF - %.0fF", forecast.getMinTemperature(), forecast.getMaxTemperature());
        tempView.setText(temperature);

        TextView windView = newRow.findViewById(R.id.forecast_wind);
        float windSpeed = forecast.getWindSpeed();
        String windDirection = WindConverter.directionFromDegrees(forecast.getWindDirection());
        String wind = String.format(Locale.US, "%.0fmph %s", windSpeed, windDirection);
        windView.setText(wind);

        return newRow;
    }
}
