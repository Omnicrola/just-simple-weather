package com.omnicrola.justsimpleweather.util;

public class WindConverter {

    private static final String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N"};

    public static String directionFromDegrees(float degrees) {
        int directionIndex = Math.round((degrees % 360f) / 22.5f);
        return directions[directionIndex];
    }

}
