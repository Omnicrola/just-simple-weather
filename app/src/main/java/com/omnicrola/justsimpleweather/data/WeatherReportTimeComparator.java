package com.omnicrola.justsimpleweather.data;

public class WeatherReportTimeComparator implements java.util.Comparator<WeatherReport> {

    @Override
    public int compare(WeatherReport report1, WeatherReport report2) {
        return (int) (report1.getTimestamp() - report2.getTimestamp());
    }
}
