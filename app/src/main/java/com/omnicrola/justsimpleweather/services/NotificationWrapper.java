package com.omnicrola.justsimpleweather.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.omnicrola.justsimpleweather.R;
import com.omnicrola.justsimpleweather.data.WeatherReport;

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

    public void setDisplay(WeatherReport weatherReport) {
        String displayText = formatDisplayText(weatherReport);
        Notification notification = new Notification.Builder(applicationContext)
                .setContentTitle("Current weather")
                .setContentText(displayText)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        notificationManager.notify(1, notification);
    }

    private String formatDisplayText(WeatherReport weatherReport) {
        float temperature = weatherReport.getTemperature();
        String description = weatherReport.getDescription();
        String formattedTime = simpleDateFormat.format(new Date(weatherReport.getTimestamp()));

        return String.format(Locale.US, "%.1fF %s | updated: %s", temperature, description, formattedTime);
    }
}
