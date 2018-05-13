package com.omnicrola.justsimpleweather.api;


public enum WeatherUnits {
    KELVIN("K", "mps"), METRIC("C", "mps"), IMPERIAL("F", "mph");

    private String temperatureLetter;
    private String windUnit;

    WeatherUnits(String temperatureLetter, String windUnit) {
        this.temperatureLetter = temperatureLetter;
        this.windUnit = windUnit;
    }

    public String getTemperatureLetter() {
        return temperatureLetter;
    }

    public String getWindUnit() {
        return windUnit;
    }
}
