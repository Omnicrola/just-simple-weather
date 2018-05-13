package com.omnicrola.justsimpleweather.util;

import android.app.Activity;
import android.renderscript.RSInvalidStateException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.omnicrola.justsimpleweather.R;

public class SpinnerLoader {


    public static SpinnerLoader using(Activity activity) {
        return new SpinnerLoader(activity);
    }

    private int spinnerId = Integer.MIN_VALUE;
    private int templateId = android.R.layout.simple_spinner_dropdown_item;
    private int resourceId;
    private String value;
    private AdapterView.OnItemSelectedListener listener;
    private Activity activity;
    private View view;

    public SpinnerLoader(Activity activity) {
        this.activity = activity;
    }

    public SpinnerLoader(View view) {
        this.view = view;
    }

    public SpinnerLoader find(int spinnerId) {
        this.spinnerId = spinnerId;
        return this;
    }

    public SpinnerLoader inView(View view) {
        this.view = view;
        return this;
    }

    public SpinnerLoader onSelected(AdapterView.OnItemSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    public SpinnerLoader usingTemplate(int templateId) {
        this.templateId = templateId;
        return this;
    }

    public SpinnerLoader loadItemsFrom(int resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public void finish() {
        Spinner countrySpinner = null;
        if (view != null) {
            countrySpinner = activity.findViewById(spinnerId);
        } else if (activity != null) {
            countrySpinner = view.findViewById(spinnerId);
        }
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(activity, resourceId, templateId);
        arrayAdapter.setDropDownViewResource(templateId);
        countrySpinner.setAdapter(arrayAdapter);

        if (listener != null) {
            countrySpinner.setOnItemSelectedListener(listener);
        }

        int position = arrayAdapter.getPosition(value);
        countrySpinner.setSelection(position);
    }

    public SpinnerLoader setTo(String value) {
        this.value = value;
        return this;
    }
}
