package com.omnicrola.justsimpleweather.api.openWeather;

import com.omnicrola.justsimpleweather.api.ResultHandler;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.api.WeatherUnits;
import com.omnicrola.justsimpleweather.data.WeatherForecasts;
import com.omnicrola.justsimpleweather.data.WeatherReport;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import dalvik.annotation.TestTarget;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OpenWeatherWrapperTest {

    private CountDownLatch lock = new CountDownLatch(1);

    private OpenWeatherWrapper openWeatherWrapper;
    private WeatherReport actualReport;
    private WeatherForecasts actualForecast;
    private String errorMessage;
    private WeatherSettings weatherSettings;

    @Before
    public void setup() {
        openWeatherWrapper = new OpenWeatherWrapper();
        actualReport = null;

        weatherSettings = WeatherSettings.builder()
                .country("us")
                .zipCode("90210")
                .units(WeatherUnits.IMPERIAL)
                .build();
    }

    @Test
    public void getCurentWeather_getsData_Functional() throws Exception {
        openWeatherWrapper.getCurrentWeather(weatherSettings, new ResultHandler<WeatherReport>() {
            @Override
            public void handle(WeatherReport result) {
                actualReport = result;
                lock.countDown();
            }

            @Override
            public void reject(String reason) {
                errorMessage = reason;
                lock.countDown();
            }
        });

        lock.await(20000, TimeUnit.MILLISECONDS);

        assertNull("No error expected", errorMessage);
        assertNotNull("No data, did request time out?", actualReport);
        assertNotEquals(actualReport.getTemperature(), 0f);
        assertNotEquals(actualReport.getHumidity(), 0f);
        assertNotEquals(actualReport.getWindSpeed(), 0f);
        assertNotEquals(actualReport.getWindDirection(), 0f);
        assertNotEquals(actualReport.getDescription().length(), 0);
        assertNotEquals(actualReport.getTimestamp(), 0L);
    }

    @Test
    public void getForecast_getsData_Functional() throws Exception {
        openWeatherWrapper.getWeatherForecast(weatherSettings, new ResultHandler<WeatherForecasts>() {
            @Override
            public void handle(WeatherForecasts result) {
                actualForecast = result;
                lock.countDown();
            }

            @Override
            public void reject(String reason) {
                errorMessage = reason;
                lock.countDown();
            }
        });

        lock.await(20000, TimeUnit.MILLISECONDS);

        assertNull("No error expected", errorMessage);
        assertNotNull("No data, did request time out?", actualForecast);
        assertNotNull(actualForecast.getForecasts());
        assertNotEquals(actualForecast.getForecasts().size(), 0);
        WeatherForecasts.WeatherForecast firstForecast = actualForecast.getForecasts().get(0);

        assertNotEquals(firstForecast.getMinTemperature(), 0f);
        assertNotEquals(firstForecast.getMaxTemperature(), 0f);
        assertNotEquals(firstForecast.getHumidity(), 0f);
        assertNotEquals(firstForecast.getWindSpeed(), 0f);
        assertNotEquals(firstForecast.getWindDirection(), 0f);
        assertNotEquals(firstForecast.getDescription().length(), 0);
        assertNotEquals(firstForecast.getTimeInFuture(), 0L);
        assertNotEquals(firstForecast.getPressure(), 0L);

    }
}