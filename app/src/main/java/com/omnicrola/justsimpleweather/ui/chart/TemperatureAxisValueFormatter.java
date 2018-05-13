package com.omnicrola.justsimpleweather.ui.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.omnicrola.justsimpleweather.api.WeatherUnits;

public class TemperatureAxisValueFormatter implements IAxisValueFormatter {

    private final WeatherUnits units;

    public TemperatureAxisValueFormatter(WeatherUnits units) {
        this.units = units;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return value + units.getTemperatureLetter();
    }
}
