package com.omnicrola.justsimpleweather.api.openWeather;


import java.util.List;

import lombok.Getter;

@Getter
public class OpenWeatherData {
    private OpenWeatherMain main;
    private OpenWeatherWind wind;
    private List<OpenWeatherWeather> weather;

    @Getter
    public static class OpenWeatherWeather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    @Getter
    public static class OpenWeatherMain {
        private float temp;
        private float pressure;
        private float humidity;

    }

    @Getter
    public static class OpenWeatherWind {
        private float speed;
        private float deg;
    }
}
