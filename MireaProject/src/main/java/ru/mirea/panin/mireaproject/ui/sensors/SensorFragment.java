package ru.mirea.panin.mireaproject.ui.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ru.mirea.panin.mireaproject.databinding.FragmentSensorBinding;

public class SensorFragment extends Fragment implements SensorEventListener {

    private FragmentSensorBinding binding;
    private SensorManager sensorManager;
    private Sensor lightSensor, tempSensor, accelerometerSensor;
    private TextView light, temp, azimuth, pitch, roll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSensorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = binding.lightSensorView;
        temp = binding.tempSensorView;
        azimuth = binding.azimuthTextView;
        pitch = binding.pitchTextView;
        roll = binding.rollTextView;
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                float valueAzimuth = sensorEvent.values[0];
                float valuePitch = sensorEvent.values[1];
                float valueRoll = sensorEvent.values[2];
                azimuth.setText("Azimuth: " + valueAzimuth);
                pitch.setText("Pitch: " + valuePitch);
                roll.setText("Roll: " + valueRoll);
                break;
            case Sensor.TYPE_LIGHT:
                light.setText(sensorEvent.values[0] + " lux");
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                temp.setText(sensorEvent.values[0] + " °C");
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}