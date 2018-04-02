package com.omnicrola.justsimpleweather.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherForecasts {
    private long timeOfForecast;
    private List<WeatherForecast> forecasts;

    @Getter
    @Builder
    public static class WeatherForecast  {
        private long timeInFuture;
        private float minTemperature;
        private float maxTemperature;
        private float humidity;
        private String description;
        private float pressure;
        private float windSpeed;
        private float windDirection;
    }
}
