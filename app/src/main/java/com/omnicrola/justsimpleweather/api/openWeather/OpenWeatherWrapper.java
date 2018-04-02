package com.omnicrola.justsimpleweather.api.openWeather;

import android.util.Log;

import com.omnicrola.justsimpleweather.BuildConfig;
import com.omnicrola.justsimpleweather.api.ResultHandler;
import com.omnicrola.justsimpleweather.api.WeatherApi;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.data.WeatherForecasts;
import com.omnicrola.justsimpleweather.data.WeatherReport;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherWrapper implements WeatherApi {

    private final OpenWeatherApi openWeatherApi;
    private final String appId;

    public OpenWeatherWrapper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + BuildConfig.OPEN_WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherApi = retrofit.create(OpenWeatherApi.class);
        appId = BuildConfig.OPEN_WEATHER_APPID;
    }

    @Override
    public void getCurrentWeather(WeatherSettings weatherSettings, final ResultHandler<WeatherReport> reportHandler) {
        String zip = weatherSettings.getZipCode() + "," + weatherSettings.getCountry();
        String units = String.valueOf(weatherSettings.getUnits());
        Log.i("open-weather", "Getting weather for : " + zip + " " + units);
        Call<OpenWeatherData> weatherForLocation = openWeatherApi.getWeatherForLocation(zip, units, appId);
        weatherForLocation.enqueue(new OpenWeatherDataAdapter(reportHandler));
    }

    @Override
    public void getWeatherForecast(WeatherSettings weatherSettings, ResultHandler<WeatherForecasts> reportHandler) {
        String zip = weatherSettings.getZipCode() + "," + weatherSettings.getCountry();
        String units = String.valueOf(weatherSettings.getUnits());
        Log.i("open-weather", "Getting forecast for : " + zip + " " + units);
        Call<OpenWeatherForecastData> weatherForecast = openWeatherApi.getWeatherForecast(zip, units, appId);
        weatherForecast.enqueue(new OpenWeatherForecastDataAdapter(reportHandler));
    }

}
