package com.omnicrola.justsimpleweather.events;


public class WeatherUpdateFailEvent {
    private static final WeatherUpdateFailEvent instance = new WeatherUpdateFailEvent();

    public static WeatherUpdateFailEvent instance() {
        return instance;
    }

    private WeatherUpdateFailEvent() {
    }
}
