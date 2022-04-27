package ru.mirea.panin.mireaproject.ui.audiorecorder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.mirea.panin.mireaproject.databinding.FragmentAudioBinding;

public class AudioFragment extends Fragment {
    private static final int REQUEST_CODE_PERMISSION = 100;

    private FragmentAudioBinding binding;
    private MediaRecorder mediaRecorder;
    private static File audioFile;
    private String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
    private Button play, stop, start, stopRec;
    private boolean isWork;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAudioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        isWork = hasPermission(requireContext(), PERMISSIONS);
        mediaRecorder = new MediaRecorder();
        play = binding.playBtn;
        stop = binding.stopBtn;
        start = binding.recBtn;
        stopRec = binding.stopRecBtn;
        if (!isWork) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST_CODE_PERMISSION);
        }

        play.setOnClickListener(view -> {
            if (audioFile != null) {
                getActivity().startService(new Intent(getContext(), PlayRecService.class)
                        .putExtra("PATH", audioFile.getAbsolutePath()));
            }
            else
                Toast.makeText(requireContext(), "No records yet", Toast.LENGTH_SHORT).show();
        });

        start.setOnClickListener(view -> {
            try {
                stop.callOnClick();
                startRecording();
                Toast.makeText(requireContext(), "Recording", Toast.LENGTH_SHORT).show();
                play.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        stop.setOnClickListener(view -> {
            requireActivity().stopService(new Intent(getActivity(), PlayRecService.class));
            start.setEnabled(true);
        });

        stopRec.setOnClickListener(view -> {
            try {
                mediaRecorder.stop();
                Toast.makeText(requireContext(), "Recorded successfully", Toast.LENGTH_SHORT).show();
                play.setEnabled(true);
            } catch (IllegalStateException e) {
                Toast.makeText(requireContext(), "Not recording now", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    public static boolean hasPermission(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void startRecording() throws IOException {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            audioFile = new File(requireActivity().getExternalFilesDir(
                    Environment.DIRECTORY_MUSIC), "audio-" + timeStamp + ".3gp");
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
        }
    }

    @Override
    public void onDestroyView() {
        mediaRecorder.release();
        mediaRecorder = null;
        binding = null;
        super.onDestroyView();
    }
}