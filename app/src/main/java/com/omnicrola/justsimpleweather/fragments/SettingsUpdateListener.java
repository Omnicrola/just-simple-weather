package com.omnicrola.justsimpleweather.fragments;

import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.omnicrola.justsimpleweather.R;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.api.WeatherUnits;
import com.omnicrola.justsimpleweather.data.storage.DataStorageService;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class SettingsUpdateListener implements AdapterView.OnItemSelectedListener {
    private final DataStorageService dataStorageService;
    private final Spinner countrySpinner;
    private final Spinner unitSpinner;
    private final EditText cityEditText;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String country = countrySpinner.getSelectedItem().toString();
        WeatherUnits units = WeatherUnits.valueOf(unitSpinner.getSelectedItem().toString().toUpperCase());

        String city = cityEditText.getText().toString();
        WeatherSettings weatherSettings = new WeatherSettings(units, city, country);
        dataStorageService.saveSettings(weatherSettings);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
