package com.omnicrola.justsimpleweather.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherReport {
    private float temperature;
    private float humidity;
    private String description;
    private float pressure;
    private float windSpeed;
    private float windDirection;
    private long timestamp;
}
