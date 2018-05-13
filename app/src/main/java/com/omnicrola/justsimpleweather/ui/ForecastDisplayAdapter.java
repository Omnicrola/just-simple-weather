package com.omnicrola.justsimpleweather.ui;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.DashPathEffect;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.omnicrola.justsimpleweather.R;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.api.WeatherUnits;
import com.omnicrola.justsimpleweather.data.WeatherForecasts;
import com.omnicrola.justsimpleweather.ui.chart.TemperatureAxisValueFormatter;
import com.omnicrola.justsimpleweather.ui.chart.TimeAxisValueFormatter;
import com.omnicrola.justsimpleweather.util.UnitConversion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ForecastDisplayAdapter {
    private Activity activity;
    private LineChart lineChart;

    public ForecastDisplayAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setDisplay(WeatherSettings settings, WeatherForecasts forecasts) {
        ArrayList<Entry> minTemps = new ArrayList<Entry>();
        ArrayList<Entry> maxTemps = new ArrayList<Entry>();

        adjustVerticalAxis(settings, forecasts.getForecasts());
        WeatherUnits units = settings.getUnits();
        for (WeatherForecasts.WeatherForecast weatherForecast : forecasts.getForecasts()) {
            long time = weatherForecast.getTimeInFuture() / (1000 * 60);
            float min = UnitConversion.temperature(units, weatherForecast.getMinTemperature());
            float max = UnitConversion.temperature(units, weatherForecast.getMaxTemperature());
            minTemps.add(new Entry(time, min));
            maxTemps.add(new Entry(time, max));
        }
        setData(minTemps, 0, Color.BLUE);
        setData(maxTemps, 1, Color.RED);

        lineChart.invalidate();
        lineChart.requestLayout();
    }

    private void adjustVerticalAxis(WeatherSettings settings, List<WeatherForecasts.WeatherForecast> forecasts) {
        float maxTemp = Float.MIN_VALUE;
        float minTemp = Float.MAX_VALUE;
        WeatherUnits units = settings.getUnits();
        for (WeatherForecasts.WeatherForecast forecast : forecasts) {
            float min = UnitConversion.temperature(units, forecast.getMinTemperature());
            float max = UnitConversion.temperature(units, forecast.getMaxTemperature());
            maxTemp = Math.max(max, maxTemp);
            minTemp = Math.min(min, minTemp);
        }

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaximum(maxTemp + 20f);
        leftAxis.setAxisMinimum(minTemp + -20f);
    }

    public void initDisplay(WeatherSettings settings) {

        lineChart = activity.findViewById(R.id.chart1);
        lineChart.setDragEnabled(true);

        WeatherUnits units = settings.getUnits();
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setValueFormatter(new TemperatureAxisValueFormatter(units));
        leftAxis.setAxisMaximum(120f);
        leftAxis.setAxisMinimum(-50f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new TimeAxisValueFormatter());
        xAxis.setDrawLabels(true);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateX(500);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setData(List<Entry> values, int index, int color) {

        LineDataSet dataSet;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() == index - 1) {
            dataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(index);
            dataSet.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            dataSet = new LineDataSet(values, "");

            dataSet.setDrawIcons(false);

            dataSet.setColor(color);
            dataSet.setCircleColor(color);
            dataSet.setLineWidth(1f);
            dataSet.setCircleRadius(3f);
            dataSet.setDrawCircleHole(false);
            dataSet.setValueTextSize(9f);
            dataSet.setDrawFilled(true);
            dataSet.setFormLineWidth(1f);
            dataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            dataSet.setFormSize(15.f);

            dataSet.setDrawValues(false);

            dataSet.setFillColor(color);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            lineChart.setData(data);
        }
    }
}
