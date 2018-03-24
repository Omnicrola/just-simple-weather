package com.omnicrola.justsimpleweather.api.openWeather;

import android.util.Log;

import com.omnicrola.justsimpleweather.api.ResultHandler;
import com.omnicrola.justsimpleweather.data.WeatherReport;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenWeatherDataAdapter implements Callback<OpenWeatherData> {

    private static final String LOG_TAG = "weather-api";
    private final ResultHandler<WeatherReport> reportHandler;

    public OpenWeatherDataAdapter(ResultHandler<WeatherReport> reportHandler) {
        this.reportHandler = reportHandler;
    }

    @Override
    public void onResponse(Call<OpenWeatherData> call, Response<OpenWeatherData> response) {
        if (response.isSuccessful()) {
            Log.i(LOG_TAG, "Retrieved weather data successfully.");
            OpenWeatherData openWeatherData = response.body();
            String description = getDescription(openWeatherData);
            OpenWeatherData.OpenWeatherMain mainWeather = openWeatherData.getMain();
            OpenWeatherData.OpenWeatherWind wind = openWeatherData.getWind();

            WeatherReport weatherReport = WeatherReport.builder()
                    .temperature(mainWeather.getTemp())
                    .humidity(mainWeather.getHumidity())
                    .description(description)
                    .pressure(mainWeather.getPressure())
                    .windDirection(wind.getDeg())
                    .windSpeed(wind.getSpeed())
                    .timestamp(System.currentTimeMillis())
                    .build();

            Log.i(LOG_TAG, "Mapped OpenWeather data to local data model.");
            reportHandler.handle(weatherReport);
        } else {
            String message = response.code() + " " + response.message()+"\n"+response.raw().request().url();
            Log.e(LOG_TAG, "Error calling Open Weather API: +" + message);
            reportHandler.reject(message);
        }
    }

    @Override
    public void onFailure(Call<OpenWeatherData> call, Throwable t) {
        Log.e("weather-api", "Error calling Open Weather API", t);
        reportHandler.reject(t.getMessage());
    }

    private String getDescription(OpenWeatherData openWeatherData) {
        List<OpenWeatherData.OpenWeatherWeather> weather = openWeatherData.getWeather();
        StringBuilder stringBuffer = new StringBuilder();
        for (OpenWeatherData.OpenWeatherWeather openWeatherWeather : weather) {
            stringBuffer.append(openWeatherWeather.getDescription());
            stringBuffer.append(", ");
        }
        stringBuffer.delete(stringBuffer.length() - 2, stringBuffer.length());
        return stringBuffer.toString();
    }

}
