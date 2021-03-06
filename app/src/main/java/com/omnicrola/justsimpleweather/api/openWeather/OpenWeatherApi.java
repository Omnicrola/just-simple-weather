package com.omnicrola.justsimpleweather.api.openWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApi {

    @GET("weather")
    Call<OpenWeatherData> getWeatherForLocation(@Query("q") String cityAndCountry, @Query("units") String units, @Query("APPID") String appId);

    @GET("forecast")
    Call<OpenWeatherForecastData> getWeatherForecast(@Query("q") String cityAndCountry, @Query("units") String units, @Query("APPID") String appid);
}
