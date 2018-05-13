package com.omnicrola.justsimpleweather.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omnicrola.justsimpleweather.R;
import com.omnicrola.justsimpleweather.data.WeatherForecasts;
import com.omnicrola.justsimpleweather.data.storage.DataStorageService;
import com.omnicrola.justsimpleweather.ui.ForecastDisplayAdapter;
import com.omnicrola.justsimpleweather.util.Possible;

public class ForecastFragment extends Fragment {

    private DataStorageService dataStorageService;
    private ForecastDisplayAdapter displayAdapter;

    public ForecastFragment() {
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance() {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.dataStorageService = new DataStorageService(getActivity().getApplicationContext());
        this.displayAdapter = new ForecastDisplayAdapter(getActivity());
    }


    @Override
    public void onStart() {
        super.onStart();
        this.displayAdapter.initDisplay();
        updateForecastFromStorage();
    }

    private void updateForecastFromStorage() {
        Possible<WeatherForecasts> forecast = dataStorageService.getForecast();
        if (forecast.isPresent()) {
            WeatherForecasts weatherForecasts = forecast.get();
            this.displayAdapter.setDisplay(weatherForecasts);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
