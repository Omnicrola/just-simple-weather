package com.omnicrola.justsimpleweather.events;


public class WeatherUpdateSuccessEvent {
    private static final WeatherUpdateSuccessEvent instance = new WeatherUpdateSuccessEvent();

    public static WeatherUpdateSuccessEvent instance() {
        return instance;
    }

    private WeatherUpdateSuccessEvent() {
    }
}
