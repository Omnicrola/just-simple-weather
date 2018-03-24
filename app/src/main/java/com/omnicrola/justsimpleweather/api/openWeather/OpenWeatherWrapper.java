package com.omnicrola.justsimpleweather.api.openWeather;

import android.util.Log;

import com.omnicrola.justsimpleweather.BuildConfig;
import com.omnicrola.justsimpleweather.api.ResultHandler;
import com.omnicrola.justsimpleweather.api.WeatherApi;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.data.WeatherReport;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherWrapper implements WeatherApi {

    private final OpenWeatherApi openWeatherApi;

    public OpenWeatherWrapper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + BuildConfig.OPEN_WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherApi = retrofit.create(OpenWeatherApi.class);
    }

    @Override
    public void getCurrentWeather(WeatherSettings weatherSettings, final ResultHandler<WeatherReport> reportHandler) {
        String zip = weatherSettings.getZipCode() + "," + weatherSettings.getCountry();
        String units = String.valueOf(weatherSettings.getUnits());
        String appId = BuildConfig.OPEN_WEATHER_APPID;
        Log.i("open-weather", "Getting weather for : " + zip + " " + units);
        Call<OpenWeatherData> weatherForLocation = openWeatherApi.getWeatherForLocation(zip, units, appId);
        weatherForLocation.enqueue(new OpenWeatherDataAdapter(reportHandler));
    }

}
