package com.omnicrola.justsimpleweather.fragments;


import android.app.Fragment;
import android.bluetooth.le.AdvertiseData;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.omnicrola.justsimpleweather.R;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.data.storage.DataStorageService;
import com.omnicrola.justsimpleweather.util.SpinnerLoader;

public class SettingsFragment extends Fragment {


    private DataStorageService dataStorageService;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.dataStorageService = new DataStorageService(getActivity().getApplicationContext());
    }


    @Override
    public void onStart() {
        super.onStart();
        WeatherSettings settings = dataStorageService.getSettings();

        EditText zipCode = getView().findViewById(R.id.settings_zip_code);
        SettingsUpdateListener updateListener = SettingsUpdateListener.builder()
                .dataStorageService(dataStorageService)
                .zipcodeView(zipCode)
                .countrySpinner((Spinner) getView().findViewById(R.id.settings_country))
                .unitSpinner((Spinner) getView().findViewById(R.id.settings_units))
                .build();

        SpinnerLoader.using(getActivity())
                .find(R.id.settings_country)
                .inView(getView())
                .loadItemsFrom(R.array.country_codes)
                .setTo(settings.getCountry())
                .onSelected(updateListener)
                .finish();
        SpinnerLoader.using(getActivity())
                .find(R.id.settings_units)
                .inView(getView())
                .loadItemsFrom(R.array.units_list)
                .setTo(settings.getUnits().toString())
                .onSelected(updateListener)
                .finish();

        zipCode.setText(settings.getZipCode());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
