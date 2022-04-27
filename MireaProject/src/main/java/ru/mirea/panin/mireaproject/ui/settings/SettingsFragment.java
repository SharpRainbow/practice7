package ru.mirea.panin.mireaproject.ui.settings;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Switch;

import ru.mirea.panin.mireaproject.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private Switch mode, mute;
    private EditText name;
    private boolean dark;
    private boolean muted;
    private SharedPreferences preferences;
    private final String MODE_TAG = "MODE";
    private final String MUTE_TAG = "MUTE";
    private final String USER_TAG = "USER";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mode = binding.switchMode;
        mute = binding.switchMute;
        name = binding.editTextName;
        mode.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
            else
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
            dark = b;
        });
        mute.setOnCheckedChangeListener((compoundButton, b) -> {
            AudioManager amanager = (AudioManager) requireActivity().getSystemService(getContext().AUDIO_SERVICE);
            if (b)
                amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            else
                amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            muted = b;
        });

        preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        dark = preferences.getBoolean(MODE_TAG, false);
        muted = preferences.getBoolean(MUTE_TAG, false);
        String user = preferences.getString(USER_TAG, "Admin");
        name.setText(user);
        if (dark || getResources().getConfiguration().uiMode == 33)
            mode.performClick();
        if (muted)
            mute.performClick();
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_TAG, name.getText().toString());
        editor.putBoolean(MODE_TAG, dark);
        editor.putBoolean(MUTE_TAG, muted);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}