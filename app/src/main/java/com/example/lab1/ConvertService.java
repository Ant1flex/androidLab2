package com.example.lab1;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ConvertService extends Service {

    private static final float MINIMUM_CELSIUS = -273.15f;
    private static final float MINIMUM_KELVIN = 0;
    private static final float MINIMUM_FAHRENHEIT = -459.67f;

    static Map<String, Float> lengths = new HashMap<String, Float>();
    static Map<String, Float> weights = new HashMap<String, Float>();
    static Map<String, Float> temperatures = new HashMap<String, Float>();

    private TestBinder binder = new TestBinder();

    static String[] mults;

    protected Spinner spinnerInput;
    protected Spinner spinnerOutput;
    protected Button convertButton;
    protected EditText input;
    protected EditText output;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected float convert(float value, float inputMult, float outputMult) {
        return value * inputMult / outputMult;
    }

    public float convertTemp(float value, String tempIn, String tempOut) {
        switch (tempIn) {
            case "Celsius": {
                if (value >= MINIMUM_CELSIUS) {
                    if (tempOut.equals("Kelvin"))
                        return value + 273.15f;
                    else if (tempOut.equals("Fahrenheit"))
                        return (value * 9 / 5) + 32;
                } else {
                    Toast.makeText(this, "Please input value", Toast.LENGTH_SHORT).show();
                    return -1000;
                }
                return value;
            }
            case "Kelvin": {
                if (value >= MINIMUM_KELVIN) {
                    if (tempOut.equals("Celsius"))
                        return value - 273.15f;
                    else if (tempOut.equals("Fahrenheit"))
                        return (value - 273.15f) * 9 / 5 + 32;
                } else {
                    Toast.makeText(this, "Please input value", Toast.LENGTH_SHORT).show();
                    return -1000;
                }
                return value;
            }
            case "Fahrenheit": {
                if (value >= MINIMUM_FAHRENHEIT) {
                    if (tempOut.equals("Celsius"))
                        return (value - 32) * 5 / 9;
                    else if (tempOut.equals("Kelvin"))
                        return (value - 32) * 5 / 9 + 273.15f;
                } else {
                    Toast.makeText(this, "Please input value", Toast.LENGTH_SHORT).show();
                    return -1000;
                }
                return value;

            }
            default:
                return value;
        }
    }

    public void openFile(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("multipliers.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                System.out.println(mLine);
                mults = mLine.split(" ");
                for (String mult : mults) {
                    System.out.println(mult);
                }
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    public void initListL(){
        {{
            lengths.put(getString(R.string.length_centimeter), Float.parseFloat(mults[0]));
            lengths.put(getString(R.string.length_meter), Float.parseFloat(mults[1]));
            lengths.put(getString(R.string.length_kilometer), Float.parseFloat(mults[2]));
            lengths.put(getString(R.string.length_inch), Float.parseFloat(mults[3]));
            lengths.put(getString(R.string.length_mile), Float.parseFloat(mults[4]));
            lengths.put(getString(R.string.length_yard), Float.parseFloat(mults[5]));
            lengths.put(getString(R.string.length_foot), Float.parseFloat(mults[6]));
        }}
    }

    public void initListW(){
        {{
            weights.put(getString(R.string.weight_gram), Float.parseFloat(mults[7]));
            weights.put(getString(R.string.weight_kilogram), Float.parseFloat(mults[8]));
            weights.put(getString(R.string.weight_ton), Float.parseFloat(mults[9]));
            weights.put(getString(R.string.weight_carat), Float.parseFloat(mults[10]));
            weights.put(getString(R.string.weight_pound), Float.parseFloat(mults[11]));
            weights.put(getString(R.string.weight_pood), Float.parseFloat(mults[12]));
        }}
    }

    public void initListT(){
        {{
            temperatures.put(getString(R.string.temperature_kelvin), Float.parseFloat(mults[13]));
            temperatures.put(getString(R.string.temperature_celsius), Float.parseFloat(mults[14]));
            temperatures.put(getString(R.string.temperature_fahrenheit), Float.parseFloat(mults[15]));
        }}
    }

    public class TestBinder extends Binder {

        public ConvertService getService() {
            return ConvertService.this;
        }
    }
}
