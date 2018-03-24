package com.omnicrola.justsimpleweather.api;


import com.omnicrola.justsimpleweather.data.WeatherReport;

public interface WeatherApi {

    void getCurrentWeather(WeatherSettings weatherSettings, ResultHandler<WeatherReport> reportHandler);
}
