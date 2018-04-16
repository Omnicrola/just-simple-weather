package com.omnicrola.justsimpleweather.ui.chart;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeAxisValueFormatter implements IAxisValueFormatter {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("EEE dd ha", Locale.getDefault());
    private final Date date;

    public TimeAxisValueFormatter() {
        this.date = new Date();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        long time = (long) value;
        time = time * 1000 * 60;
        this.date.setTime(time);
        return FORMAT.format(this.date);
    }
}
