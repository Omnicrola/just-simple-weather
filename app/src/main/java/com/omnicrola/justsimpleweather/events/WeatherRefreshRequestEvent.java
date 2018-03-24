package com.omnicrola.justsimpleweather.events;

public class WeatherRefreshRequestEvent {
    private static final WeatherRefreshRequestEvent instance = new WeatherRefreshRequestEvent();

    public static WeatherRefreshRequestEvent instance() {
        return instance;
    }

    private WeatherRefreshRequestEvent() {
    }
}
