package com.omnicrola.justsimpleweather.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherSettings {
    private WeatherUnits units;
    private String zipCode;
    private String country;
}
