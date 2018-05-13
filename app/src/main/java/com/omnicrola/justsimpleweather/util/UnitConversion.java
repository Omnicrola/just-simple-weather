package com.omnicrola.justsimpleweather.util;

import com.omnicrola.justsimpleweather.api.WeatherUnits;

public class UnitConversion {

    // it is assumed that all values are always stored in the same format regaurdless of what format
    // the user chooses to view. Assumption is Kelvin for temperature, m/sec for wind speed
    public static float temperature(WeatherUnits units, float baseTemp) {
        switch (units) {
            case KELVIN:
                return baseTemp;
            case METRIC:
                return baseTemp - 273.15f;
            case IMPERIAL:
                return (((baseTemp - 273f) * 9f / 5f) + 32f);
            default:
                return baseTemp;
        }
    }

    public static float speed(WeatherUnits units, float baseSpeed) {
        switch (units) {
            case KELVIN:
                return baseSpeed;
            case METRIC:
                return baseSpeed;
            case IMPERIAL:
                return baseSpeed / 0.44704f;
            default:
                return baseSpeed;
        }
    }

}
