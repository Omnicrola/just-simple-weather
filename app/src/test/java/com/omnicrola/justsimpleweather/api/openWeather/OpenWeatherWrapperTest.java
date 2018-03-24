package com.omnicrola.justsimpleweather.api.openWeather;

import com.omnicrola.justsimpleweather.api.ResultHandler;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.data.WeatherReport;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import dalvik.annotation.TestTarget;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OpenWeatherWrapperTest {

    private CountDownLatch lock = new CountDownLatch(1);

    private OpenWeatherWrapper openWeatherWrapper;
    private WeatherReport actualReport;
    private String errorMessage;

    @Before
    public void setup() {
        openWeatherWrapper = new OpenWeatherWrapper();
        actualReport = null;

    }

    @Test
    public void getCurentWeather_getsData_Functional() throws Exception {
        WeatherSettings weatherSettings = new WeatherSettings();
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
}