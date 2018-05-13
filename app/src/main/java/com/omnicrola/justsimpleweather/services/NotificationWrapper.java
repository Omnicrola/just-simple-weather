package com.omnicrola.justsimpleweather.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.omnicrola.justsimpleweather.R;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.api.WeatherUnits;
import com.omnicrola.justsimpleweather.data.WeatherReport;
import com.omnicrola.justsimpleweather.util.UnitConversion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationWrapper {
    private final Context applicationContext;
    private final NotificationManager notificationManager;
    private final SimpleDateFormat simpleDateFormat;

    public NotificationWrapper(Context applicationContext, NotificationManager systemService) {
        this.applicationContext = applicationContext;
        this.notificationManager = systemService;
        simpleDateFormat = new SimpleDateFormat("MM/dd hh:mm", Locale.getDefault());
    }

    public void setDisplay(WeatherSettings settings, WeatherReport weatherReport) {
        String displayText = formatDisplayText(settings, weatherReport);
        Notification notification = new Notification.Builder(applicationContext)
                .setContentTitle("Current weather")
                .setContentText(displayText)
                .setSmallIcon(R.drawable.weather_icon_sun)
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        notificationManager.notify(1, notification);
    }

    private String formatDisplayText(WeatherSettings settings, WeatherReport weatherReport) {
        WeatherUnits units = settings.getUnits();
        float temperature = UnitConversion.temperature(units, weatherReport.getTemperature());
        String description = weatherReport.getDescription();
        String formattedTime = simpleDateFormat.format(new Date(weatherReport.getTimestamp()));
        String letter = units.getTemperatureLetter();

        return String.format(Locale.US, "%.1f%s %s | updated: %s", temperature, letter, description, formattedTime);
    }
}
