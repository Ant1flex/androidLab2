package com.example.lab1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MenuFragment extends Fragment {

    private ConvertService service;

    public Button lengthBtn;
    public Button weightBtn;
    public Button temperatureBtn;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MenuFragment.this.service = ((ConvertService.TestBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(getContext(), ConvertService.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);


        lengthBtn = view.findViewById(R.id.lengthBtn);
        weightBtn = view.findViewById(R.id.weightBtn);
        temperatureBtn = view.findViewById(R.id.temperatureBtn);

        lengthBtn.setOnClickListener(v -> {
            service.openFile();
            service.initListL();
            menuHandler(new LengthFragment());
        });

        weightBtn.setOnClickListener(v -> {
            service.openFile();
            service.initListW();
            menuHandler(new WeightFragment());
        });

        temperatureBtn.setOnClickListener(v -> {
            service.openFile();
            service.initListT();
            menuHandler(new TemperatureFragment());
        });
    }

    public void menuHandler(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, fragment)
                .commit();
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
