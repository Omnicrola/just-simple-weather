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
import com.omnicrola.justsimpleweather.data.WeatherForecasts;
import com.omnicrola.justsimpleweather.ui.chart.TimeAxisValueFormatter;

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

    public void setDisplay(WeatherForecasts forecasts) {
        ArrayList<Entry> minTemps = new ArrayList<Entry>();
        ArrayList<Entry> maxTemps = new ArrayList<Entry>();

        adjustVerticalAxis(forecasts.getForecasts());

        for (WeatherForecasts.WeatherForecast weatherForecast : forecasts.getForecasts()) {
            long time = weatherForecast.getTimeInFuture() / (1000*60);
            minTemps.add(new Entry(time, weatherForecast.getMinTemperature()));
            maxTemps.add(new Entry(time, weatherForecast.getMaxTemperature()));
        }
        setData(minTemps, 0, Color.BLUE);
        setData(maxTemps, 1, Color.RED);

        lineChart.invalidate();
        lineChart.requestLayout();
    }

    private void adjustVerticalAxis(List<WeatherForecasts.WeatherForecast> forecasts) {
        float maxTemp = Float.MIN_VALUE;
        float minTemp = Float.MAX_VALUE;
        for (WeatherForecasts.WeatherForecast forecast : forecasts) {
            maxTemp = Math.max(forecast.getMaxTemperature(), maxTemp);
            minTemp = Math.min(forecast.getMinTemperature(), minTemp);
        }

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaximum(maxTemp + 20f);
        leftAxis.setAxisMinimum(minTemp + -20f);

    }

    public void initDisplay() {

        lineChart = activity.findViewById(R.id.chart1);
        lineChart.setDragEnabled(true);

        YAxis leftAxis = lineChart.getAxisLeft();
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
