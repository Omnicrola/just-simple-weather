package com.omnicrola.justsimpleweather.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherSettings {
    private WeatherUnits units;
    private String zipCode;
    private String country;
}
