package com.example.lab1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeightFragment extends Fragment {

    private ConvertService service;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            WeightFragment.this.service = ((ConvertService.TestBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };


    public static WeightFragment newInstance() {

        Bundle args = new Bundle();

        WeightFragment fragment = new WeightFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(getContext(), ConvertService.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Spinner spinnerInput = view.findViewById(R.id.weightInputList);
        Spinner spinnerOutput = view.findViewById(R.id.weightOutputList);
        EditText input = view.findViewById(R.id.weightInput);
        EditText output = view.findViewById(R.id.weightOutput);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<>(service.weights.keySet()));

        spinnerInput.setAdapter(adapter);
        spinnerOutput.setAdapter(adapter);

        view.findViewById(R.id.calcWeightBtn).setOnClickListener(v -> {

            float inputValue = Float.parseFloat(input.getText().toString());
            String inputValueStr = input.getText().toString();

            if (TextUtils.isEmpty(inputValueStr)) {
                Toast.makeText(getContext(), "Please input value", Toast.LENGTH_SHORT).show();
            } else {
                float value = service.convert(inputValue, service.weights.get(spinnerInput.getSelectedItem().toString()), service.weights.get(spinnerOutput.getSelectedItem().toString()));
                output.setText(Float.toString(value));
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), ConvertService.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unbindService(connection);
    }

}
