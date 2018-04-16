package com.omnicrola.justsimpleweather.data.storage;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.omnicrola.justsimpleweather.api.WeatherSettings;
import com.omnicrola.justsimpleweather.util.Possible;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class DataStorageManager {
    private static final String LOG_TAG = "file-io";

    private final Context context;
    private final Gson gson;

    public DataStorageManager(Context context) {
        this.context = context;
        gson = new Gson();
    }

    public void write(String filename, Object data) {
        try {
            String json = gson.toJson(data);
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
            Log.i(LOG_TAG, "Data saved");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating file", e);
        }
    }

    public <T> Possible<T> read(String filename, Type returnType) {
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            T data = gson.fromJson(bufferedReader, returnType);
            fileInputStream.close();
            return Possible.of(data);
        } catch (IOException e) {
            Log.e("", "Error reading from file", e);
        }
        return Possible.empty();
    }

    public <T> T read(String filename, Class<T> returnType) {
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            T data = gson.fromJson(bufferedReader, returnType);
            fileInputStream.close();
            return data;
        } catch (IOException e) {
            Log.e("", "Error reading from file", e);
        }
        return null;
    }


    public boolean fileIsMissing(String filename) {
        File file = new File(context.getFilesDir(), filename);
        return !file.exists();
    }

    public void createEmptyFile(String filename) {
        File file = new File(context.getFilesDir(), filename);
        try {
            boolean newFile = file.createNewFile();
            if (newFile) {
                Log.i(LOG_TAG, "New data file created.");
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating data file", e);
        }
    }


}
