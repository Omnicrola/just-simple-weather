package com.omnicrola.justsimpleweather.api.openWeather;

import java.util.List;

import lombok.Getter;

@Getter
public class OpenWeatherForecastData {
    private List<OpenWeatherForecastListItem> list;

    @Getter
    public static class OpenWeatherForecastListItem {
        private long dt;
        private OpenWeatherForecastMain main;
        private List<OpenWeatherForecastWeather> weather;
        private OpenWeatherForecastWind wind;

    }

    @Getter
    public static class OpenWeatherForecastMain {
        private float temp;
        private float temp_min;
        private float temp_max;
        private float grnd_level;
        private float humidity;

    }

    @Getter
    public static class OpenWeatherForecastWeather {
        private String main;
        private String description;
        private String icon;
    }

    @Getter
    public static class OpenWeatherForecastWind {
        private float speed;
        private float deg;
    }
}
