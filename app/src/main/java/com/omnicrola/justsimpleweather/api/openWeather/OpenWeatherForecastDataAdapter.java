package com.omnicrola.justsimpleweather.api.openWeather;

import android.util.Log;

import com.omnicrola.justsimpleweather.api.ResultHandler;
import com.omnicrola.justsimpleweather.data.WeatherForecasts;
import com.omnicrola.justsimpleweather.data.WeatherReport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class OpenWeatherForecastDataAdapter implements Callback<OpenWeatherForecastData> {
    private static final String LOG_TAG = "weather-api";
    private ResultHandler<WeatherForecasts> reportHandler;

    public OpenWeatherForecastDataAdapter(ResultHandler<WeatherForecasts> reportHandler) {
        this.reportHandler = reportHandler;
    }

    @Override
    public void onResponse(Call<OpenWeatherForecastData> call, Response<OpenWeatherForecastData> response) {
        if (response.isSuccessful()) {
            Log.i(LOG_TAG, "Retrieved weather data successfully.");
            OpenWeatherForecastData body = response.body();

            List<WeatherForecasts.WeatherForecast> allForecasts = convertForecasts(body.getList());
            WeatherForecasts forecast = WeatherForecasts.builder()
                    .timeOfForecast(System.currentTimeMillis())
                    .forecasts(allForecasts)
                    .build();

            reportHandler.handle(forecast);
        } else {
            String message = response.code() + " " + response.message() + "\n" + response.raw().request().url();
            Log.e(LOG_TAG, "Error calling Open Weather API: +" + message);
            reportHandler.reject(message);
        }

    }

    private List<WeatherForecasts.WeatherForecast> convertForecasts(List<OpenWeatherForecastData.OpenWeatherForecastListItem> openWeatherForecasts) {
        ArrayList<WeatherForecasts.WeatherForecast> weatherForecasts = new ArrayList<>();
        for (OpenWeatherForecastData.OpenWeatherForecastListItem openWeatherForecast : openWeatherForecasts) {
            WeatherForecasts.WeatherForecast forecast = WeatherForecasts.WeatherForecast.builder()
                    .timeInFuture(openWeatherForecast.getDt() * 1000)
                    .minTemperature(openWeatherForecast.getMain().getTemp_min())
                    .maxTemperature(openWeatherForecast.getMain().getTemp_max())
                    .humidity(openWeatherForecast.getMain().getHumidity())
                    .pressure(openWeatherForecast.getMain().getGrnd_level())
                    .windSpeed(openWeatherForecast.getWind().getSpeed())
                    .windDirection(openWeatherForecast.getWind().getDeg())
                    .description(getDescription(openWeatherForecast.getWeather()))
                    .build();
            weatherForecasts.add(forecast);
        }

        return weatherForecasts;
    }

    private String getDescription(List<OpenWeatherForecastData.OpenWeatherForecastWeather> openWeatherData) {
        StringBuilder stringBuffer = new StringBuilder();
        for (OpenWeatherForecastData.OpenWeatherForecastWeather openWeatherWeather : openWeatherData) {
            stringBuffer.append(openWeatherWeather.getDescription());
            stringBuffer.append(", ");
        }
        stringBuffer.delete(stringBuffer.length() - 2, stringBuffer.length());
        return stringBuffer.toString();
    }


    @Override
    public void onFailure(Call<OpenWeatherForecastData> call, Throwable t) {
        Log.e("weather-api", "Error calling Open Weather API", t);
        reportHandler.reject(t.getMessage());
    }
}
